package org.argouml.uml.diagram.ui;

import java.awt.Font;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import org.argouml.application.events.ArgoDiagramAppearanceEvent;
import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.FigText;
import org.argouml.uml.diagram.ui.ArgoFig;


public class ArgoFigText extends FigText implements NotificationEmitter,ArgoFig {
	private NotificationBroadcasterSupport notifier = new NotificationBroadcasterSupport();
	private DiagramSettings settings;
	@Deprecated public ArgoFigText(int x,int y,int w,int h) {
		super(x,y,w,h);
		setFontFamily("dialog");
	}
	@Deprecated public ArgoFigText(int x,int y,int w,int h,boolean expandOnly) {
		super(x,y,w,h,expandOnly);
		setFontFamily("dialog");
	}
	public ArgoFigText(Object owner,Rectangle bounds,DiagramSettings renderSettings,boolean expandOnly) {
		this(bounds.x,bounds.y,bounds.width,bounds.height,expandOnly);
		settings = renderSettings;
		super.setFontFamily(settings.getFontName());
		super.setFontSize(settings.getFontSize());
		super.setFillColor(FILL_COLOR);
		super.setTextFillColor(FILL_COLOR);
		super.setTextColor(TEXT_COLOR);
		if (owner != null) {
			super.setOwner(owner);
			Model.getPump().addModelEventListener(this,owner,"remove");
		}
	}
	@Override public void deleteFromModel() {
		super.deleteFromModel();
		firePropChange("remove",null,null);
		notifier.sendNotification(new Notification("remove",this,0));
	}
	public void removeNotificationListener(NotificationListener listener,NotificationFilter filter,Object handback)throws ListenerNotFoundException {
		notifier.removeNotificationListener(listener,filter,handback);
	}
	public void addNotificationListener(NotificationListener listener,NotificationFilter filter,Object handback)throws IllegalArgumentException {
		notifier.addNotificationListener(listener,filter,handback);
	}
	public MBeanNotificationInfo[]getNotificationInfo() {
		return notifier.getNotificationInfo();
	}
	public void removeNotificationListener(NotificationListener listener)throws ListenerNotFoundException {
		notifier.removeNotificationListener(listener);
	}
	@SuppressWarnings("deprecation")@Deprecated public void setProject(Project project) {
		throw new UnsupportedOperationException();
	}
	@SuppressWarnings("deprecation")@Deprecated public Project getProject() {
		return ArgoFigUtil.getProject(this);
	}
	public void renderingChanged() {
		updateFont();
		setBounds(getBounds());
		damage();
	}
	@Deprecated public void diagramFontChanged(@SuppressWarnings("unused")ArgoDiagramAppearanceEvent e) {
		renderingChanged();
	}
	protected void updateFont() {
		setFont(getSettings().getFont(getFigFontStyle()));
	}
	protected int getFigFontStyle() {
		return Font.PLAIN;
	}
	@SuppressWarnings("deprecation")@Override@Deprecated public void setOwner(Object own) {
		super.setOwner(own);
	}
	@Deprecated protected void updateListeners(Object oldOwner,Object newOwner) {
		if (oldOwner == newOwner) {
			return;
		}
		if (oldOwner != null) {
			Model.getPump().removeModelEventListener(this,oldOwner);
		}
		if (newOwner != null) {
			Model.getPump().addModelEventListener(this,newOwner,"remove");
		}
	}
	@Override public void propertyChange(PropertyChangeEvent pce) {
		super.propertyChange(pce);
		if ("remove".equals(pce.getPropertyName())&&(pce.getSource() == getOwner())) {
			deleteFromModel();
		}
	}
	public DiagramSettings getSettings() {
		if (settings == null) {
			Project p = getProject();
			if (p != null) {
				return p.getProjectSettings().getDefaultDiagramSettings();
			}
		}
		return settings;
	}
	public void setSettings(DiagramSettings renderSettings) {
		settings = renderSettings;
		renderingChanged();
	}
}



