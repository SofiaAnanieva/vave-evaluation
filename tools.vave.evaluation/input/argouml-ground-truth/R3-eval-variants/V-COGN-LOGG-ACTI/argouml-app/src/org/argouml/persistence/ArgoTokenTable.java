package org.argouml.persistence;


class ArgoTokenTable extends XMLTokenTableBase {
	public ArgoTokenTable() {
		super(32);
	}
	private static final String STRING_ARGO = "argo";
	private static final String STRING_AUTHORNAME = "authorname";
	private static final String STRING_AUTHOREMAIL = "authoremail";
	private static final String STRING_VERSION = "version";
	private static final String STRING_DESCRIPTION = "description";
	private static final String STRING_SEARCHPATH = "searchpath";
	private static final String STRING_MEMBER = "member";
	private static final String STRING_HISTORYFILE = "historyfile";
	private static final String STRING_DOCUMENTATION = "documentation";
	private static final String STRING_SETTINGS = "settings";
	private static final String STRING_NOTATIONLANGUAGE = "notationlanguage";
	private static final String STRING_SHOWBOLDNAMES = "showboldnames";
	private static final String STRING_USEGUILLEMOTS = "useguillemots";
	private static final String STRING_SHOWASSOCIATIONNAMES = "showassociationnames";
	private static final String STRING_SHOWVISIBILITY = "showvisibility";
	private static final String STRING_SHOWMULTIPLICITY = "showmultiplicity";
	private static final String STRING_SHOWINITIALVALUE = "showinitialvalue";
	private static final String STRING_SHOWPROPERTIES = "showproperties";
	private static final String STRING_SHOWTYPES = "showtypes";
	private static final String STRING_SHOWSTEREOTYPES = "showstereotypes";
	private static final String STRING_SHOWSINGULARMULTIPLICITIES = "showsingularmultiplicities";
	private static final String STRING_HIDEBIDIRECTIONALARROWS = "hidebidirectionalarrows";
	private static final String STRING_DEFAULTSHADOWWIDTH = "defaultshadowwidth";
	private static final String STRING_FONTNAME = "fontname";
	private static final String STRING_FONTSIZE = "fontsize";
	@Deprecated private static final String STRING_GENERATION_OUTPUT_DIR = "generationoutputdir";
	private static final String STRING_ACTIVE_DIAGRAM = "activediagram";
	public static final int TOKEN_ARGO = 1;
	public static final int TOKEN_AUTHORNAME = 2;
	public static final int TOKEN_AUTHOREMAIL = 3;
	public static final int TOKEN_VERSION = 4;
	public static final int TOKEN_DESCRIPTION = 5;
	public static final int TOKEN_SEARCHPATH = 6;
	public static final int TOKEN_MEMBER = 7;
	public static final int TOKEN_HISTORYFILE = 8;
	public static final int TOKEN_DOCUMENTATION = 9;
	public static final int TOKEN_SETTINGS = 10;
	public static final int TOKEN_NOTATIONLANGUAGE = 11;
	public static final int TOKEN_USEGUILLEMOTS = 12;
	public static final int TOKEN_SHOWVISIBILITY = 13;
	public static final int TOKEN_SHOWMULTIPLICITY = 14;
	public static final int TOKEN_SHOWINITIALVALUE = 15;
	public static final int TOKEN_SHOWPROPERTIES = 16;
	public static final int TOKEN_SHOWTYPES = 17;
	public static final int TOKEN_SHOWSTEREOTYPES = 18;
	public static final int TOKEN_DEFAULTSHADOWWIDTH = 19;
	public static final int TOKEN_SHOWBOLDNAMES = 20;
	public static final int TOKEN_FONTNAME = 21;
	public static final int TOKEN_FONTSIZE = 22;
	@Deprecated public static final int TOKEN_GENERATION_OUTPUT_DIR = 23;
	public static final int TOKEN_SHOWASSOCIATIONNAMES = 24;
	public static final int TOKEN_ACTIVE_DIAGRAM = 25;
	public static final int TOKEN_SHOWSINGULARMULTIPLICITIES = 26;
	public static final int TOKEN_HIDEBIDIRECTIONALARROWS = 27;
	public static final int TOKEN_UNDEFINED = 99;
	protected void setupTokens() {
		addToken(STRING_ARGO,Integer.valueOf(TOKEN_ARGO));
		addToken(STRING_AUTHORNAME,Integer.valueOf(TOKEN_AUTHORNAME));
		addToken(STRING_AUTHOREMAIL,Integer.valueOf(TOKEN_AUTHOREMAIL));
		addToken(STRING_VERSION,Integer.valueOf(TOKEN_VERSION));
		addToken(STRING_DESCRIPTION,Integer.valueOf(TOKEN_DESCRIPTION));
		addToken(STRING_SEARCHPATH,Integer.valueOf(TOKEN_SEARCHPATH));
		addToken(STRING_MEMBER,Integer.valueOf(TOKEN_MEMBER));
		addToken(STRING_HISTORYFILE,Integer.valueOf(TOKEN_HISTORYFILE));
		addToken(STRING_DOCUMENTATION,Integer.valueOf(TOKEN_DOCUMENTATION));
		addToken(STRING_SETTINGS,Integer.valueOf(TOKEN_SETTINGS));
		addToken(STRING_NOTATIONLANGUAGE,Integer.valueOf(TOKEN_NOTATIONLANGUAGE));
		addToken(STRING_SHOWBOLDNAMES,Integer.valueOf(TOKEN_SHOWBOLDNAMES));
		addToken(STRING_USEGUILLEMOTS,Integer.valueOf(TOKEN_USEGUILLEMOTS));
		addToken(STRING_SHOWVISIBILITY,Integer.valueOf(TOKEN_SHOWVISIBILITY));
		addToken(STRING_SHOWMULTIPLICITY,Integer.valueOf(TOKEN_SHOWMULTIPLICITY));
		addToken(STRING_HIDEBIDIRECTIONALARROWS,Integer.valueOf(TOKEN_HIDEBIDIRECTIONALARROWS));
		addToken(STRING_SHOWINITIALVALUE,Integer.valueOf(TOKEN_SHOWINITIALVALUE));
		addToken(STRING_SHOWPROPERTIES,Integer.valueOf(TOKEN_SHOWPROPERTIES));
		addToken(STRING_SHOWTYPES,Integer.valueOf(TOKEN_SHOWTYPES));
		addToken(STRING_SHOWSTEREOTYPES,Integer.valueOf(TOKEN_SHOWSTEREOTYPES));
		addToken(STRING_SHOWSINGULARMULTIPLICITIES,Integer.valueOf(TOKEN_SHOWSINGULARMULTIPLICITIES));
		addToken(STRING_DEFAULTSHADOWWIDTH,Integer.valueOf(TOKEN_DEFAULTSHADOWWIDTH));
		addToken(STRING_FONTNAME,Integer.valueOf(TOKEN_FONTNAME));
		addToken(STRING_FONTSIZE,Integer.valueOf(TOKEN_FONTSIZE));
		addToken(STRING_GENERATION_OUTPUT_DIR,Integer.valueOf(TOKEN_GENERATION_OUTPUT_DIR));
		addToken(STRING_SHOWASSOCIATIONNAMES,Integer.valueOf(TOKEN_SHOWASSOCIATIONNAMES));
		addToken(STRING_ACTIVE_DIAGRAM,Integer.valueOf(TOKEN_ACTIVE_DIAGRAM));
	}
}



