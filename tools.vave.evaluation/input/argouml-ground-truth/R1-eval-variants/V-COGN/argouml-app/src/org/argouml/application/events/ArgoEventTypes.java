package org.argouml.application.events;


public interface ArgoEventTypes {
	int ANY_EVENT = 1000;
	int ANY_MODULE_EVENT = 1100;
	int MODULE_LOADED = 1101;
	int MODULE_UNLOADED = 1102;
	int MODULE_ENABLED = 1103;
	int MODULE_DISABLED = 1104;
	int LAST_MODULE_EVENT = 1199;
	int ANY_NOTATION_EVENT = 1200;
	int NOTATION_CHANGED = 1201;
	int NOTATION_ADDED = 1202;
	int NOTATION_REMOVED = 1203;
	int NOTATION_PROVIDER_ADDED = 1204;
	int NOTATION_PROVIDER_REMOVED = 1205;
	int LAST_NOTATION_EVENT = 1299;
	int ANY_GENERATOR_EVENT = 1300;
	int GENERATOR_CHANGED = 1301;
	int GENERATOR_ADDED = 1302;
	int GENERATOR_REMOVED = 1303;
	int LAST_GENERATOR_EVENT = 1399;
	int ANY_HELP_EVENT = 1400;
	int HELP_CHANGED = 1401;
	int HELP_REMOVED = 1403;
	int LAST_HELP_EVENT = 1499;
	int ANY_STATUS_EVENT = 1500;
	int STATUS_TEXT = 1501;
	int STATUS_CLEARED = 1503;
	int STATUS_PROJECT_SAVED = 1504;
	int STATUS_PROJECT_LOADED = 1505;
	int STATUS_PROJECT_MODIFIED = 1506;
	int LAST_STATUS_EVENT = 1599;
	int ANY_DIAGRAM_APPEARANCE_EVENT = 1600;
	int DIAGRAM_FONT_CHANGED = 1601;
	int LAST_DIAGRAM_APPEARANCE_EVENT = 1699;
	int ANY_PROFILE_EVENT = 1700;
	int PROFILE_ADDED = 1701;
	int PROFILE_REMOVED = 1702;
	int LAST_PROFILE_EVENT = 1799;
	int ARGO_EVENT_END = 99999;
}



