package org.argouml.notation;

import org.tigris.gef.undo.Memento;


public class NotationSettings {
	private static final NotationSettings DEFAULT_SETTINGS = initializeDefaultSettings();
	private NotationSettings parent;
	private String notationLanguage;
	private boolean showAssociationNames;
	private boolean showAssociationNamesSet = false;
	private boolean showVisibilities;
	private boolean showVisibilitiesSet = false;
	private boolean showPaths;
	private boolean showPathsSet = false;
	private boolean fullyHandleStereotypes;
	private boolean fullyHandleStereotypesSet = false;
	private boolean useGuillemets;
	private boolean useGuillemetsSet = false;
	private boolean showMultiplicities;
	private boolean showMultiplicitiesSet = false;
	private boolean showSingularMultiplicities;
	private boolean showSingularMultiplicitiesSet = false;
	private boolean showTypes;
	private boolean showTypesSet = false;
	private boolean showProperties;
	private boolean showPropertiesSet = false;
	private boolean showInitialValues;
	private boolean showInitialValuesSet = false;
	public NotationSettings() {
		super();
		parent = getDefaultSettings();
	}
	public NotationSettings(NotationSettings parentSettings) {
		this();
		parent = parentSettings;
	}
	private static NotationSettings initializeDefaultSettings() {
		NotationSettings settings = new NotationSettings();
		settings.parent = null;
		settings.setNotationLanguage(Notation.DEFAULT_NOTATION);
		settings.setFullyHandleStereotypes(false);
		settings.setShowAssociationNames(true);
		settings.setShowInitialValues(false);
		settings.setShowMultiplicities(false);
		settings.setShowPaths(false);
		settings.setShowProperties(false);
		settings.setShowSingularMultiplicities(true);
		settings.setShowTypes(true);
		settings.setShowVisibilities(false);
		settings.setUseGuillemets(false);
		return settings;
	}
	public static NotationSettings getDefaultSettings() {
		return DEFAULT_SETTINGS;
	}
	public String getNotationLanguage() {
		if (notationLanguage == null) {
			if (parent != null) {
				return parent.getNotationLanguage();
			}else {
				return Notation.DEFAULT_NOTATION;
			}
		}
		return notationLanguage;
	}
	public boolean setNotationLanguage(final String newLanguage) {
		if (notationLanguage != null&&notationLanguage.equals(newLanguage)) {
			return true;
		}
		if (Notation.findNotation(newLanguage) == null) {
			return false;
		}
		final String oldLanguage = notationLanguage;
		Memento memento = new Memento() {
	public void redo() {
		notationLanguage = newLanguage;
	}
	public void undo() {
		notationLanguage = oldLanguage;
	}
};
		doUndoable(memento);
		return true;
	}
	public boolean isFullyHandleStereotypes() {
		if (fullyHandleStereotypesSet) {
			return fullyHandleStereotypes;
		}else {
			if (parent != null) {
				return parent.fullyHandleStereotypes;
			}else {
				return getDefaultSettings().isFullyHandleStereotypes();
			}
		}
	}
	public void setFullyHandleStereotypes(boolean newValue) {
		fullyHandleStereotypes = newValue;
		fullyHandleStereotypesSet = true;
	}
	public boolean isShowSingularMultiplicities() {
		if (showSingularMultiplicitiesSet) {
			return showSingularMultiplicities;
		}else if (parent != null) {
			return parent.isShowSingularMultiplicities();
		}
		return getDefaultSettings().isShowSingularMultiplicities();
	}
	public void setShowSingularMultiplicities(final boolean showem) {
		if (showSingularMultiplicities == showem&&showSingularMultiplicitiesSet) {
			return;
		}
		final boolean oldValid = showSingularMultiplicitiesSet;
		Memento memento = new Memento() {
	public void redo() {
		showSingularMultiplicities = showem;
		showSingularMultiplicitiesSet = true;
	}
	public void undo() {
		showSingularMultiplicities = !showem;
		showSingularMultiplicitiesSet = oldValid;
	}
};
		doUndoable(memento);
	}
	public boolean isUseGuillemets() {
		if (useGuillemetsSet) {
			return useGuillemets;
		}else if (parent != null) {
			return parent.isUseGuillemets();
		}
		return getDefaultSettings().isUseGuillemets();
	}
	public void setUseGuillemets(final boolean showem) {
		if (useGuillemets == showem&&useGuillemetsSet) {
			return;
		}
		final boolean oldValid = useGuillemetsSet;
		Memento memento = new Memento() {
	public void redo() {
		useGuillemets = showem;
		useGuillemetsSet = true;
	}
	public void undo() {
		useGuillemets = !showem;
		useGuillemetsSet = oldValid;
	}
};
		doUndoable(memento);
	}
	public boolean isShowTypes() {
		if (showTypesSet) {
			return showTypes;
		}else if (parent != null) {
			return parent.isShowTypes();
		}
		return getDefaultSettings().isShowTypes();
	}
	public void setShowTypes(final boolean showem) {
		if (showTypes == showem&&showTypesSet) {
			return;
		}
		final boolean oldValid = showTypesSet;
		Memento memento = new Memento() {
	public void redo() {
		showTypes = showem;
		showTypesSet = true;
	}
	public void undo() {
		showTypes = !showem;
		showTypesSet = oldValid;
	}
};
		doUndoable(memento);
	}
	public boolean isShowProperties() {
		if (showPropertiesSet) {
			return showProperties;
		}else if (parent != null) {
			return parent.isShowProperties();
		}
		return getDefaultSettings().isShowProperties();
	}
	public void setShowProperties(final boolean showem) {
		if (showProperties == showem&&showPropertiesSet) {
			return;
		}
		final boolean oldValid = showPropertiesSet;
		Memento memento = new Memento() {
	public void redo() {
		showProperties = showem;
		showPropertiesSet = true;
	}
	public void undo() {
		showProperties = !showem;
		showPropertiesSet = oldValid;
	}
};
		doUndoable(memento);
	}
	public boolean isShowInitialValues() {
		if (showInitialValuesSet) {
			return showInitialValues;
		}else if (parent != null) {
			return parent.isShowInitialValues();
		}
		return getDefaultSettings().isShowInitialValues();
	}
	public void setShowInitialValues(final boolean showem) {
		if (showInitialValues == showem&&showInitialValuesSet) {
			return;
		}
		final boolean oldValid = showInitialValuesSet;
		Memento memento = new Memento() {
	public void redo() {
		showInitialValues = showem;
		showInitialValuesSet = true;
	}
	public void undo() {
		showInitialValues = !showem;
		showInitialValuesSet = oldValid;
	}
};
		doUndoable(memento);
	}
	public boolean isShowMultiplicities() {
		if (showMultiplicitiesSet) {
			return showMultiplicities;
		}else if (parent != null) {
			return parent.isShowMultiplicities();
		}
		return getDefaultSettings().isShowMultiplicities();
	}
	public void setShowMultiplicities(final boolean showem) {
		if (showMultiplicities == showem&&showMultiplicitiesSet) {
			return;
		}
		final boolean oldValid = showMultiplicitiesSet;
		Memento memento = new Memento() {
	public void redo() {
		showMultiplicities = showem;
		showMultiplicitiesSet = true;
	}
	public void undo() {
		showMultiplicities = !showem;
		showMultiplicitiesSet = oldValid;
	}
};
		doUndoable(memento);
	}
	public boolean isShowAssociationNames() {
		if (showAssociationNamesSet) {
			return showAssociationNames;
		}else if (parent != null) {
			return parent.isShowAssociationNames();
		}
		return getDefaultSettings().isShowAssociationNames();
	}
	public void setShowAssociationNames(final boolean showem) {
		if (showAssociationNames == showem&&showAssociationNamesSet) {
			return;
		}
		final boolean oldValid = showAssociationNamesSet;
		Memento memento = new Memento() {
	public void redo() {
		showAssociationNames = showem;
		showAssociationNamesSet = true;
	}
	public void undo() {
		showAssociationNames = !showem;
		showAssociationNamesSet = oldValid;
	}
};
		doUndoable(memento);
	}
	public boolean isShowVisibilities() {
		if (showVisibilitiesSet) {
			return showVisibilities;
		}else if (parent != null) {
			return parent.isShowVisibilities();
		}
		return getDefaultSettings().isShowVisibilities();
	}
	public void setShowVisibilities(final boolean showem) {
		if (showVisibilities == showem&&showVisibilitiesSet) {
			return;
		}
		final boolean oldValid = showVisibilitiesSet;
		Memento memento = new Memento() {
	public void redo() {
		showVisibilities = showem;
		showVisibilitiesSet = true;
	}
	public void undo() {
		showVisibilities = !showem;
		showVisibilitiesSet = oldValid;
	}
};
		doUndoable(memento);
	}
	public boolean isShowPaths() {
		if (showPathsSet) {
			return showPaths;
		}else if (parent != null) {
			return parent.isShowPaths();
		}
		return getDefaultSettings().isShowPaths();
	}
	public void setShowPaths(boolean showPaths) {
		this.showPaths = showPaths;
		showPathsSet = true;
	}
	private void doUndoable(Memento memento) {
		memento.redo();
	}
}



