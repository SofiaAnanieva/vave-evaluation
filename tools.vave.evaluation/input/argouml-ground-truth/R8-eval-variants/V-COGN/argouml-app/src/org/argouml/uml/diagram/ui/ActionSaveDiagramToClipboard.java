package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.SaveGraphicsManager;
import org.tigris.gef.base.SaveGIFAction;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;


public class ActionSaveDiagramToClipboard extends AbstractAction implements ClipboardOwner {
	public ActionSaveDiagramToClipboard() {
		super(Translator.localize("menu.popup.copy-diagram-to-clip"),ResourceLoaderWrapper.lookupIcon("action.copy"));
	}
	public void actionPerformed(ActionEvent actionEvent) {
		Image diagramGifImage = getImage();
		if (diagramGifImage == null) {
			return;
		}
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new ImageSelection(diagramGifImage),this);
	}
	private Image getImage() {
		int scale = Configuration.getInteger(SaveGraphicsManager.KEY_GRAPHICS_RESOLUTION,1);
		Editor ce = Globals.curEditor();
		Rectangle drawingArea = ce.getLayerManager().getActiveLayer().calcDrawingArea();
		if (drawingArea.x < 0||drawingArea.y < 0||drawingArea.width <= 0||drawingArea.height <= 0) {
			return null;
		}
		boolean isGridHidden = ce.getGridHidden();
		ce.setGridHidden(true);
		Image diagramGifImage = ce.createImage(drawingArea.width * scale,drawingArea.height * scale);
		Graphics g = diagramGifImage.getGraphics();
		if (g instanceof Graphics2D) {
			((Graphics2D) g).scale(scale,scale);
		}
		g.setColor(new Color(SaveGIFAction.TRANSPARENT_BG_COLOR));
		g.fillRect(0,0,drawingArea.width * scale,drawingArea.height * scale);
		g.translate(-drawingArea.x,-drawingArea.y);
		ce.print(g);
		ce.setGridHidden(isGridHidden);
		return diagramGifImage;
	}
	public void lostOwnership(Clipboard clipboard,Transferable transferable) {
	}
	public boolean isEnabled() {
		Editor ce = Globals.curEditor();
		if (ce == null||ce.getLayerManager() == null||ce.getLayerManager().getActiveLayer() == null) {
			return false;
		}
		Layer layer = ce.getLayerManager().getActiveLayer();
		if (layer == null) {
			return false;
		}
		Rectangle drawingArea = layer.calcDrawingArea();
		if (drawingArea.x < 0||drawingArea.y < 0||drawingArea.width <= 0||drawingArea.height <= 0) {
			return false;
		}
		return super.isEnabled();
	}
	private static final long serialVersionUID = 4916652432210626558l;
}

class ImageSelection implements Transferable {
	private DataFlavor[]supportedFlavors =  {DataFlavor.imageFlavor};
	private Image diagramImage;
	public ImageSelection(Image newDiagramImage) {
		diagramImage = newDiagramImage;
	}
	public synchronized DataFlavor[]getTransferDataFlavors() {
		return(supportedFlavors);
	}
	public boolean isDataFlavorSupported(DataFlavor parFlavor) {
		return(parFlavor.getMimeType().equals(DataFlavor.imageFlavor.getMimeType())&&parFlavor.getHumanPresentableName().equals(DataFlavor.imageFlavor.getHumanPresentableName()));
	}
	public synchronized Object getTransferData(DataFlavor parFlavor)throws UnsupportedFlavorException {
		if (isDataFlavorSupported(parFlavor)) {
			return(diagramImage);
		}
		throw new UnsupportedFlavorException(DataFlavor.imageFlavor);
	}
}



