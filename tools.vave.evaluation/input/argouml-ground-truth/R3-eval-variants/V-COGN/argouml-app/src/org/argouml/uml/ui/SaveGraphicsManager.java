package org.argouml.uml.ui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.gefext.DeferredBufferedImage;
import org.argouml.i18n.Translator;
import org.argouml.util.FileFilters;
import org.argouml.util.SuffixFilter;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.SaveEPSAction;
import org.tigris.gef.base.SaveGIFAction;
import org.tigris.gef.base.SaveGraphicsAction;
import org.tigris.gef.base.SavePNGAction;
import org.tigris.gef.base.SavePSAction;
import org.tigris.gef.base.SaveSVGAction;
import org.tigris.gef.persistence.export.PostscriptWriter;


public final class SaveGraphicsManager {
	private static final int MIN_MARGIN = 15;
	public static final ConfigurationKey KEY_DEFAULT_GRAPHICS_FILTER = Configuration.makeKey("graphics","default","filter");
	public static final ConfigurationKey KEY_SAVE_GRAPHICS_PATH = Configuration.makeKey("graphics","save","path");
	public static final ConfigurationKey KEY_SAVEALL_GRAPHICS_PATH = Configuration.makeKey("graphics","save-all","path");
	public static final ConfigurationKey KEY_GRAPHICS_RESOLUTION = Configuration.makeKey("graphics","export","resolution");
	private SuffixFilter defaultFilter;
	private List<SuffixFilter>otherFilters = new ArrayList<SuffixFilter>();
	private static SaveGraphicsManager instance;
	private SaveGraphicsManager() {
		defaultFilter = FileFilters.PNG_FILTER;
		otherFilters.add(FileFilters.GIF_FILTER);
		otherFilters.add(FileFilters.SVG_FILTER);
		otherFilters.add(FileFilters.PS_FILTER);
		otherFilters.add(FileFilters.EPS_FILTER);
		setDefaultFilterBySuffix(Configuration.getString(KEY_DEFAULT_GRAPHICS_FILTER,defaultFilter.getSuffix()));
	}
	public void setDefaultFilterBySuffix(String suffix) {
		for (SuffixFilter sf:otherFilters) {
			if (sf.getSuffix().equalsIgnoreCase(suffix)) {
				setDefaultFilter(sf);
				break;
			}
		}
	}
	public void setDefaultFilter(SuffixFilter f) {
		otherFilters.remove(f);
		if (!otherFilters.contains(defaultFilter)) {
			otherFilters.add(defaultFilter);
		}
		defaultFilter = f;
		Configuration.setString(KEY_DEFAULT_GRAPHICS_FILTER,f.getSuffix());
		Collections.sort(otherFilters,new Comparator<SuffixFilter>() {
			public int compare(SuffixFilter arg0,SuffixFilter arg1) {
				return arg0.getSuffix().compareToIgnoreCase(arg1.getSuffix());
			}
		});
	}
	public static SaveGraphicsManager getInstance() {
		if (instance == null) {
			instance = new SaveGraphicsManager();
		}
		return instance;
	}
	public void register(SuffixFilter f) {
		otherFilters.add(f);
	}
	public void setFileChooserFilters(JFileChooser chooser,String defaultName) {
		chooser.addChoosableFileFilter(defaultFilter);
		Iterator iter = otherFilters.iterator();
		while (iter.hasNext()) {
			chooser.addChoosableFileFilter((SuffixFilter) iter.next());
		}
		chooser.setFileFilter(defaultFilter);
		String fileName = defaultName + "." + defaultFilter.getSuffix();
		chooser.setSelectedFile(new File(fileName));
		chooser.addPropertyChangeListener(JFileChooser.FILE_FILTER_CHANGED_PROPERTY,new FileFilterChangedListener(chooser,defaultName));
	}
	static class FileFilterChangedListener implements PropertyChangeListener {
	private JFileChooser chooser;
	private String defaultName;
	public FileFilterChangedListener(JFileChooser c,String name) {
		chooser = c;
		defaultName = name;
	}
	public void propertyChange(PropertyChangeEvent evt) {
		SuffixFilter filter = (SuffixFilter) evt.getNewValue();
		String fileName = defaultName + "." + filter.getSuffix();
		SwingUtilities.invokeLater(new Anonymous1(fileName));
	}
	class Anonymous1 implements Runnable {
		private String fileName;
		Anonymous1(String fn) {
				fileName = fn;
			}
		public void run() {
			chooser.setSelectedFile(new File(fileName));
		}
	}
}
	public SuffixFilter getFilterFromFileName(String name) {
		if (name.toLowerCase().endsWith("." + defaultFilter.getSuffix())) {
			return defaultFilter;
		}
		Iterator iter = otherFilters.iterator();
		while (iter.hasNext()) {
			SuffixFilter filter = (SuffixFilter) iter.next();
			if (name.toLowerCase().endsWith("." + filter.getSuffix())) {
				return filter;
			}
		}
		return null;
	}
	public String getDefaultSuffix() {
		return defaultFilter.getSuffix();
	}
	public String fixExtension(String in) {
		if (getFilterFromFileName(in) == null) {
			in += "." + getDefaultSuffix();
		}
		return in;
	}
	public SaveGraphicsAction getSaveActionBySuffix(String suffix) {
		SaveGraphicsAction cmd = null;
		if (FileFilters.PS_FILTER.getSuffix().equals(suffix)) {
			cmd = new SavePSAction(Translator.localize("action.save-ps"));
		}else if (FileFilters.EPS_FILTER.getSuffix().equals(suffix)) {
			cmd = new SaveScaledEPSAction(Translator.localize("action.save-eps"));
		}else if (FileFilters.PNG_FILTER.getSuffix().equals(suffix)) {
			cmd = new SavePNGAction2(Translator.localize("action.save-png"));
		}else if (FileFilters.GIF_FILTER.getSuffix().equals(suffix)) {
			cmd = new SaveGIFAction(Translator.localize("action.save-gif"));
		}else if (FileFilters.SVG_FILTER.getSuffix().equals(suffix)) {
			cmd = new SaveSVGAction(Translator.localize("action.save-svg"));
		}
		return cmd;
	}
	public List<SuffixFilter>getSettingsList() {
		List<SuffixFilter>c = new ArrayList<SuffixFilter>();
		c.add(defaultFilter);
		c.addAll(otherFilters);
		return c;
	}
	static Rectangle adjustDrawingArea(Rectangle area) {
		int xMargin = area.x;
		if (xMargin < 0) {
			xMargin = 0;
		}
		int yMargin = area.y;
		if (yMargin < 0) {
			yMargin = 0;
		}
		int margin = Math.max(xMargin,yMargin);
		if (margin < MIN_MARGIN) {
			margin = MIN_MARGIN;
		}
		return new Rectangle(0,0,area.width + (2 * margin),area.height + (2 * margin));
	}
}

class SaveScaledEPSAction extends SaveEPSAction {
	SaveScaledEPSAction(String name) {
			super(name);
		}
	@Override protected void saveGraphics(OutputStream s,Editor ce,Rectangle drawingArea)throws IOException {
		double editorScale = ce.getScale();
		int x = (int) (drawingArea.x * editorScale);
		int y = (int) (drawingArea.y * editorScale);
		int h = (int) (drawingArea.height * editorScale);
		int w = (int) (drawingArea.width * editorScale);
		drawingArea = new Rectangle(x,y,w,h);
		PostscriptWriter ps = new PostscriptWriter(s,drawingArea);
		ps.scale(editorScale,editorScale);
		ce.print(ps);
		ps.dispose();
	}
}

class SavePNGAction2 extends SavePNGAction {
	SavePNGAction2(String name) {
			super(name);
		}
	@Override public void actionPerformed(ActionEvent ae) {
		Editor ce = Globals.curEditor();
		Rectangle drawingArea = ce.getLayerManager().getActiveLayer().calcDrawingArea();
		if (drawingArea.width <= 0||drawingArea.height <= 0) {
			Rectangle dummyArea = new Rectangle(0,0,50,50);
			try {
				saveGraphics(outputStream,ce,dummyArea);
			}catch (java.io.IOException e) {
			}
			return;
		}
		super.actionPerformed(ae);
	}
	@Override protected void saveGraphics(OutputStream s,Editor ce,Rectangle drawingArea)throws IOException {
		Rectangle canvasArea = SaveGraphicsManager.adjustDrawingArea(drawingArea);
		RenderedImage i = new DeferredBufferedImage(canvasArea,BufferedImage.TYPE_INT_ARGB,ce,scale);
		ImageIO.write(i,"png",s);
	}
}

class SaveGIFAction2 extends SaveGIFAction {
	SaveGIFAction2(String name) {
			super(name);
		}
	@Override protected void saveGraphics(OutputStream s,Editor ce,Rectangle drawingArea)throws IOException {
		Rectangle canvasArea = SaveGraphicsManager.adjustDrawingArea(drawingArea);
		RenderedImage i = new DeferredBufferedImage(canvasArea,BufferedImage.TYPE_INT_ARGB,ce,scale);
		ImageIO.write(i,"gif",s);
	}
}



