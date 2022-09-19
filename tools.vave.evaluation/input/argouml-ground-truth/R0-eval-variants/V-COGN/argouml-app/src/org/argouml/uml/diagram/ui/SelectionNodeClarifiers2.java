package org.argouml.uml.diagram.ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.Icon;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Mode;
import org.tigris.gef.base.ModeCreateEdgeAndNode;
import org.tigris.gef.base.ModeManager;
import org.tigris.gef.base.ModeModify;
import org.tigris.gef.base.ModePlace;
import org.tigris.gef.base.SelectionButtons;
import org.tigris.gef.base.SelectionManager;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.Handle;
import org.argouml.uml.diagram.ui.Clarifiable;


public abstract class SelectionNodeClarifiers2 extends SelectionButtons {
	protected static final int BASE = 10;
	protected static final int TOP = 10;
	protected static final int BOTTOM = 11;
	protected static final int LEFT = 12;
	protected static final int RIGHT = 13;
	protected static final int LOWER_LEFT = 14;
	private static final int OFFSET = 2;
	private int button;
	public SelectionNodeClarifiers2(Fig f) {
		super(f);
	}
	@Override public void paint(Graphics g) {
		final Mode topMode = Globals.curEditor().getModeManager().top();
		if (!(topMode instanceof ModePlace)) {
			((Clarifiable) getContent()).paintClarifiers(g);
		}
		super.paint(g);
	}
	public final void paintButtons(Graphics g) {
		final Mode topMode = Globals.curEditor().getModeManager().top();
		if (!(topMode instanceof ModePlace)) {
			Icon[]icons = getIcons();
			if (icons == null) {
				return;
			}
			int cx = getContent().getX();
			int cy = getContent().getY();
			int cw = getContent().getWidth();
			int ch = getContent().getHeight();
			if (icons[0] != null) {
				paintButtonAbove(icons[0],g,cx + cw / 2,cy - OFFSET,TOP);
			}
			if (icons[1] != null) {
				paintButtonBelow(icons[1],g,cx + cw / 2,cy + ch + OFFSET,BOTTOM);
			}
			if (icons[2] != null) {
				paintButtonLeft(icons[2],g,cx - OFFSET,cy + ch / 2,LEFT);
			}
			if (icons[3] != null) {
				paintButtonRight(icons[3],g,cx + cw + OFFSET,cy + ch / 2,RIGHT);
			}
			if (icons[4] != null) {
				paintButtonLeft(icons[4],g,cx - OFFSET,cy + ch,LOWER_LEFT);
			}
		}
	}
	protected Object getNewNode(int arg0) {
		return null;
	}
	public void hitHandle(Rectangle cursor,Handle h) {
		super.hitHandle(cursor,h);
		if (h.index != -1) {
			return;
		}
		if (!isPaintButtons()) {
			return;
		}
		Icon[]icons = getIcons();
		if (icons == null) {
			return;
		}
		Editor ce = Globals.curEditor();
		SelectionManager sm = ce.getSelectionManager();
		if (sm.size() != 1) {
			return;
		}
		ModeManager mm = ce.getModeManager();
		if (mm.includes(ModeModify.class)&&getPressedButton() == -1) {
			return;
		}
		int cx = getContent().getX();
		int cy = getContent().getY();
		int cw = getContent().getWidth();
		int ch = getContent().getHeight();
		if (icons[0] != null&&hitAbove(cx + cw / 2,cy,icons[0].getIconWidth(),icons[0].getIconHeight(),cursor)) {
			h.index = TOP;
		}else if (icons[1] != null&&hitBelow(cx + cw / 2,cy + ch,icons[1].getIconWidth(),icons[1].getIconHeight(),cursor)) {
			h.index = BOTTOM;
		}else if (icons[2] != null&&hitLeft(cx,cy + ch / 2,icons[2].getIconWidth(),icons[2].getIconHeight(),cursor)) {
			h.index = LEFT;
		}else if (icons[3] != null&&hitRight(cx + cw,cy + ch / 2,icons[3].getIconWidth(),icons[3].getIconHeight(),cursor)) {
			h.index = RIGHT;
		}else if (icons[4] != null&&hitLeft(cx,cy + ch,icons[4].getIconWidth(),icons[4].getIconHeight(),cursor)) {
			h.index = LOWER_LEFT;
		}else {
			h.index = -1;
		}
		if (h.index == -1) {
			h.instructions = getInstructions(15);
		}else {
			h.instructions = getInstructions(h.index);
		}
	}
	public void dragHandle(int mX,int mY,int anX,int anY,Handle hand) {
		mX = Math.max(mX,0);
		mY = Math.max(mY,0);
		if (hand.index < 10) {
			setPaintButtons(false);
			super.dragHandle(mX,mY,anX,anY,hand);
			return;
		}
		if (!isDraggableHandle(hand.index)) {
			return;
		}
		int cx = getContent().getX(),cy = getContent().getY();
		int cw = getContent().getWidth(),ch = getContent().getHeight();
		int bx = mX,by = mY;
		button = hand.index;
		switch (hand.index) {case TOP:
			by = cy;
			bx = cx + cw / 2;
			break;
		case BOTTOM:
			by = cy + ch;
			bx = cx + cw / 2;
			break;
		case LEFT:
			by = cy + ch / 2;
			bx = cx;
			break;
		case RIGHT:
			by = cy + ch / 2;
			bx = cx + cw;
			break;
		case LOWER_LEFT:
			by = cy + ch;
			bx = cx;
			break;
		default:
			break;
		}
		Object nodeType = getNewNodeType(hand.index);
		Object edgeType = getNewEdgeType(hand.index);
		boolean reverse = isReverseEdge(hand.index);
		if (edgeType != null&&nodeType != null) {
			Editor ce = Globals.curEditor();
			ModeCreateEdgeAndNode m = new ModeCreateEdgeAndNode(ce,edgeType,isEdgePostProcessRequested(),this);
			m.setup((FigNode) getContent(),getContent().getOwner(),bx,by,reverse);
			ce.pushMode(m);
		}
	}
	@Override public void buttonClicked(int buttonCode) {
		super.buttonClicked(buttonCode);
	}
	protected Object createEdgeAbove(MutableGraphModel gm,Object newNode) {
		return createEdge(gm,newNode,TOP);
	}
	protected Object createEdgeUnder(MutableGraphModel gm,Object newNode) {
		return createEdge(gm,newNode,BOTTOM);
	}
	protected Object createEdgeLeft(MutableGraphModel gm,Object newNode) {
		return createEdge(gm,newNode,LEFT);
	}
	protected Object createEdgeRight(MutableGraphModel gm,Object newNode) {
		return createEdge(gm,newNode,RIGHT);
	}
	private Object createEdge(MutableGraphModel gm,Object newNode,int index) {
		Object edge;
		if (isReverseEdge(index)) {
			edge = gm.connect(newNode,getContent().getOwner(),getNewEdgeType(index));
		}else {
			edge = gm.connect(getContent().getOwner(),newNode,getNewEdgeType(index));
		}
		return edge;
	}
	protected Object createEdgeToSelf(MutableGraphModel gm) {
		Object edge = gm.connect(getContent().getOwner(),getContent().getOwner(),getNewEdgeType(LOWER_LEFT));
		return edge;
	}
	protected abstract Icon[]getIcons();
	protected abstract String getInstructions(int index);
	protected abstract Object getNewNodeType(int index);
	protected abstract Object getNewEdgeType(int index);
	protected boolean isReverseEdge(int index) {
		return false;
	}
	protected boolean isDraggableHandle(int index) {
		return true;
	}
	protected boolean isEdgePostProcessRequested() {
		return false;
	}
	protected int getButton() {
		return button;
	}
}



