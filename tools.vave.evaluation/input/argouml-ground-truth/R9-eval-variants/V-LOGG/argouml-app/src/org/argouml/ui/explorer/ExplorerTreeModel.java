package org.argouml.ui.explorer;

import java.awt.EventQueue;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.InvalidElementException;
import org.argouml.ui.explorer.rules.PerspectiveRule;
import org.argouml.ui.explorer.WeakExplorerNode;


public class ExplorerTreeModel extends DefaultTreeModel implements TreeModelUMLEventListener,ItemListener {
	private static final Logger LOG = Logger.getLogger(ExplorerTreeModel.class);
	private List<PerspectiveRule>rules;
	private Map<Object,Set<ExplorerTreeNode>>modelElementMap;
	private Comparator order;
	private List<ExplorerTreeNode>updatingChildren = new ArrayList<ExplorerTreeNode>();
	private ExplorerUpdater nodeUpdater = new ExplorerUpdater();
	private ExplorerTree tree;
	class ExplorerUpdater implements Runnable {
		private LinkedList<ExplorerTreeNode>pendingUpdates = new LinkedList<ExplorerTreeNode>();
		private boolean hot;
		public static final int MAX_UPDATES_PER_RUN = 100;
		private synchronized void schedule() {
			if (hot) {
				return;
			}
			hot = true;
			EventQueue.invokeLater(this);
		}
		public synchronized void schedule(ExplorerTreeNode node) {
			if (node.getPending()) {
				return;
			}
			pendingUpdates.add(node);
			node.setPending(true);
			schedule();
		}
		public void run() {
			boolean done = false;
			for (int i = 0;i < MAX_UPDATES_PER_RUN;i++) {
				ExplorerTreeNode node = null;
				synchronized (this) {
					if (!pendingUpdates.isEmpty()) {
						node = pendingUpdates.removeFirst();
						node.setPending(false);
					}else {
						done = true;
						hot = false;
						break;
					}
				}
				updateChildren(new TreePath(getPathToRoot(node)));
			}
			if (!done) {
				schedule();
			}else {
				tree.refreshSelection();
			}
		}
	}
	public ExplorerTreeModel(Object root,ExplorerTree myTree) {
		super(new DefaultMutableTreeNode());
		tree = myTree;
		setRoot(new ExplorerTreeNode(root,this));
		setAsksAllowsChildren(false);
		modelElementMap = new HashMap<Object,Set<ExplorerTreeNode>>();
		ExplorerEventAdaptor.getInstance().setTreeModelUMLEventListener(this);
		order = new TypeThenNameOrder();
	}
	public void modelElementChanged(Object node) {
		traverseModified((TreeNode) getRoot(),node);
	}
	public void modelElementAdded(Object node) {
		traverseModified((TreeNode) getRoot(),node);
	}
	private void traverseModified(TreeNode start,Object node) {
		Enumeration children = start.children();
		while (children.hasMoreElements()) {
			TreeNode child = (TreeNode) children.nextElement();
			traverseModified(child,node);
		}
		if (start instanceof ExplorerTreeNode) {
			((ExplorerTreeNode) start).nodeModified(node);
		}
	}
	public void modelElementRemoved(Object node) {
		for (ExplorerTreeNode changeNode:new ArrayList<ExplorerTreeNode>(findNodes(node))) {
			if (changeNode.getParent() != null) {
				removeNodeFromParent(changeNode);
			}
		}
		traverseModified((TreeNode) getRoot(),node);
	}
	public void structureChanged() {
		if (getRoot()instanceof ExplorerTreeNode) {
			((ExplorerTreeNode) getRoot()).remove();
		}
		for (Collection nodes:modelElementMap.values()) {
			nodes.clear();
		}
		modelElementMap.clear();
		modelElementMap = new HashMap<Object,Set<ExplorerTreeNode>>();
		Project proj = ProjectManager.getManager().getCurrentProject();
		ExplorerTreeNode rootNode = new ExplorerTreeNode(proj,this);
		addToMap(proj,rootNode);
		setRoot(rootNode);
	}
	public void updateChildren(TreePath path) {
		ExplorerTreeNode node = (ExplorerTreeNode) path.getLastPathComponent();
		Object modelElement = node.getUserObject();
		if (rules == null) {
			return;
		}
		if (updatingChildren.contains(node)) {
			return;
		}
		updatingChildren.add(node);
		List children = reorderChildren(node);
		List newChildren = new ArrayList();
		Set deps = new HashSet();
		collectChildren(modelElement,newChildren,deps);
		node.setModifySet(deps);
		mergeChildren(node,children,newChildren);
		updatingChildren.remove(node);
	}
	private List<Object>reorderChildren(ExplorerTreeNode node) {
		List<Object>childUserObjects = new ArrayList<Object>();
		List<ExplorerTreeNode>reordered = new ArrayList<ExplorerTreeNode>();
		Enumeration enChld = node.children();
		Object lastObj = null;
		while (enChld.hasMoreElements()) {
			Object child = enChld.nextElement();
			if (child instanceof ExplorerTreeNode) {
				Object obj = ((ExplorerTreeNode) child).getUserObject();
				if (lastObj != null&&order.compare(lastObj,obj) > 0) {
					if (!tree.isPathSelected(new TreePath(getPathToRoot((ExplorerTreeNode) child)))) {
						reordered.add((ExplorerTreeNode) child);
					}else {
						ExplorerTreeNode prev = (ExplorerTreeNode) ((ExplorerTreeNode) child).getPreviousSibling();
						while (prev != null&&(order.compare(prev.getUserObject(),obj) >= 0)) {
							reordered.add(prev);
							childUserObjects.remove(childUserObjects.size() - 1);
							prev = (ExplorerTreeNode) prev.getPreviousSibling();
						}
						childUserObjects.add(obj);
						lastObj = obj;
					}
				}else {
					childUserObjects.add(obj);
					lastObj = obj;
				}
			}else {
				throw new IllegalArgumentException("Incomprehencible child node " + child.toString());
			}
		}
		for (ExplorerTreeNode child:reordered) {
			super.removeNodeFromParent(child);
		}
		for (ExplorerTreeNode child:reordered) {
			Object obj = child.getUserObject();
			int ip = Collections.binarySearch(childUserObjects,obj,order);
			if (ip < 0) {
				ip = -(ip + 1);
			}
			super.insertNodeInto(child,node,ip);
			childUserObjects.add(ip,obj);
		}
		return childUserObjects;
	}
	private void collectChildren(Object modelElement,List newChildren,Set deps) {
		if (modelElement == null) {
			return;
		}
		for (PerspectiveRule rule:rules) {
			Collection children = Collections.emptySet();
			try {
				children = rule.getChildren(modelElement);
			}catch (InvalidElementException e) {
				LOG.debug("InvalidElementException in ExplorerTree : " + e.getStackTrace());
			}
			for (Object child:children) {
				if (child == null) {
					LOG.warn("PerspectiveRule " + rule + " wanted to " + "add null to the explorer tree!");
				}else if ((child != null)&&!newChildren.contains(child)) {
					newChildren.add(child);
				}
			}
			try {
				Set dependencies = rule.getDependencies(modelElement);
				deps.addAll(dependencies);
			}catch (InvalidElementException e) {
				LOG.debug("InvalidElementException in ExplorerTree : " + e.getStackTrace());
			}
		}
		Collections.sort(newChildren,order);
		deps.addAll(newChildren);
	}
	private Set prepareAddRemoveSets(List children,List newChildren) {
		Set removeSet = new HashSet();
		Set commonObjects = new HashSet();
		if (children.size() < newChildren.size()) {
			commonObjects.addAll(children);
			commonObjects.retainAll(newChildren);
		}else {
			commonObjects.addAll(newChildren);
			commonObjects.retainAll(children);
		}
		newChildren.removeAll(commonObjects);
		removeSet.addAll(children);
		removeSet.removeAll(commonObjects);
		Iterator it = removeSet.iterator();
		List weakNodes = null;
		while (it.hasNext()) {
			Object obj = it.next();
			if (!(obj instanceof WeakExplorerNode)) {
				continue;
			}
			WeakExplorerNode node = (WeakExplorerNode) obj;
			if (weakNodes == null) {
				weakNodes = new LinkedList();
				Iterator it2 = newChildren.iterator();
				while (it2.hasNext()) {
					Object obj2 = it2.next();
					if (obj2 instanceof WeakExplorerNode) {
						weakNodes.add(obj2);
					}
				}
			}
			Iterator it3 = weakNodes.iterator();
			while (it3.hasNext()) {
				Object obj3 = it3.next();
				if (node.subsumes(obj3)) {
					it.remove();
					newChildren.remove(obj3);
					it3.remove();
					break;
				}
			}
		}
		return removeSet;
	}
	private void mergeChildren(ExplorerTreeNode node,List children,List newChildren) {
		Set removeObjects = prepareAddRemoveSets(children,newChildren);
		List<ExplorerTreeNode>actualNodes = new ArrayList<ExplorerTreeNode>();
		Enumeration childrenEnum = node.children();
		while (childrenEnum.hasMoreElements()) {
			actualNodes.add((ExplorerTreeNode) childrenEnum.nextElement());
		}
		int position = 0;
		Iterator childNodes = actualNodes.iterator();
		Iterator newNodes = newChildren.iterator();
		Object firstNew = newNodes.hasNext()?newNodes.next():null;
		while (childNodes.hasNext()) {
			Object childObj = childNodes.next();
			if (!(childObj instanceof ExplorerTreeNode)) {
				continue;
			}
			ExplorerTreeNode child = (ExplorerTreeNode) childObj;
			Object userObject = child.getUserObject();
			if (removeObjects.contains(userObject)) {
				removeNodeFromParent(child);
			}else {
				while (firstNew != null&&order.compare(firstNew,userObject) < 0) {
					insertNodeInto(new ExplorerTreeNode(firstNew,this),node,position);
					position++;
					firstNew = newNodes.hasNext()?newNodes.next():null;
				}
				position++;
			}
		}
		while (firstNew != null) {
			insertNodeInto(new ExplorerTreeNode(firstNew,this),node,position);
			position++;
			firstNew = newNodes.hasNext()?newNodes.next():null;
		}
	}
	@Override public void insertNodeInto(MutableTreeNode newChild,MutableTreeNode parent,int index) {
		super.insertNodeInto(newChild,parent,index);
		if (newChild instanceof ExplorerTreeNode) {
			addNodesToMap((ExplorerTreeNode) newChild);
		}
	}
	@Override public void removeNodeFromParent(MutableTreeNode node) {
		if (node instanceof ExplorerTreeNode) {
			removeNodesFromMap((ExplorerTreeNode) node);
			((ExplorerTreeNode) node).remove();
		}
		super.removeNodeFromParent(node);
	}
	private void addNodesToMap(ExplorerTreeNode node) {
		Enumeration children = node.children();
		while (children.hasMoreElements()) {
			ExplorerTreeNode child = (ExplorerTreeNode) children.nextElement();
			addNodesToMap(child);
		}
		addToMap(node.getUserObject(),node);
	}
	private void removeNodesFromMap(ExplorerTreeNode node) {
		Enumeration children = node.children();
		while (children.hasMoreElements()) {
			ExplorerTreeNode child = (ExplorerTreeNode) children.nextElement();
			removeNodesFromMap(child);
		}
		removeFromMap(node.getUserObject(),node);
	}
	private void addToMap(Object modelElement,ExplorerTreeNode node) {
		Set<ExplorerTreeNode>nodes = modelElementMap.get(modelElement);
		if (nodes != null) {
			nodes.add(node);
		}else {
			nodes = new HashSet<ExplorerTreeNode>();
			nodes.add(node);
			modelElementMap.put(modelElement,nodes);
		}
	}
	private void removeFromMap(Object modelElement,ExplorerTreeNode node) {
		Collection<ExplorerTreeNode>nodes = modelElementMap.get(modelElement);
		if (nodes != null) {
			nodes.remove(node);
			if (nodes.isEmpty()) {
				modelElementMap.remove(modelElement);
			}
		}
	}
	private Collection<ExplorerTreeNode>findNodes(Object modelElement) {
		Collection<ExplorerTreeNode>nodes = modelElementMap.get(modelElement);
		if (nodes == null) {
			return Collections.EMPTY_LIST;
		}
		return nodes;
	}
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource()instanceof PerspectiveComboBox) {
			rules = ((ExplorerPerspective) e.getItem()).getList();
		}else {
			order = (Comparator) e.getItem();
		}
		structureChanged();
		tree.expandPath(tree.getPathForRow(1));
	}
	ExplorerUpdater getNodeUpdater() {
		return nodeUpdater;
	}
	private static final long serialVersionUID = 3132732494386565870l;
}



