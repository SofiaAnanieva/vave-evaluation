package org.argouml.persistence;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ResolvedCritic;
import org.argouml.cognitive.ToDoItem;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.argouml.persistence.TodoTokenTable;


class TodoParser extends SAXParserBase {
	private static final Logger LOG = Logger.getLogger(TodoParser.class);
	private TodoTokenTable tokens = new TodoTokenTable();
	private String headline;
	private int priority;
	private String moreinfourl;
	private String description;
	private String critic;
	private List offenders;
	public TodoParser() {
	}
	public synchronized void readTodoList(InputSource inputSource)throws SAXException {
		LOG.info("Reading ToDo list");
		parse(inputSource);
	}
	public synchronized void readTodoList(Reader is)throws SAXException {
		LOG.info("=======================================");
		LOG.info("== READING TO DO LIST");
		parse(is);
	}
	public void handleStartElement(XMLElement e) {
		try {
			switch (tokens.toToken(e.getName(),true)) {case TodoTokenTable.TOKEN_HEADLINE:
			case TodoTokenTable.TOKEN_DESCRIPTION:
			case TodoTokenTable.TOKEN_PRIORITY:
			case TodoTokenTable.TOKEN_MOREINFOURL:
			case TodoTokenTable.TOKEN_POSTER:
			case TodoTokenTable.TOKEN_OFFENDER:
				break;
			case TodoTokenTable.TOKEN_TO_DO:
				handleTodo(e);
				break;
			case TodoTokenTable.TOKEN_TO_DO_LIST:
				handleTodoList(e);
				break;
			case TodoTokenTable.TOKEN_TO_DO_ITEM:
				handleTodoItemStart(e);
				break;
			case TodoTokenTable.TOKEN_RESOLVEDCRITICS:
				handleResolvedCritics(e);
				break;
			case TodoTokenTable.TOKEN_ISSUE:
				handleIssueStart(e);
				break;
			default:
				LOG.warn("WARNING: unknown tag:" + e.getName());
				break;
			}
		}catch (Exception ex) {
			LOG.error("Exception in startelement",ex);
		}
	}
	public void handleEndElement(XMLElement e)throws SAXException {
		try {
			switch (tokens.toToken(e.getName(),false)) {case TodoTokenTable.TOKEN_TO_DO:
			case TodoTokenTable.TOKEN_RESOLVEDCRITICS:
			case TodoTokenTable.TOKEN_TO_DO_LIST:
				break;
			case TodoTokenTable.TOKEN_TO_DO_ITEM:
				handleTodoItemEnd(e);
				break;
			case TodoTokenTable.TOKEN_HEADLINE:
				handleHeadline(e);
				break;
			case TodoTokenTable.TOKEN_DESCRIPTION:
				handleDescription(e);
				break;
			case TodoTokenTable.TOKEN_PRIORITY:
				handlePriority(e);
				break;
			case TodoTokenTable.TOKEN_MOREINFOURL:
				handleMoreInfoURL(e);
				break;
			case TodoTokenTable.TOKEN_ISSUE:
				handleIssueEnd(e);
				break;
			case TodoTokenTable.TOKEN_POSTER:
				handlePoster(e);
				break;
			case TodoTokenTable.TOKEN_OFFENDER:
				handleOffender(e);
				break;
			default:
				LOG.warn("WARNING: unknown end tag:" + e.getName());
				break;
			}
		}catch (Exception ex) {
			throw new SAXException(ex);
		}
	}
	protected void handleTodo(XMLElement e) {
	}
	protected void handleTodoList(XMLElement e) {
	}
	protected void handleResolvedCritics(XMLElement e) {
	}
	protected void handleTodoItemStart(XMLElement e) {
		headline = "";
		priority = ToDoItem.HIGH_PRIORITY;
		moreinfourl = "";
		description = "";
	}
	protected void handleTodoItemEnd(XMLElement e) {
		ToDoItem item;
		Designer dsgr;
		dsgr = Designer.theDesigner();
		item = new ToDoItem(dsgr,headline,priority,description,moreinfourl,new ListSet());
		dsgr.getToDoList().addElement(item);
	}
	protected void handleHeadline(XMLElement e) {
		headline = decode(e.getText()).trim();
	}
	protected void handlePriority(XMLElement e) {
		String prio = decode(e.getText()).trim();
		int np;
		try {
			np = Integer.parseInt(prio);
		}catch (NumberFormatException nfe) {
			np = ToDoItem.HIGH_PRIORITY;
			if (TodoTokenTable.STRING_PRIO_HIGH.equalsIgnoreCase(prio)) {
				np = ToDoItem.HIGH_PRIORITY;
			}else if (TodoTokenTable.STRING_PRIO_MED.equalsIgnoreCase(prio)) {
				np = ToDoItem.MED_PRIORITY;
			}else if (TodoTokenTable.STRING_PRIO_LOW.equalsIgnoreCase(prio)) {
				np = ToDoItem.LOW_PRIORITY;
			}
		}
		priority = np;
	}
	protected void handleMoreInfoURL(XMLElement e) {
		moreinfourl = decode(e.getText()).trim();
	}
	protected void handleDescription(XMLElement e) {
		description = decode(e.getText()).trim();
	}
	protected void handleIssueStart(XMLElement e) {
		critic = null;
		offenders = null;
	}
	protected void handleIssueEnd(XMLElement e) {
		Designer dsgr;
		ResolvedCritic item;
		if (critic == null) {
			return;
		}
		item = new ResolvedCritic(critic,offenders);
		dsgr = Designer.theDesigner();
		dsgr.getToDoList().addResolvedCritic(item);
	}
	protected void handlePoster(XMLElement e) {
		critic = decode(e.getText()).trim();
	}
	protected void handleOffender(XMLElement e) {
		if (offenders == null) {
			offenders = new ArrayList();
		}
		offenders.add(decode(e.getText()).trim());
	}
	public static String decode(String str) {
		if (str == null) {
			return null;
		}
		StringBuffer sb;
		int i1,i2;
		char c;
		sb = new StringBuffer();
		for (i1 = 0,i2 = 0;i2 < str.length();i2++) {
			c = str.charAt(i2);
			if (c == '%') {
				if (i2 > i1) {
					sb.append(str.substring(i1,i2));
				}
				for (i1 = ++ i2;i2 < str.length();i2++) {
					if (str.charAt(i2) == ';') {
						break;
					}
				}
				if (i2 >= str.length()) {
					i1 = i2;
					break;
				}
				if (i2 > i1) {
					String ent = str.substring(i1,i2);
					if ("proc".equals(ent)) {
						sb.append('%');
					}else {
						try {
							sb.append((char) Integer.parseInt(ent));
						}catch (NumberFormatException nfe) {
						}
					}
				}
				i1 = i2 + 1;
			}
		}
		if (i2 > i1) {
			sb.append(str.substring(i1,i2));
		}
		return sb.toString();
	}
	public static String encode(String str) {
		StringBuffer sb;
		int i1,i2;
		char c;
		if (str == null) {
			return null;
		}
		sb = new StringBuffer();
		for (i1 = 0,i2 = 0;i2 < str.length();i2++) {
			c = str.charAt(i2);
			if (c == '%') {
				if (i2 > i1) {
					sb.append(str.substring(i1,i2));
				}
				sb.append("%proc;");
				i1 = i2 + 1;
			}else if (c < 0x28||(c >= 0x3c&&c <= 0x40&&c != 0x3d&&c != 0x3f)||(c >= 0x5e&&c <= 0x60&&c != 0x5f)||c >= 0x7b) {
				if (i2 > i1) {
					sb.append(str.substring(i1,i2));
				}
				sb.append("%" + Integer.toString(c) + ";");
				i1 = i2 + 1;
			}
		}
		if (i2 > i1) {
			sb.append(str.substring(i1,i2));
		}
		return sb.toString();
	}
}



