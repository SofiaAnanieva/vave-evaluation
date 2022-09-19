package org.argouml.uml.diagram.ui;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.application.events.ArgoNotationEventListener;
import org.argouml.i18n.Translator;
import org.argouml.model.UmlChangeEvent;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.notation.NotationSettings;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigSingleLineText;


public class FigSingleLineTextWithNotation extends FigSingleLineText implements ArgoNotationEventListener {
	@SuppressWarnings("deprecation")@Deprecated public FigSingleLineTextWithNotation(int x,int y,int w,int h,boolean expandOnly) {
		super(x,y,w,h,expandOnly);
	}
	@SuppressWarnings("deprecation")@Deprecated public FigSingleLineTextWithNotation(int x,int y,int w,int h,boolean expandOnly,String[]allProperties) {
		super(x,y,w,h,expandOnly,allProperties);
	}
	public FigSingleLineTextWithNotation(Object owner,Rectangle bounds,DiagramSettings settings,boolean expandOnly) {
		super(owner,bounds,settings,expandOnly);
		initNotationProviders();
	}
	public FigSingleLineTextWithNotation(Object owner,Rectangle bounds,DiagramSettings settings,boolean expandOnly,String property) {
		super(owner,bounds,settings,expandOnly,property);
		initNotationProviders();
	}
	public FigSingleLineTextWithNotation(Object owner,Rectangle bounds,DiagramSettings settings,boolean expandOnly,String[]allProperties) {
		super(owner,bounds,settings,expandOnly,allProperties);
		initNotationProviders();
	}
	@SuppressWarnings("deprecation")@Deprecated@Override public void setOwner(Object owner) {
		super.setOwner(owner);
		initNotationProviders();
	}
	private NotationProvider notationProvider;
	private HashMap<String,Object>npArguments = new HashMap<String,Object>();
	@Override public void removeFromDiagram() {
		ArgoEventPump.removeListener(ArgoEventTypes.ANY_NOTATION_EVENT,this);
		notationProvider.cleanListener(this,getOwner());
		super.removeFromDiagram();
	}
	@Override public void propertyChange(PropertyChangeEvent pce) {
		if (notationProvider != null) {
			notationProvider.updateListener(this,getOwner(),pce);
		}
		super.propertyChange(pce);
	}
	protected void updateLayout(UmlChangeEvent event) {
		assert event != null;
		if (notationProvider != null&&(!"remove".equals(event.getPropertyName())||event.getSource() != getOwner())) {
			this.setText(notationProvider.toString(getOwner(),getNotationSettings()));
			damage();
		}
	}
	protected int getNotationProviderType() {
		return NotationProviderFactory2.TYPE_NAME;
	}
	public NotationProvider getNotationProvider() {
		return notationProvider;
	}
	void setNotationProvider(NotationProvider np) {
		if (notationProvider != null) {
			notationProvider.cleanListener(this,getOwner());
		}
		this.notationProvider = np;
		initNotationArguments();
	}
	@Deprecated public HashMap<String,Object>getNpArguments() {
		return npArguments;
	}
	protected void initNotationProviders() {
		if (notationProvider != null) {
			notationProvider.cleanListener(this,getOwner());
		}
		if (getOwner() != null) {
			NotationName notation = Notation.findNotation(getNotationSettings().getNotationLanguage());
			notationProvider = NotationProviderFactory2.getInstance().getNotationProvider(getNotationProviderType(),getOwner(),this,notation);
			initNotationArguments();
		}
	}
	@Deprecated protected void initNotationArguments() {
		npArguments.put("useGuillemets",getNotationSettings().isUseGuillemets());
	}
	@Deprecated protected void putNotationArgument(String key,Object element) {
		npArguments.put(key,element);
	}
	protected void textEditStarted() {
		String s = getNotationProvider().getParsingHelp();
		showHelp(s);
		setText();
	}
	protected void textEdited() {
		notationProvider.parse(getOwner(),getText());
		setText();
	}
	@Deprecated public void notationAdded(ArgoNotationEvent e) {
	}
	@Deprecated public void notationChanged(ArgoNotationEvent e) {
		renderingChanged();
	}
	@Deprecated public void notationProviderAdded(ArgoNotationEvent e) {
	}
	@Deprecated public void notationProviderRemoved(ArgoNotationEvent e) {
	}
	@Deprecated public void notationRemoved(ArgoNotationEvent e) {
	}
	public void renderingChanged() {
		initNotationProviders();
		super.renderingChanged();
	}
	@Override protected void setText() {
		assert getOwner() != null;
		assert notationProvider != null;
		setText(notationProvider.toString(getOwner(),getNotationSettings()));
	}
	protected void showHelp(String s) {
		ArgoEventPump.fireEvent(new ArgoHelpEvent(ArgoEventTypes.HELP_CHANGED,this,Translator.localize(s)));
	}
	protected NotationSettings getNotationSettings() {
		return getSettings().getNotationSettings();
	}
}



