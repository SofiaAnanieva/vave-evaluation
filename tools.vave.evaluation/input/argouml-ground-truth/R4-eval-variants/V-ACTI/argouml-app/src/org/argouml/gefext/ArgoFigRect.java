package org.argouml.gefext;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import org.tigris.gef.presentation.FigRect;


public class ArgoFigRect extends FigRect implements NotificationEmitter {
	private NotificationBroadcasterSupport notifier = new NotificationBroadcasterSupport();
	public ArgoFigRect(int x,int y,int w,int h) {
		super(x,y,w,h);
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
}



