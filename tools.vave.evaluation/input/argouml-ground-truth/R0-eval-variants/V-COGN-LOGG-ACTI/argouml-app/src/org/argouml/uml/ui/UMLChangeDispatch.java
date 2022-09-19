package org.argouml.uml.ui;

import java.awt.Component;
import java.awt.Container;
import org.argouml.uml.ui.PropPanel;


public class UMLChangeDispatch implements Runnable,UMLUserInterfaceComponent {
	private int eventType;
	private Container container;
	private Object target;
	public static final int TARGET_CHANGED_ADD = -1;
	public static final int TARGET_CHANGED = 0;
	public static final int TARGET_REASSERTED = 7;
	public UMLChangeDispatch(Container uic,int et) {
		synchronized (uic) {
			container = uic;
			eventType = et;
			if (uic instanceof PropPanel) {
				target = ((PropPanel) uic).getTarget();
			}
		}
	}
	public void targetChanged() {
		eventType = 0;
	}
	public void targetReasserted() {
		eventType = 7;
	}
	public void run() {
		if (target != null) {
			synchronizedDispatch(container);
		}else {
			dispatch(container);
		}
	}
	private void dispatch(Container theAWTContainer) {
		int count = theAWTContainer.getComponentCount();
		Component component;
		for (int i = 0;i < count;i++) {
			component = theAWTContainer.getComponent(i);
			if (component instanceof Container) {
				dispatch((Container) component);
			}
			if (component instanceof UMLUserInterfaceComponent&&component.isVisible()) {
				switch (eventType) {case TARGET_CHANGED_ADD:
				case TARGET_CHANGED:
					((UMLUserInterfaceComponent) component).targetChanged();
					break;
				case TARGET_REASSERTED:
					((UMLUserInterfaceComponent) component).targetReasserted();
					break;
				}
			}
		}
	}
	private void synchronizedDispatch(Container cont) {
		if (target == null) {
			throw new IllegalStateException("Target may not be null in " + "synchronized dispatch");
		}
		synchronized (target) {
			dispatch(cont);
		}
	}
}



