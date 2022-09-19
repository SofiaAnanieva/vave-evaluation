package org.argouml.uml;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.model.UUIDManager;


public class CommentEdge extends NotificationBroadcasterSupport {
	private Object source;
	private Object dest;
	private Object uuid;
	private Object comment;
	private Object annotatedElement;
	public CommentEdge() {
		uuid = UUIDManager.getInstance().getNewUUID();
	}
	public CommentEdge(Object theSource,Object theDest) {
		if (!(Model.getFacade().isAModelElement(theSource))) {
			throw new IllegalArgumentException("The source of the CommentEdge must be a model element");
		}
		if (!(Model.getFacade().isAModelElement(theDest))) {
			throw new IllegalArgumentException("The destination of the CommentEdge " + "must be a model element");
		}
		if (Model.getFacade().isAComment(theSource)) {
			comment = theSource;
			annotatedElement = theDest;
		}else {
			comment = theDest;
			annotatedElement = theSource;
		}
		this.source = theSource;
		this.dest = theDest;
		uuid = UUIDManager.getInstance().getNewUUID();
	}
	public Object getSource() {
		return source;
	}
	public Object getDestination() {
		return dest;
	}
	public Object getUUID() {
		return uuid;
	}
	public void setDestination(Object destination) {
		if (destination == null) {
			throw new IllegalArgumentException("The destination of a comment edge cannot be null");
		}
		if (!(Model.getFacade().isAModelElement(destination))) {
			throw new IllegalArgumentException("The destination of the CommentEdge cannot be a " + destination.getClass().getName());
		}
		dest = destination;
	}
	public void setSource(Object theSource) {
		if (theSource == null) {
			throw new IllegalArgumentException("The source of a comment edge cannot be null");
		}
		if (!(Model.getFacade().isAModelElement(theSource))) {
			throw new IllegalArgumentException("The source of the CommentEdge cannot be a " + theSource.getClass().getName());
		}
		this.source = theSource;
	}
	public void delete() {
		if (Model.getFacade().isAComment(source)) {
			Model.getCoreHelper().removeAnnotatedElement(source,dest);
		}else {
			if (Model.getFacade().isAComment(dest)) {
				Model.getCoreHelper().removeAnnotatedElement(dest,source);
			}
		}
		this.sendNotification(new Notification("remove",this,0));
	}
	public String toString() {
		return Translator.localize("misc.tooltip.commentlink");
	}
	public Object getAnnotatedElement() {
		return annotatedElement;
	}
	public void setAnnotatedElement(Object theAnnotatedElement) {
		if (theAnnotatedElement == null) {
			throw new IllegalArgumentException("An annotated element must be supplied");
		}
		if (Model.getFacade().isAComment(theAnnotatedElement)) {
			throw new IllegalArgumentException("An annotated element cannot be a comment");
		}
		this.annotatedElement = theAnnotatedElement;
	}
	public Object getComment() {
		return comment;
	}
	public void setComment(Object theComment) {
		if (theComment == null) {
			throw new IllegalArgumentException("A comment must be supplied");
		}
		if (!Model.getFacade().isAComment(theComment)) {
			throw new IllegalArgumentException("A comment cannot be a " + theComment.getClass().getName());
		}
		this.comment = theComment;
	}
}



