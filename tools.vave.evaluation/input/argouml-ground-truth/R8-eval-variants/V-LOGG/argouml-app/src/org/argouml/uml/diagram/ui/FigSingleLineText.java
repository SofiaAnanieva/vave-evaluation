package org.argouml.uml.diagram.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.FigText;
import org.argouml.uml.diagram.ui.ArgoFigText;


public class FigSingleLineText extends ArgoFigText {
	private static final Logger LOG = Logger.getLogger(FigSingleLineText.class);
	private String[]properties;
	@SuppressWarnings("deprecation")@Deprecated public FigSingleLineText(int x,int y,int w,int h,boolean expandOnly) {
		super(x,y,w,h,expandOnly);
		initialize();
	}
	private void initialize() {
		setFillColor(FILL_COLOR);
		setFilled(false);
		setTabAction(FigText.END_EDITING);
		setReturnAction(FigText.END_EDITING);
		setLineWidth(0);
		setTextColor(TEXT_COLOR);
	}
	@Deprecated public FigSingleLineText(int x,int y,int w,int h,boolean expandOnly,String property) {
		this(x,y,w,h,expandOnly,new String[] {property});
	}
	@Deprecated public FigSingleLineText(int x,int y,int w,int h,boolean expandOnly,String[]allProperties) {
		this(x,y,w,h,expandOnly);
		this.properties = allProperties;
	}
	public FigSingleLineText(Object owner,Rectangle bounds,DiagramSettings settings,boolean expandOnly) {
		this(owner,bounds,settings,expandOnly,(String[]) null);
	}
	public FigSingleLineText(Object owner,Rectangle bounds,DiagramSettings settings,boolean expandOnly,String property) {
		this(owner,bounds,settings,expandOnly,new String[] {property});
	}
	public FigSingleLineText(Rectangle bounds,DiagramSettings settings,boolean expandOnly) {
		this(null,bounds,settings,expandOnly);
	}
	public FigSingleLineText(Object owner,Rectangle bounds,DiagramSettings settings,boolean expandOnly,String[]allProperties) {
		super(owner,bounds,settings,expandOnly);
		initialize();
		this.properties = allProperties;
		addModelListener();
	}
	@Override public Dimension getMinimumSize() {
		Dimension d = new Dimension();
		Font font = getFont();
		if (font == null) {
			return d;
		}
		int maxW = 0;
		int maxH = 0;
		if (getFontMetrics() == null) {
			maxH = font.getSize();
		}else {
			maxH = getFontMetrics().getHeight();
			maxW = getFontMetrics().stringWidth(getText());
		}
		int overallH = (maxH + getTopMargin() + getBotMargin());
		int overallW = maxW + getLeftMargin() + getRightMargin();
		d.width = overallW;
		d.height = overallH;
		return d;
	}
	@Override protected boolean isStartEditingKey(KeyEvent ke) {
		if ((ke.getModifiers()&(KeyEvent.META_MASK|KeyEvent.ALT_MASK)) == 0) {
			return super.isStartEditingKey(ke);
		}else {
			return false;
		}
	}
	@SuppressWarnings("deprecation")@Deprecated@Override public void setOwner(Object owner) {
		super.setOwner(owner);
		if (owner != null&&properties != null) {
			addModelListener();
			setText();
		}
	}
	private void addModelListener() {
		if (properties != null&&getOwner() != null) {
			Model.getPump().addModelEventListener(this,getOwner(),properties);
		}
	}
	@Override public void removeFromDiagram() {
		if (getOwner() != null&&properties != null) {
			Model.getPump().removeModelEventListener(this,getOwner(),properties);
		}
		super.removeFromDiagram();
	}
	@Override public void propertyChange(PropertyChangeEvent pce) {
		if ("remove".equals(pce.getPropertyName())&&(pce.getSource() == getOwner())) {
			deleteFromModel();
		}
		if (pce instanceof UmlChangeEvent) {
			final UmlChangeEvent event = (UmlChangeEvent) pce;
			Runnable doWorkRunnable = new Runnable() {
			public void run() {
				try {
					updateLayout(event);
				}catch (InvalidElementException e) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("event = " + event.getClass().getName());
						LOG.debug("source = " + event.getSource());
						LOG.debug("old = " + event.getOldValue());
						LOG.debug("name = " + event.getPropertyName());
						LOG.debug("updateLayout method accessed " + "deleted element ",e);
					}
				}
			}
		};
			SwingUtilities.invokeLater(doWorkRunnable);
		}
	}
	protected void updateLayout(UmlChangeEvent event) {
		assert event != null;
		if (getOwner() == event.getSource()&&properties != null&&Arrays.asList(properties).contains(event.getPropertyName())&&event instanceof AttributeChangeEvent) {
			setText();
		}
	}
	protected void setText() {
	}
	public void renderingChanged() {
		super.renderingChanged();
		setText();
	}
}



