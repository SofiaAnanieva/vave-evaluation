package org.argouml.gefext;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.tigris.gef.base.Editor;


public class DeferredBufferedImage implements RenderedImage {
	private static final Logger LOG = Logger.getLogger(DeferredBufferedImage.class);
	public static final int TRANSPARENT_BG_COLOR = 0xefefef;
	public static final Color BACKGROUND_COLOR = new Color(TRANSPARENT_BG_COLOR,true);
	private static final int BUFFER_HEIGHT = 32;
	private int x,y;
	private int width;
	private int height;
	private int scale;
	private BufferedImage image;
	private Editor editor;
	private int scaledBufferHeight;
	private int y1,y2;
	public DeferredBufferedImage(Rectangle drawingArea,int imageType,Editor ed,int scaleFactor) {
		editor = ed;
		scale = scaleFactor;
		x = drawingArea.x;
		y = drawingArea.y;
		width = drawingArea.width;
		height = drawingArea.height;
		x = x * scale;
		y = y * scale;
		width = width * scale;
		height = height * scale;
		scaledBufferHeight = BUFFER_HEIGHT * scale;
		image = new BufferedImage(width,scaledBufferHeight,imageType);
		y1 = y;
		y2 = y1;
	}
	public Raster getData() {
		LOG.debug("getData with no params");
		return getData(new Rectangle(x,y,width,height));
	}
	public Raster getData(Rectangle clip) {
		if (!isRasterValid(clip)) {
			LOG.debug("Raster not valid, computing new raster");
			computeRaster(clip);
		}
		Rectangle oClip = offsetWindow(clip);
		Raster ras = image.getData(oClip);
		Raster translatedRaster = ras.createTranslatedChild(clip.x,clip.y);
		return translatedRaster;
	}
	private boolean isRasterValid(Rectangle clip) {
		if (clip.height > scaledBufferHeight) {
			throw new IndexOutOfBoundsException("clip rectangle must fit in band buffer");
		}
		return(clip.y >= y1&&(clip.y + clip.height - 1) < y2);
	}
	private void computeRaster(Rectangle clip) {
		LOG.debug("Computing raster for rectangle " + clip);
		Graphics2D graphics = image.createGraphics();
		graphics.scale(1.0 * scale,1.0 * scale);
		graphics.setColor(BACKGROUND_COLOR);
		Composite c = graphics.getComposite();
		graphics.setComposite(AlphaComposite.Src);
		graphics.fillRect(0,0,width,scaledBufferHeight);
		graphics.setComposite(c);
		graphics.setClip(0,0,width,scaledBufferHeight);
		graphics.translate(0,-clip.y / scale);
		y1 = clip.y;
		y2 = y1 + scaledBufferHeight;
		editor.print(graphics);
		graphics.dispose();
	}
	private Rectangle offsetWindow(Rectangle clip) {
		int baseY = clip.y - y1;
		return new Rectangle(clip.x,baseY,clip.width,Math.min(clip.height,scaledBufferHeight - baseY));
	}
	public WritableRaster copyData(WritableRaster outRaster) {
		throw new UnsupportedOperationException();
	}
	public Vector<RenderedImage>getSources() {
		return null;
	}
	public ColorModel getColorModel() {
		return image.getColorModel();
	}
	public int getMinX() {
		LOG.debug("getMinX = 0");
		return 0;
	}
	public int getMinY() {
		LOG.debug("getMinY = 0");
		return 0;
	}
	public int getMinTileX() {
		LOG.debug("getMinTileX = 0");
		return 0;
	}
	public int getMinTileY() {
		LOG.debug("getMinTileY = 0");
		return 0;
	}
	public int getNumXTiles() {
		LOG.debug("getNumXTiles = 1");
		return 1;
	}
	public int getNumYTiles() {
		int tiles = (getHeight() + scaledBufferHeight - 1) / scaledBufferHeight;
		LOG.debug("getNumYTiles = " + tiles);
		return tiles;
	}
	public Object getProperty(String name) {
		return image.getProperty(name);
	}
	public String[]getPropertyNames() {
		return image.getPropertyNames();
	}
	public SampleModel getSampleModel() {
		return image.getSampleModel();
	}
	public Raster getTile(int tileX,int tileY) {
		LOG.debug("getTile x=" + tileX + " y = " + tileY);
		if (tileX < getMinTileX()||tileX >= getMinTileX() + getNumXTiles()||tileY < getMinTileY()||tileY >= getMinTileY() + getNumYTiles()) {
			throw new IndexOutOfBoundsException();
		}
		Rectangle tileBounds = new Rectangle(0,(tileY - getMinTileY() * scaledBufferHeight),width,scaledBufferHeight);
		return getData(tileBounds);
	}
	public int getTileGridXOffset() {
		LOG.debug("getTileGridXOffset = 0");
		return 0;
	}
	public int getTileGridYOffset() {
		LOG.debug("getTileGridYOffset = 0");
		return 0;
	}
	public int getTileWidth() {
		LOG.debug("getTileWidth = " + width);
		return width;
	}
	public int getTileHeight() {
		LOG.debug("getTileHeight = " + scaledBufferHeight);
		return scaledBufferHeight;
	}
	public int getWidth() {
		LOG.debug("getWidth = " + width);
		return width;
	}
	public int getHeight() {
		LOG.debug("getHeight = " + height);
		return height;
	}
}



