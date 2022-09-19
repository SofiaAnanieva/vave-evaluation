package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.ui.ArgoJMenu;
import org.argouml.ui.explorer.ExplorerEventAdaptor;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.StereotypeContainer;
import org.argouml.uml.diagram.VisibilityContainer;
import org.argouml.uml.diagram.DiagramFactory.DiagramType;
import org.argouml.uml.diagram.ui.ArgoFig;
import org.argouml.uml.diagram.ui.ArgoFigText;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Geometry;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;
import org.tigris.gef.undo.UndoableAction;


public class FigPackage extends FigNodeModelElement implements StereotypeContainer,VisibilityContainer {
	private static final Logger LOG = Logger.getLogger(FigPackage.class);
	private static final int MIN_HEIGHT = 21;
	private static final int MIN_WIDTH = 50;
	private int width = 140;
	private int height = 100;
	private int indentX = 50;
	private int textH = 20;
	private int tabHeight = 20;
	private FigText body;
	private boolean stereotypeVisible = true;
	@SuppressWarnings("deprecation")@Deprecated public FigPackage(Object node,int x,int y) {
		body = new FigPackageFigText(0,textH,width,height - textH);
		setOwner(node);
		initialize();
		setLocation(x,y);
	}
	private void initialize() {
		body.setEditable(false);
		setBigPort(new PackagePortFigRect(0,0,width,height,indentX,tabHeight));
		getNameFig().setBounds(0,0,width - indentX,textH + 2);
		getNameFig().setJustification(FigText.JUSTIFY_LEFT);
		getBigPort().setFilled(false);
		getBigPort().setLineWidth(0);
		getStereotypeFig().setVisible(false);
		addFig(getBigPort());
		addFig(getNameFig());
		addFig(getStereotypeFig());
		addFig(body);
		setBlinkPorts(false);
		setFilled(true);
		setFillColor(FILL_COLOR);
		setLineColor(LINE_COLOR);
		setLineWidth(LINE_WIDTH);
		updateEdges();
	}
	@Deprecated public FigPackage(@SuppressWarnings("unused")GraphModel gm,Object node) {
		this(node,0,0);
	}
	public FigPackage(Object owner,Rectangle bounds,DiagramSettings settings) {
		super(owner,bounds,settings);
		body = new FigPackageFigText(getOwner(),new Rectangle(0,textH,width,height - textH),getSettings());
		initialize();
		if (bounds != null) {
			setLocation(bounds.x,bounds.y);
		}
		setBounds(getBounds());
	}
	@Override public Object clone() {
		FigPackage figClone = (FigPackage) super.clone();
		for (Fig thisFig:(List<Fig>) getFigs()) {
			if (thisFig == body) {
				figClone.body = (FigText) thisFig;
			}
		}
		return figClone;
	}
	@Override public void setLineColor(Color col) {
		super.setLineColor(col);
		getStereotypeFig().setLineColor(null);
		getNameFig().setLineColor(col);
		body.setLineColor(col);
	}
	@Override public Color getLineColor() {
		return body.getLineColor();
	}
	@Override public void setFillColor(Color col) {
		super.setFillColor(col);
		getStereotypeFig().setFillColor(null);
		getNameFig().setFillColor(col);
		body.setFillColor(col);
	}
	@Override public Color getFillColor() {
		return body.getFillColor();
	}
	@Override public void setFilled(boolean f) {
		getStereotypeFig().setFilled(false);
		getNameFig().setFilled(f);
		body.setFilled(f);
	}
	@Override public boolean isFilled() {
		return body.isFilled();
	}
	@Override public void setLineWidth(int w) {
		getNameFig().setLineWidth(w);
		body.setLineWidth(w);
	}
	@Override public int getLineWidth() {
		return body.getLineWidth();
	}
	@Override protected void updateStereotypeText() {
		Object modelElement = getOwner();
		if (modelElement == null) {
			return;
		}
		Rectangle rect = getBounds();
		if (Model.getFacade().getStereotypes(modelElement).isEmpty()) {
			if (getStereotypeFig().isVisible()) {
				getNameFig().setTopMargin(0);
				getStereotypeFig().setVisible(false);
			}
		}else {
			super.updateStereotypeText();
			if (!isStereotypeVisible()) {
				getNameFig().setTopMargin(0);
				getStereotypeFig().setVisible(false);
			}else if (!getStereotypeFig().isVisible()) {
				getNameFig().setTopMargin(getStereotypeFig().getMinimumSize().height);
				getStereotypeFig().setVisible(true);
			}
		}
		forceRepaintShadow();
		setBounds(rect.x,rect.y,rect.width,rect.height);
	}
	@Override public String classNameAndBounds() {
		return super.classNameAndBounds() + "stereotypeVisible=" + isStereotypeVisible() + ";" + "visibilityVisible=" + isVisibilityVisible();
	}
	@Override public boolean getUseTrapRect() {
		return true;
	}
	@Override public Dimension getMinimumSize() {
		Dimension aSize = new Dimension(getNameFig().getMinimumSize());
		aSize.height = Math.max(aSize.height,MIN_HEIGHT);
		aSize.width = Math.max(aSize.width,MIN_WIDTH);
		if (isStereotypeVisible()) {
			Dimension st = getStereotypeFig().getMinimumSize();
			aSize.width = Math.max(aSize.width,st.width);
		}
		aSize.width += indentX + 1;
		aSize.height += 30;
		return aSize;
	}
	@Override protected void setStandardBounds(int xa,int ya,int w,int h) {
		Rectangle oldBounds = getBounds();
		Dimension minimumSize = getMinimumSize();
		int newW = Math.max(w,minimumSize.width);
		int newH = Math.max(h,minimumSize.height);
		Dimension nameMin = getNameFig().getMinimumSize();
		int minNameHeight = Math.max(nameMin.height,MIN_HEIGHT);
		int currentY = ya;
		int tabWidth = newW - indentX;
		if (isStereotypeVisible()) {
			Dimension stereoMin = getStereotypeFig().getMinimumSize();
			getNameFig().setTopMargin(stereoMin.height);
			getNameFig().setBounds(xa,currentY,tabWidth + 1,minNameHeight);
			getStereotypeFig().setBounds(xa,ya,tabWidth,stereoMin.height + 1);
			if (tabWidth < stereoMin.width + 1) {
				tabWidth = stereoMin.width + 2;
			}
		}else {
			getNameFig().setBounds(xa,currentY,tabWidth + 1,minNameHeight);
		}
		currentY += minNameHeight - 1;
		body.setBounds(xa,currentY,newW,newH + ya - currentY);
		tabHeight = currentY - ya;
		getBigPort().setBounds(xa,ya,newW,newH);
		calcBounds();
		updateEdges();
		firePropChange("bounds",oldBounds,getBounds());
	}
	@Override public Vector getPopUpActions(MouseEvent me) {
		Vector popUpActions = super.getPopUpActions(me);
		popUpActions.add(popUpActions.size() - getPopupAddOffset(),buildModifierPopUp(ABSTRACT|LEAF|ROOT));
		popUpActions.add(popUpActions.size() - getPopupAddOffset(),buildVisibilityPopUp());
		return popUpActions;
	}
	@Override protected ArgoJMenu buildShowPopUp() {
		ArgoJMenu showMenu = super.buildShowPopUp();
		Editor ce = Globals.curEditor();
		List<Fig>figs = ce.getSelectionManager().getFigs();
		boolean sOn = false;
		boolean sOff = false;
		boolean vOn = false;
		boolean vOff = false;
		for (Fig f:figs) {
			if (f instanceof StereotypeContainer) {
				boolean v = ((StereotypeContainer) f).isStereotypeVisible();
				if (v) {
					sOn = true;
				}else {
					sOff = true;
				}
				v = ((VisibilityContainer) f).isVisibilityVisible();
				if (v) {
					vOn = true;
				}else {
					vOff = true;
				}
			}
		}
		if (sOn) {
			showMenu.add(new HideStereotypeAction());
		}
		if (sOff) {
			showMenu.add(new ShowStereotypeAction());
		}
		if (vOn) {
			showMenu.add(new HideVisibilityAction());
		}
		if (vOff) {
			showMenu.add(new ShowVisibilityAction());
		}
		return showMenu;
	}
	private void doStereotype(boolean value) {
		Editor ce = Globals.curEditor();
		List<Fig>figs = ce.getSelectionManager().getFigs();
		for (Fig f:figs) {
			if (f instanceof StereotypeContainer) {
				((StereotypeContainer) f).setStereotypeVisible(value);
			}
			if (f instanceof FigNodeModelElement) {
				((FigNodeModelElement) f).forceRepaintShadow();
				((ArgoFig) f).renderingChanged();
			}
			f.damage();
		}
	}
	private void doVisibility(boolean value) {
		Editor ce = Globals.curEditor();
		List<Fig>figs = ce.getSelectionManager().getFigs();
		for (Fig f:figs) {
			if (f instanceof VisibilityContainer) {
				((VisibilityContainer) f).setVisibilityVisible(value);
			}
			f.damage();
		}
	}
	class FigPackageFigText extends ArgoFigText {
		@SuppressWarnings("deprecation")@Deprecated public FigPackageFigText(int xa,int ya,int w,int h) {
			super(xa,ya,w,h);
		}
		public FigPackageFigText(Object owner,Rectangle bounds,DiagramSettings settings) {
			super(owner,bounds,settings,false);
		}
		@Override public void mouseClicked(MouseEvent me) {
			String lsDefaultName = "main";
			if (me.getClickCount() >= 2) {
				Object lPkg = FigPackage.this.getOwner();
				if (lPkg != null) {
					Object lNS = lPkg;
					Project lP = getProject();
					List<ArgoDiagram>diags = lP.getDiagramList();
					ArgoDiagram lFirst = null;
					for (ArgoDiagram lDiagram:diags) {
						Object lDiagramNS = lDiagram.getNamespace();
						if ((lNS == null&&lDiagramNS == null)||(lNS.equals(lDiagramNS))) {
							if (lFirst == null) {
								lFirst = lDiagram;
							}
							if (lDiagram.getName() != null&&lDiagram.getName().startsWith(lsDefaultName)) {
								me.consume();
								super.mouseClicked(me);
								TargetManager.getInstance().setTarget(lDiagram);
								return;
							}
						}
					}
					if (lFirst != null) {
						me.consume();
						super.mouseClicked(me);
						TargetManager.getInstance().setTarget(lFirst);
						return;
					}
					me.consume();
					super.mouseClicked(me);
					try {
						createClassDiagram(lNS,lsDefaultName,lP);
					}catch (Exception ex) {
						LOG.error(ex);
					}
					return;
				}
			}
			super.mouseClicked(me);
		}
		private static final long serialVersionUID = -1355316218065323634l;
	}
	private void createClassDiagram(Object namespace,String defaultName,Project project)throws PropertyVetoException {
		String namespaceDescr;
		if (namespace != null&&Model.getFacade().getName(namespace) != null) {
			namespaceDescr = Model.getFacade().getName(namespace);
		}else {
			namespaceDescr = Translator.localize("misc.name.anon");
		}
		String dialogText = "Add new class diagram to " + namespaceDescr + "?";
		int option = JOptionPane.showConfirmDialog(null,dialogText,"Add new class diagram?",JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			ArgoDiagram classDiagram = DiagramFactory.getInstance().createDiagram(DiagramType.Class,namespace,null);
			String diagramName = defaultName + "_" + classDiagram.getName();
			project.addMember(classDiagram);
			TargetManager.getInstance().setTarget(classDiagram);
			classDiagram.setName(diagramName);
			ExplorerEventAdaptor.getInstance().structureChanged();
		}
	}
	public boolean isStereotypeVisible() {
		return stereotypeVisible;
	}
	public void setStereotypeVisible(boolean isVisible) {
		stereotypeVisible = isVisible;
		renderingChanged();
		damage();
	}
	public boolean isVisibilityVisible() {
		return getNotationSettings().isShowVisibilities();
	}
	public void setVisibilityVisible(boolean isVisible) {
		getNotationSettings().setShowVisibilities(isVisible);
		renderingChanged();
		damage();
	}
	@Override protected void textEditStarted(FigText ft) {
		if (ft == getNameFig()) {
			showHelp("parsing.help.fig-package");
		}
	}
	@Override public Point getClosestPoint(Point anotherPt) {
		Rectangle r = getBounds();
		int[]xs =  {r.x,r.x + r.width - indentX,r.x + r.width - indentX,r.x + r.width,r.x + r.width,r.x,r.x};
		int[]ys =  {r.y,r.y,r.y + tabHeight,r.y + tabHeight,r.y + r.height,r.y + r.height,r.y};
		Point p = Geometry.ptClosestTo(xs,ys,7,anotherPt);
		return p;
	}
	@Override protected void modelChanged(PropertyChangeEvent mee) {
		if (mee instanceof RemoveAssociationEvent&&"ownedElement".equals(mee.getPropertyName())&&mee.getSource() == getOwner()) {
			if (LOG.isInfoEnabled()&&mee.getNewValue() == null) {
				LOG.info(Model.getFacade().getName(mee.getOldValue()) + " has been removed from the namespace of " + Model.getFacade().getName(getOwner()) + " by notice of " + mee.toString());
			}
			LayerPerspective layer = (LayerPerspective) getLayer();
			Fig f = layer.presentationFor(mee.getOldValue());
			if (f != null&&f.getEnclosingFig() == this) {
				removeEnclosedFig(f);
				f.setEnclosingFig(null);
			}
		}
		super.modelChanged(mee);
	}
	private static final long serialVersionUID = 3617092272529451041l;
	private class HideStereotypeAction extends UndoableAction {
	private static final String ACTION_KEY = "menu.popup.show.hide-stereotype";
	HideStereotypeAction() {
			super(Translator.localize(ACTION_KEY),ResourceLoaderWrapper.lookupIcon(ACTION_KEY));
		}
	@Override public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		doStereotype(false);
	}
	private static final long serialVersionUID = 1999499813643610674l;
}
	private class ShowStereotypeAction extends UndoableAction {
	private static final String ACTION_KEY = "menu.popup.show.show-stereotype";
	ShowStereotypeAction() {
			super(Translator.localize(ACTION_KEY),ResourceLoaderWrapper.lookupIcon(ACTION_KEY));
		}
	@Override public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		doStereotype(true);
	}
	private static final long serialVersionUID = -4327161642276705610l;
}
	private class HideVisibilityAction extends UndoableAction {
	private static final String ACTION_KEY = "menu.popup.show.hide-visibility";
	HideVisibilityAction() {
			super(Translator.localize(ACTION_KEY),ResourceLoaderWrapper.lookupIcon(ACTION_KEY));
		}
	@Override public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		doVisibility(false);
	}
	private static final long serialVersionUID = 8574809709777267866l;
}
	private class ShowVisibilityAction extends UndoableAction {
	private static final String ACTION_KEY = "menu.popup.show.show-visibility";
	ShowVisibilityAction() {
			super(Translator.localize(ACTION_KEY),ResourceLoaderWrapper.lookupIcon(ACTION_KEY));
		}
	@Override public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);
		doVisibility(true);
	}
	private static final long serialVersionUID = 7722093402948975834l;
}
}

class PackagePortFigRect extends FigRect {
	private int indentX;
	private int tabHeight;
	public PackagePortFigRect(int x,int y,int w,int h,int ix,int th) {
		super(x,y,w,h,null,null);
		this.indentX = ix;
		tabHeight = th;
	}
	@Override public Point getClosestPoint(Point anotherPt) {
		Rectangle r = getBounds();
		int[]xs =  {r.x,r.x + r.width - indentX,r.x + r.width - indentX,r.x + r.width,r.x + r.width,r.x,r.x};
		int[]ys =  {r.y,r.y,r.y + tabHeight,r.y + tabHeight,r.y + r.height,r.y + r.height,r.y};
		Point p = Geometry.ptClosestTo(xs,ys,7,anotherPt);
		return p;
	}
	private static final long serialVersionUID = -7083102131363598065l;
}



