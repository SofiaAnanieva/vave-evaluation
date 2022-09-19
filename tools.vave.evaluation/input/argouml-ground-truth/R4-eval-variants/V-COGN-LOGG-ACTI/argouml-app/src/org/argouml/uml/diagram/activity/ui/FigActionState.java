package org.argouml.uml.diagram.activity.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.Iterator;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.state.ui.FigStateVertex;
import org.argouml.uml.diagram.ui.FigMultiLineTextWithBold;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigText;


public class FigActionState extends FigStateVertex {
	private static final int HEIGHT = 25;
	private static final int STATE_WIDTH = 90;
	private static final int PADDING = 8;
	private FigRRect cover;
	private NotationProvider notationProvider;
	@SuppressWarnings("deprecation")@Deprecated public FigActionState() {
		initializeActionState();
	}
	@SuppressWarnings("deprecation")@Deprecated public FigActionState(@SuppressWarnings("unused")GraphModel gm,Object node) {
		setOwner(node);
		initializeActionState();
	}
	public FigActionState(Object owner,Rectangle bounds,DiagramSettings settings) {
		super(owner,bounds,settings);
		initializeActionState();
	}
	private void initializeActionState() {
		setBigPort(new FigRRect(X0 + 1,Y0 + 1,STATE_WIDTH - 2,HEIGHT - 2,DEBUG_COLOR,DEBUG_COLOR));
		((FigRRect) getBigPort()).setCornerRadius(getBigPort().getHeight() / 2);
		cover = new FigRRect(X0,Y0,STATE_WIDTH,HEIGHT,LINE_COLOR,FILL_COLOR);
		cover.setCornerRadius(getHeight() / 2);
		Rectangle bounds = new Rectangle(X0 + PADDING,Y0,STATE_WIDTH - PADDING * 2,HEIGHT);
		setNameFig(new FigMultiLineTextWithBold(getOwner(),bounds,getSettings(),true));
		getNameFig().setText(placeString());
		getNameFig().setBotMargin(7);
		getNameFig().setTopMargin(7);
		getNameFig().setRightMargin(4);
		getNameFig().setLeftMargin(4);
		getNameFig().setJustification(FigText.JUSTIFY_CENTER);
		getBigPort().setLineWidth(0);
		addFig(getBigPort());
		addFig(cover);
		addFig(getStereotypeFig());
		addFig(getNameFig());
		Rectangle r = getBounds();
		setBounds(r.x,r.y,r.width,r.height);
	}
	@Override protected void initNotationProviders(Object own) {
		if (notationProvider != null) {
			notationProvider.cleanListener(this,own);
		}
		super.initNotationProviders(own);
		NotationName notationName = Notation.findNotation(getNotationSettings().getNotationLanguage());
		if (Model.getFacade().isAActionState(own)) {
			notationProvider = NotationProviderFactory2.getInstance().getNotationProvider(getNotationProviderType(),own,this,notationName);
		}
	}
	@Override protected int getNotationProviderType() {
		return NotationProviderFactory2.TYPE_ACTIONSTATE;
	}
	@Override public Object clone() {
		FigActionState figClone = (FigActionState) super.clone();
		Iterator it = figClone.getFigs().iterator();
		figClone.setBigPort((FigRRect) it.next());
		figClone.cover = (FigRRect) it.next();
		figClone.setNameFig((FigText) it.next());
		return figClone;
	}
	@Override public Dimension getMinimumSize() {
		Dimension stereoDim = getStereotypeFig().getMinimumSize();
		Dimension nameDim = getNameFig().getMinimumSize();
		int w = Math.max(stereoDim.width,nameDim.width) + PADDING * 2;
		int h = stereoDim.height - 2 + nameDim.height + PADDING;
		w = Math.max(w,h + 44);
		return new Dimension(w,h);
	}
	@Override protected void setStandardBounds(int x,int y,int w,int h) {
		if (getNameFig() == null) {
			return;
		}
		Rectangle oldBounds = getBounds();
		Dimension stereoDim = getStereotypeFig().getMinimumSize();
		Dimension nameDim = getNameFig().getMinimumSize();
		getNameFig().setBounds(x + PADDING,y + stereoDim.height,w - PADDING * 2,nameDim.height);
		getStereotypeFig().setBounds(x + PADDING,y,w - PADDING * 2,stereoDim.height);
		getBigPort().setBounds(x + 1,y + 1,w - 2,h - 2);
		cover.setBounds(x,y,w,h);
		((FigRRect) getBigPort()).setCornerRadius(h);
		cover.setCornerRadius(h);
		calcBounds();
		updateEdges();
		firePropChange("bounds",oldBounds,getBounds());
	}
	@Override public void setLineColor(Color col) {
		cover.setLineColor(col);
	}
	@Override public Color getLineColor() {
		return cover.getLineColor();
	}
	@Override public void setFillColor(Color col) {
		cover.setFillColor(col);
	}
	@Override public Color getFillColor() {
		return cover.getFillColor();
	}
	@Override public void setFilled(boolean f) {
		cover.setFilled(f);
	}
	@Override public boolean isFilled() {
		return cover.isFilled();
	}
	@Override public void setLineWidth(int w) {
		cover.setLineWidth(w);
	}
	@Override public int getLineWidth() {
		return cover.getLineWidth();
	}
	@Override protected void modelChanged(PropertyChangeEvent mee) {
		super.modelChanged(mee);
		if (mee instanceof AddAssociationEvent||mee instanceof AttributeChangeEvent) {
			renderingChanged();
			notationProvider.updateListener(this,getOwner(),mee);
			damage();
		}
	}
	@Override public void removeFromDiagramImpl() {
		if (notationProvider != null) {
			notationProvider.cleanListener(this,getOwner());
		}
		super.removeFromDiagramImpl();
	}
	@Override protected void updateNameText() {
		if (notationProvider != null) {
			getNameFig().setText(notationProvider.toString(getOwner(),getNotationSettings()));
		}
	}
	@Override protected void updateStereotypeText() {
		getStereotypeFig().setOwner(getOwner());
	}
	@Override protected void textEdited(FigText ft)throws PropertyVetoException {
		notationProvider.parse(getOwner(),ft.getText());
		ft.setText(notationProvider.toString(getOwner(),getNotationSettings()));
	}
	@Override protected void textEditStarted(FigText ft) {
		if (ft == getNameFig()) {
			showHelp(notationProvider.getParsingHelp());
		}
	}
	private static final long serialVersionUID = -3526461404860044420l;
}



