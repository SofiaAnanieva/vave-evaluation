package org.argouml.uml.diagram.ui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;
import org.argouml.uml.diagram.ui.ArgoFigGroup;


public class FigStereotypesGroup extends ArgoFigGroup {
	private Fig bigPort;
	private static final Logger LOG = Logger.getLogger(FigStereotypesGroup.class);
	private String keyword;
	private int stereotypeCount = 0;
	private boolean hidingStereotypesWithIcon = false;
	@SuppressWarnings("deprecation")@Deprecated public FigStereotypesGroup(int x,int y,int w,int h) {
		super();
		constructFigs(x,y,w,h);
	}
	private void constructFigs(int x,int y,int w,int h) {
		bigPort = new FigRect(x,y,w,h,LINE_COLOR,FILL_COLOR);
		addFig(bigPort);
		setLineWidth(0);
		setFilled(false);
	}
	public FigStereotypesGroup(Object owner,Rectangle bounds,DiagramSettings settings) {
		super(owner,settings);
		constructFigs(bounds.x,bounds.y,bounds.width,bounds.height);
		Model.getPump().addModelEventListener(this,owner,"stereotype");
		populate();
	}
	@SuppressWarnings("deprecation")@Override@Deprecated public void setOwner(Object own) {
		if (own != null) {
			super.setOwner(own);
			Model.getPump().addModelEventListener(this,own,"stereotype");
			populate();
		}
	}
	@Override public void removeFromDiagram() {
		for (Object f:getFigs()) {
			((Fig) f).removeFromDiagram();
		}
		super.removeFromDiagram();
		Model.getPump().removeModelEventListener(this,getOwner(),"stereotype");
	}
	@Deprecated protected Fig getBigPort() {
		return bigPort;
	}
	@Override public void propertyChange(PropertyChangeEvent event) {
		if (event instanceof AddAssociationEvent) {
			AddAssociationEvent aae = (AddAssociationEvent) event;
			if (event.getPropertyName().equals("stereotype")) {
				Object stereotype = aae.getChangedValue();
				if (findFig(stereotype) == null) {
					FigText stereotypeTextFig = new FigStereotype(stereotype,getBoundsForNextStereotype(),getSettings());
					stereotypeCount++;
					addFig(stereotypeTextFig);
					reorderStereotypeFigs();
					damage();
				}
			}else {
				LOG.warn("Unexpected property " + event.getPropertyName());
			}
		}
		if (event instanceof RemoveAssociationEvent) {
			if (event.getPropertyName().equals("stereotype")) {
				RemoveAssociationEvent rae = (RemoveAssociationEvent) event;
				Object stereotype = rae.getChangedValue();
				Fig f = findFig(stereotype);
				if (f != null) {
					removeFig(f);
					f.removeFromDiagram();
					--
					stereotypeCount;
				}
			}else {
				LOG.warn("Unexpected property " + event.getPropertyName());
			}
		}
	}
	private void reorderStereotypeFigs() {
		List<Fig>allFigs = getFigs();
		List<Fig>figsWithIcon = new ArrayList<Fig>();
		List<Fig>figsWithOutIcon = new ArrayList<Fig>();
		List<Fig>others = new ArrayList<Fig>();
		for (Fig f:allFigs) {
			if (f instanceof FigStereotype) {
				FigStereotype s = (FigStereotype) f;
				if (getIconForStereotype(s) != null) {
					figsWithIcon.add(s);
				}else {
					figsWithOutIcon.add(s);
				}
			}else {
				others.add(f);
			}
		}
		List<Fig>n = new ArrayList<Fig>();
		n.addAll(others);
		n.addAll(figsWithOutIcon);
		n.addAll(figsWithIcon);
		setFigs(n);
	}
	private FigStereotype findFig(Object stereotype) {
		for (Object f:getFigs()) {
			if (f instanceof FigStereotype) {
				FigStereotype fs = (FigStereotype) f;
				if (fs.getOwner() == stereotype) {
					return fs;
				}
			}
		}
		return null;
	}
	List<FigStereotype>getStereotypeFigs() {
		final List<FigStereotype>stereotypeFigs = new ArrayList<FigStereotype>();
		for (Object f:getFigs()) {
			if (f instanceof FigStereotype) {
				FigStereotype fs = (FigStereotype) f;
				stereotypeFigs.add(fs);
			}
		}
		return stereotypeFigs;
	}
	private FigKeyword findFigKeyword() {
		for (Object f:getFigs()) {
			if (f instanceof FigKeyword) {
				return(FigKeyword) f;
			}
		}
		return null;
	}
	public void populate() {
		stereotypeCount = 0;
		Object modelElement = getOwner();
		if (modelElement == null) {
			LOG.debug("Cannot populate the stereotype compartment " + "unless the parent has an owner.");
			return;
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("Populating stereotypes compartment for " + Model.getFacade().getName(modelElement));
		}
		Collection<Fig>removeCollection = new ArrayList<Fig>(getFigs());
		if (keyword != null) {
			FigKeyword keywordFig = findFigKeyword();
			if (keywordFig == null) {
				keywordFig = new FigKeyword(keyword,getBoundsForNextStereotype(),getSettings());
				addFig(keywordFig);
			}else {
				removeCollection.remove(keywordFig);
			}
			++
			stereotypeCount;
		}
		for (Object stereo:Model.getFacade().getStereotypes(modelElement)) {
			FigStereotype stereotypeTextFig = findFig(stereo);
			if (stereotypeTextFig == null) {
				stereotypeTextFig = new FigStereotype(stereo,getBoundsForNextStereotype(),getSettings());
				addFig(stereotypeTextFig);
			}else {
				removeCollection.remove(stereotypeTextFig);
			}
			++
			stereotypeCount;
		}
		for (Fig f:removeCollection) {
			if (f instanceof FigStereotype||f instanceof FigKeyword) {
				removeFig(f);
			}
		}
		reorderStereotypeFigs();
		updateHiddenStereotypes();
	}
	public int getStereotypeCount() {
		return stereotypeCount;
	}
	private Rectangle getBoundsForNextStereotype() {
		return new Rectangle(bigPort.getX() + 1,bigPort.getY() + 1 + (stereotypeCount * ROWHEIGHT),0,ROWHEIGHT - 2);
	}
	private void updateHiddenStereotypes() {
		List<Fig>figs = getFigs();
		for (Fig f:figs) {
			if (f instanceof FigStereotype) {
				FigStereotype fs = (FigStereotype) f;
				fs.setVisible(getIconForStereotype(fs) == null||!isHidingStereotypesWithIcon());
			}
		}
	}
	private Image getIconForStereotype(FigStereotype fs) {
		Project project = getProject();
		if (project == null) {
			LOG.warn("getProject() returned null");
			return null;
		}
		Object owner = fs.getOwner();
		if (owner == null) {
			return null;
		}else {
			return project.getProfileConfiguration().getFigNodeStrategy().getIconForStereotype(owner);
		}
	}
	@Override protected void setBoundsImpl(int x,int y,int w,int h) {
		Rectangle oldBounds = getBounds();
		int yy = y;
		for (Fig fig:(Collection<Fig>) getFigs()) {
			if (fig != bigPort) {
				fig.setBounds(x + 1,yy + 1,w - 2,fig.getMinimumSize().height);
				yy += fig.getMinimumSize().height;
			}
		}
		bigPort.setBounds(x,y,w,h);
		calcBounds();
		firePropChange("bounds",oldBounds,getBounds());
	}
	public void setKeyword(String word) {
		keyword = word;
		populate();
	}
	public boolean isHidingStereotypesWithIcon() {
		return hidingStereotypesWithIcon;
	}
	public void setHidingStereotypesWithIcon(boolean hideStereotypesWithIcon) {
		this.hidingStereotypesWithIcon = hideStereotypesWithIcon;
		updateHiddenStereotypes();
	}
	@Override public Dimension getMinimumSize() {
		Dimension dim = null;
		Object modelElement = getOwner();
		if (modelElement != null) {
			List<FigStereotype>stereos = getStereotypeFigs();
			if (stereos.size() > 0||keyword != null) {
				int minWidth = 0;
				int minHeight = 0;
				for (Fig fig:(Collection<Fig>) getFigs()) {
					if (fig.isVisible()&&fig != bigPort) {
						int fw = fig.getMinimumSize().width;
						if (fw > minWidth) {
							minWidth = fw;
						}
						minHeight += fig.getMinimumSize().height;
					}
				}
				minHeight += 2;
				dim = new Dimension(minWidth,minHeight);
			}
		}
		if (dim == null) {
			dim = new Dimension(0,0);
		}
		return dim;
	}
}



