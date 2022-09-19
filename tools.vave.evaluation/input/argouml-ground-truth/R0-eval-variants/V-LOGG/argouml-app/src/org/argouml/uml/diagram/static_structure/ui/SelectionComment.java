package org.argouml.uml.diagram.static_structure.ui;

import javax.swing.Icon;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.ui.SelectionNodeClarifiers2;
import org.tigris.gef.presentation.Fig;


public class SelectionComment extends SelectionNodeClarifiers2 {
	private static Icon linkIcon = ResourceLoaderWrapper.lookupIconResource("CommentLink");
	private static Icon icons[] =  {linkIcon,linkIcon,linkIcon,linkIcon,null};
	private static String instructions[] =  {"Link this comment","Link this comment","Link this comment","Link this comment",null,"Move object(s)"};
	public SelectionComment(Fig f) {
		super(f);
	}
	@Override protected Icon[]getIcons() {
		if (Model.getModelManagementHelper().isReadOnly(getContent().getOwner())) {
			return null;
		}
		return icons;
	}
	@Override protected String getInstructions(int index) {
		return instructions[index - BASE];
	}
	@Override protected Object getNewEdgeType(int index) {
		return CommentEdge.class;
	}
	@Override protected Object getNewNodeType(int index) {
		return Model.getMetaTypes().getComment();
	}
	@Override protected Object getNewNode(int index) {
		return Model.getCoreFactory().createComment();
	}
}



