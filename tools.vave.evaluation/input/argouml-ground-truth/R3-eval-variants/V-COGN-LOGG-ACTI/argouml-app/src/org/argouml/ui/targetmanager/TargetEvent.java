package org.argouml.ui.targetmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;


public class TargetEvent extends EventObject {
	public static final String TARGET_SET = "set";
	public static final String TARGET_ADDED = "added";
	public static final String TARGET_REMOVED = "removed";
	private String theEventName;
	private Object[]theOldTargets;
	private Object[]theNewTargets;
	public TargetEvent(Object source,String tEName,Object[]oldTargets,Object[]newTargets) {
		super(source);
		theEventName = tEName;
		theOldTargets = oldTargets;
		theNewTargets = newTargets;
	}
	public String getName() {
		return theEventName;
	}
	public Object[]getOldTargets() {
		return theOldTargets == null?new Object[] {}:theOldTargets;
	}
	public Object[]getNewTargets() {
		return theNewTargets == null?new Object[] {}:theNewTargets;
	}
	public Object getNewTarget() {
		return theNewTargets == null||theNewTargets. < 1?null:theNewTargets[0];
	}
	public Collection getRemovedTargetCollection() {
		List removedTargets = new ArrayList();
		List oldTargets = Arrays.asList(theOldTargets);
		List newTargets = Arrays.asList(theNewTargets);
		for (Object o:oldTargets) {
			if (!newTargets.contains(o)) {
				removedTargets.add(o);
			}
		}
		return removedTargets;
	}
	public Object[]getRemovedTargets() {
		return getRemovedTargetCollection().toArray();
	}
	public Collection getAddedTargetCollection() {
		List addedTargets = new ArrayList();
		List oldTargets = Arrays.asList(theOldTargets);
		List newTargets = Arrays.asList(theNewTargets);
		for (Object o:newTargets) {
			if (!oldTargets.contains(o)) {
				addedTargets.add(o);
			}
		}
		return addedTargets;
	}
	public Object[]getAddedTargets() {
		return getAddedTargetCollection().toArray();
	}
	private static final long serialVersionUID = -307886693486269426l;
}



