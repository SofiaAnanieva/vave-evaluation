package org.argouml.uml.diagram.static_structure.ui;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.ArgoFig;
import org.argouml.uml.diagram.ui.ArgoFigUtil;
import org.argouml.util.IItemUID;
import org.argouml.util.ItemUID;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdgePoly;
import org.tigris.gef.presentation.FigNode;


public class FigEdgeNote extends FigEdgePoly implements ArgoFig,IItemUID,PropertyChangeListener {
	private Object comment;
	private Object annotatedElement;
	private DiagramSettings settings;
	private ItemUID itemUid;
	public FigEdgeNote(Object element,DiagramSettings theSettings) {
		super();
		settings = theSettings;
		if (element != null) {
			setOwner(element);
		}else {
			setOwner(new CommentEdge());
		}
		setBetweenNearestPoints(true);
		getFig().setLineWidth(LINE_WIDTH);
		getFig().setDashed(true);
	}
	@Override public void setFig(Fig f) {
		super.setFig(f);
		getFig().setDashed(true);
	}
	@Override public String toString() {
		return Translator.localize("misc.comment-edge");
	}
	protected void modelChanged(PropertyChangeEvent e) {
		if (e instanceof RemoveAssociationEvent&&e.getOldValue() == annotatedElement) {
			removeFromDiagram();
		}
	}
	@Override public String getTipString(MouseEvent me) {
		return"Comment Edge";
	}
	@Override public void propertyChange(PropertyChangeEvent pve) {
		modelChanged(pve);
	}
	@Override public final void removeFromDiagram() {
		Object o = getOwner();
		if (o != null) {
			removeElementListener(o);
		}
		super.removeFromDiagram();
		damage();
	}
	protected Object getSource() {
		Object theOwner = getOwner();
		if (theOwner != null) {
			return((CommentEdge) theOwner).getSource();
		}
		return null;
	}
	protected Object getDestination() {
		Object theOwner = getOwner();
		if (theOwner != null) {
			return((CommentEdge) theOwner).getDestination();
		}
		return null;
	}
	@Override public void setDestFigNode(FigNode fn) {
		if (fn != null&&Model.getFacade().isAComment(fn.getOwner())) {
			Object oldComment = comment;
			if (oldComment != null) {
				removeElementListener(oldComment);
			}
			comment = fn.getOwner();
			if (comment != null) {
				addElementListener(comment);
			}
			((CommentEdge) getOwner()).setComment(comment);
		}else if (fn != null&&!Model.getFacade().isAComment(fn.getOwner())) {
			annotatedElement = fn.getOwner();
			((CommentEdge) getOwner()).setAnnotatedElement(annotatedElement);
		}
		super.setDestFigNode(fn);
	}
	@Override public void setSourceFigNode(FigNode fn) {
		if (fn != null&&Model.getFacade().isAComment(fn.getOwner())) {
			Object oldComment = comment;
			if (oldComment != null) {
				removeElementListener(oldComment);
			}
			comment = fn.getOwner();
			if (comment != null) {
				addElementListener(comment);
			}
			((CommentEdge) getOwner()).setComment(comment);
		}else if (fn != null&&!Model.getFacade().isAComment(fn.getOwner())) {
			annotatedElement = fn.getOwner();
			((CommentEdge) getOwner()).setAnnotatedElement(annotatedElement);
		}
		super.setSourceFigNode(fn);
	}
	private void addElementListener(Object element) {
		Model.getPump().addModelEventListener(this,element);
	}
	private void removeElementListener(Object element) {
		Model.getPump().removeModelEventListener(this,element);
	}
	@SuppressWarnings("deprecation")@Deprecated public Project getProject() {
		return ArgoFigUtil.getProject(this);
	}
	public DiagramSettings getSettings() {
		return settings;
	}
	public void renderingChanged() {
	}
	@SuppressWarnings("deprecation")@Deprecated public void setProject(Project project) {
	}
	public void setSettings(DiagramSettings theSettings) {
		settings = theSettings;
	}
	public void setItemUID(ItemUID newId) {
		itemUid = newId;
	}
	public ItemUID getItemUID() {
		return itemUid;
	}
}



