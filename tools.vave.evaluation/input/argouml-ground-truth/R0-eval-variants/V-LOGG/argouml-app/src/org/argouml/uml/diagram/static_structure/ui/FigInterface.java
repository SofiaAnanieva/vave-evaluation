package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;


public class FigInterface extends FigClassifierBox {
	private static final Logger LOG = Logger.getLogger(FigInterface.class);
	@SuppressWarnings("deprecation")@Deprecated public FigInterface() {
		initialize();
	}
	private void initialize() {
		getStereotypeFig().setKeyword("interface");
		enableSizeChecking(false);
		setSuppressCalcBounds(true);
		Dimension size = new Dimension(0,0);
		addFig(getBigPort());
		addFig(getStereotypeFig());
		addChildDimensions(size,getStereotypeFig());
		addFig(getNameFig());
		addChildDimensions(size,getNameFig());
		addFig(getOperationsFig());
		addChildDimensions(size,getOperationsFig());
		addFig(borderFig);
		setSuppressCalcBounds(false);
		enableSizeChecking(true);
		setBounds(X0,Y0,size.width,size.height);
	}
	@SuppressWarnings("deprecation")@Deprecated public FigInterface(@SuppressWarnings("unused")GraphModel gm,Object node) {
		this();
		setOwner(node);
		enableSizeChecking(true);
	}
	public FigInterface(Object owner,Rectangle bounds,DiagramSettings settings) {
		super(owner,bounds,settings);
		initialize();
	}
	@Override public Selection makeSelection() {
		return new SelectionInterface(this);
	}
	@Override public Dimension getMinimumSize() {
		Dimension aSize = getNameFig().getMinimumSize();
		aSize.height += NAME_V_PADDING * 2;
		aSize.height = Math.max(NAME_FIG_HEIGHT,aSize.height);
		aSize = addChildDimensions(aSize,getStereotypeFig());
		aSize = addChildDimensions(aSize,getOperationsFig());
		aSize.width = Math.max(WIDTH,aSize.width);
		return aSize;
	}
	@Override public void translate(int dx,int dy) {
		super.translate(dx,dy);
		Editor ce = Globals.curEditor();
		if (ce != null) {
			Selection sel = ce.getSelectionManager().findSelectionFor(this);
			if (sel instanceof SelectionClass) {
				((SelectionClass) sel).hideButtons();
			}
		}
	}
	@Override public void setEnclosingFig(Fig encloser) {
		Fig oldEncloser = getEnclosingFig();
		if (encloser == null||(encloser != null&&!Model.getFacade().isAInstance(encloser.getOwner()))) {
			super.setEnclosingFig(encloser);
		}
		if (!(Model.getFacade().isAModelElement(getOwner()))) {
			return;
		}
		if (!isVisible()) {
			return;
		}
		Object me = getOwner();
		Object m = null;
		try {
			if (encloser != null&&oldEncloser != encloser&&Model.getFacade().isAPackage(encloser.getOwner())) {
				Model.getCoreHelper().setNamespace(me,encloser.getOwner());
			}
			if (Model.getFacade().getNamespace(me) == null&&(TargetManager.getInstance().getTarget()instanceof ArgoDiagram)) {
				m = ((ArgoDiagram) TargetManager.getInstance().getTarget()).getNamespace();
				Model.getCoreHelper().setNamespace(me,m);
			}
		}catch (Exception e) {
			LOG.error("could not set package due to:" + e + "\' at " + encloser,e);
		}
		if (encloser != null&&(Model.getFacade().isAComponent(encloser.getOwner()))) {
			moveIntoComponent(encloser);
			super.setEnclosingFig(encloser);
		}
	}
	@Override public String classNameAndBounds() {
		return super.classNameAndBounds() + "operationsVisible=" + isOperationsVisible();
	}
	@Override protected void setStandardBounds(final int x,final int y,final int w,final int h) {
		Rectangle oldBounds = getBounds();
		getBigPort().setBounds(x,y,w,h);
		borderFig.setBounds(x,y,w,h);
		int currentHeight = 0;
		if (getStereotypeFig().isVisible()) {
			int stereotypeHeight = getStereotypeFig().getMinimumSize().height;
			getStereotypeFig().setBounds(x,y,w,stereotypeHeight);
			currentHeight = stereotypeHeight;
		}
		int nameHeight = getNameFig().getMinimumSize().height;
		getNameFig().setBounds(x,y + currentHeight,w,nameHeight);
		currentHeight += nameHeight;
		if (getOperationsFig().isVisible()) {
			int operationsY = y + currentHeight;
			int operationsHeight = (h + y) - operationsY - 1;
			getOperationsFig().setBounds(x,operationsY,w,operationsHeight);
		}
		calcBounds();
		updateEdges();
		firePropChange("bounds",oldBounds,getBounds());
	}
}



