package org.argouml.persistence;


class TodoTokenTable extends XMLTokenTableBase {
	private static final String STRING_TO_DO = "todo";
	private static final String STRING_TO_DO_LIST = "todolist";
	private static final String STRING_TO_DO_ITEM = "todoitem";
	private static final String STRING_HEADLINE = "headline";
	private static final String STRING_DESCRIPTION = "description";
	private static final String STRING_PRIORITY = "priority";
	private static final String STRING_MOREINFOURL = "moreinfourl";
	private static final String STRING_RESOLVEDCRITICS = "resolvedcritics";
	private static final String STRING_ISSUE = "issue";
	private static final String STRING_POSTER = "poster";
	private static final String STRING_OFFENDER = "offender";
	public static final String STRING_PRIO_HIGH = "high";
	public static final String STRING_PRIO_MED = "medium";
	public static final String STRING_PRIO_LOW = "low";
	public static final int TOKEN_TO_DO = 1;
	public static final int TOKEN_TO_DO_LIST = 2;
	public static final int TOKEN_TO_DO_ITEM = 3;
	public static final int TOKEN_HEADLINE = 4;
	public static final int TOKEN_DESCRIPTION = 5;
	public static final int TOKEN_PRIORITY = 6;
	public static final int TOKEN_MOREINFOURL = 7;
	public static final int TOKEN_RESOLVEDCRITICS = 8;
	public static final int TOKEN_ISSUE = 9;
	public static final int TOKEN_POSTER = 10;
	public static final int TOKEN_OFFENDER = 11;
	public static final int TOKEN_UNDEFINED = 12;
	public TodoTokenTable() {
		super(32);
	}
	protected void setupTokens() {
		addToken(STRING_TO_DO,Integer.valueOf(TOKEN_TO_DO));
		addToken(STRING_TO_DO_LIST,Integer.valueOf(TOKEN_TO_DO_LIST));
		addToken(STRING_TO_DO_ITEM,Integer.valueOf(TOKEN_TO_DO_ITEM));
		addToken(STRING_HEADLINE,Integer.valueOf(TOKEN_HEADLINE));
		addToken(STRING_DESCRIPTION,Integer.valueOf(TOKEN_DESCRIPTION));
		addToken(STRING_PRIORITY,Integer.valueOf(TOKEN_PRIORITY));
		addToken(STRING_MOREINFOURL,Integer.valueOf(TOKEN_MOREINFOURL));
		addToken(STRING_RESOLVEDCRITICS,Integer.valueOf(TOKEN_RESOLVEDCRITICS));
		addToken(STRING_ISSUE,Integer.valueOf(TOKEN_ISSUE));
		addToken(STRING_POSTER,Integer.valueOf(TOKEN_POSTER));
		addToken(STRING_OFFENDER,Integer.valueOf(TOKEN_OFFENDER));
	}
}



