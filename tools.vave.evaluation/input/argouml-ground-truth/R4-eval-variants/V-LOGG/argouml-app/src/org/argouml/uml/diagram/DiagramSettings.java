package org.argouml.uml.diagram;

import java.awt.Font;
import org.argouml.notation.NotationSettings;
import org.tigris.gef.undo.Memento;
import org.argouml.uml.diagram.DiagramAppearance;


public class DiagramSettings {
	public enum StereotypeStyle {
	TEXTUAL (DiagramAppearance.STEREOTYPE_VIEW_TEXTUAL),
	BIG_ICON (DiagramAppearance.STEREOTYPE_VIEW_BIG_ICON),
	SMALL_ICON (DiagramAppearance.STEREOTYPE_VIEW_SMALL_ICON);
	StereotypeStyle(int value) {
			assert value == this.ordinal();
		}
	public static StereotypeStyle getEnum(int value) {
		int counter = 0;
		for (StereotypeStyle sv:StereotypeStyle.values()) {
			if (counter == value) {
				return sv;
			}
			counter++;
		}
		return null;
	}}
	private DiagramSettings parent;
	private NotationSettings notationSettings;
	private String fontName;
	private Integer fontSize;
	private Boolean showBoldNames;
	private Font fontPlain;
	private Font fontItalic;
	private Font fontBold;
	private Font fontBoldItalic;
	private Boolean showBidirectionalArrows;
	private Integer defaultShadowWidth;
	private StereotypeStyle defaultStereotypeView;
	public DiagramSettings() {
		this(null);
	}
	public DiagramSettings(DiagramSettings parentSettings) {
		super();
		parent = parentSettings;
		if (parentSettings == null) {
			notationSettings = new NotationSettings();
		}else {
			notationSettings = new NotationSettings(parent.getNotationSettings());
		}
		recomputeFonts();
	}
	public NotationSettings getNotationSettings() {
		return notationSettings;
	}
	public void setNotationSettings(NotationSettings notationSettings) {
		this.notationSettings = notationSettings;
	}
	public boolean isShowBoldNames() {
		if (showBoldNames == null) {
			if (parent != null) {
				return parent.isShowBoldNames();
			}else {
				return false;
			}
		}
		return showBoldNames;
	}
	public void setShowBoldNames(final boolean showem) {
		if (showBoldNames != null&&showBoldNames == showem) {
			return;
		}
		Memento memento = new Memento() {
	public void redo() {
		showBoldNames = showem;
	}
	public void undo() {
		showBoldNames = !showem;
	}
};
		doUndoable(memento);
	}
	public boolean isShowBidirectionalArrows() {
		if (showBidirectionalArrows == null) {
			if (parent != null) {
				return parent.isShowBidirectionalArrows();
			}else {
				return false;
			}
		}
		return showBidirectionalArrows;
	}
	public void setShowBidirectionalArrows(final boolean showem) {
		if (showBidirectionalArrows != null&&showBidirectionalArrows == showem) {
			return;
		}
		Memento memento = new Memento() {
	public void redo() {
		showBidirectionalArrows = showem;
	}
	public void undo() {
		showBidirectionalArrows = !showem;
	}
};
		doUndoable(memento);
	}
	public int getDefaultShadowWidth() {
		if (defaultShadowWidth == null) {
			if (parent != null) {
				return parent.getDefaultShadowWidth();
			}else {
				return 0;
			}
		}
		return defaultShadowWidth;
	}
	public void setDefaultShadowWidth(final int newWidth) {
		if (defaultShadowWidth != null&&defaultShadowWidth == newWidth) {
			return;
		}
		final Integer oldValue = defaultShadowWidth;
		Memento memento = new Memento() {
	public void redo() {
		defaultShadowWidth = newWidth;
	}
	public void undo() {
		defaultShadowWidth = oldValue;
	}
};
		doUndoable(memento);
	}
	public StereotypeStyle getDefaultStereotypeView() {
		if (defaultStereotypeView == null) {
			if (parent != null) {
				return parent.getDefaultStereotypeView();
			}else {
				return StereotypeStyle.TEXTUAL;
			}
		}
		return defaultStereotypeView;
	}
	public int getDefaultStereotypeViewInt() {
		return getDefaultStereotypeView().ordinal();
	}
	public void setDefaultStereotypeView(final int newView) {
		StereotypeStyle sv = StereotypeStyle.getEnum(newView);
		if (sv == null) {
			throw new IllegalArgumentException("Bad argument " + newView);
		}
		setDefaultStereotypeView(sv);
	}
	public void setDefaultStereotypeView(final StereotypeStyle newView) {
		if (defaultStereotypeView != null&&defaultStereotypeView == newView) {
			return;
		}
		final StereotypeStyle oldValue = defaultStereotypeView;
		Memento memento = new Memento() {
	public void redo() {
		defaultStereotypeView = newView;
	}
	public void undo() {
		defaultStereotypeView = oldValue;
	}
};
		doUndoable(memento);
	}
	public String getFontName() {
		if (fontName == null) {
			if (parent != null) {
				return parent.getFontName();
			}else {
				return"Dialog";
			}
		}
		return fontName;
	}
	public void setFontName(String newFontName) {
		if (fontName != null&&fontName.equals(newFontName)) {
			return;
		}
		fontName = newFontName;
		recomputeFonts();
	}
	public int getFontSize() {
		if (fontSize == null) {
			if (parent != null) {
				return parent.getFontSize();
			}else {
				return 10;
			}
		}
		return fontSize;
	}
	public void setFontSize(int newFontSize) {
		if (fontSize != null&&fontSize == newFontSize) {
			return;
		}
		fontSize = newFontSize;
		recomputeFonts();
	}
	private void recomputeFonts() {
		if ((fontName != null&&!"".equals(fontName)&&fontSize != null)||parent == null) {
			String name = getFontName();
			int size = getFontSize();
			fontPlain = new Font(name,Font.PLAIN,size);
			fontItalic = new Font(name,Font.ITALIC,size);
			fontBold = new Font(name,Font.BOLD,size);
			fontBoldItalic = new Font(name,Font.BOLD|Font.ITALIC,size);
		}else {
			fontPlain = null;
			fontItalic = null;
			fontBold = null;
			fontBoldItalic = null;
		}
	}
	public Font getFontPlain() {
		if (fontPlain == null) {
			return parent.getFontPlain();
		}
		return fontPlain;
	}
	public Font getFontItalic() {
		if (fontItalic == null) {
			return parent.getFontItalic();
		}
		return fontItalic;
	}
	public Font getFontBold() {
		if (fontBold == null) {
			return parent.getFontBold();
		}
		return fontBold;
	}
	public Font getFontBoldItalic() {
		if (fontBoldItalic == null) {
			return parent.getFontBoldItalic();
		}
		return fontBoldItalic;
	}
	public Font getFont(int fontStyle) {
		if ((fontStyle&Font.ITALIC) != 0) {
			if ((fontStyle&Font.BOLD) != 0) {
				return getFontBoldItalic();
			}else {
				return getFontItalic();
			}
		}else {
			if ((fontStyle&Font.BOLD) != 0) {
				return getFontBold();
			}else {
				return getFontPlain();
			}
		}
	}
	private void doUndoable(Memento memento) {
		memento.redo();
	}
}



