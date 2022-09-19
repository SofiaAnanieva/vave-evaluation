package org.argouml.uml.diagram.ui;

import org.argouml.model.Model;
import org.argouml.uml.CommentEdge;
import org.tigris.gef.presentation.Fig;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.ModeCreateGraphEdge;


public final class ModeCreateCommentEdge extends ModeCreateGraphEdge {
	@Override protected final boolean isConnectionValid(Fig source,Fig dest) {
		if (dest instanceof FigNodeModelElement) {
			Object srcOwner = source.getOwner();
			Object dstOwner = dest.getOwner();
			if (!Model.getFacade().isAModelElement(srcOwner)||!Model.getFacade().isAModelElement(dstOwner)) {
				return false;
			}
			if (Model.getModelManagementHelper().isReadOnly(srcOwner)||Model.getModelManagementHelper().isReadOnly(dstOwner)) {
				return false;
			}
			return Model.getFacade().isAComment(srcOwner)||Model.getFacade().isAComment(dstOwner);
		}else {
			return true;
		}
	}
	protected final Object getMetaType() {
		return CommentEdge.class;
	}
}



