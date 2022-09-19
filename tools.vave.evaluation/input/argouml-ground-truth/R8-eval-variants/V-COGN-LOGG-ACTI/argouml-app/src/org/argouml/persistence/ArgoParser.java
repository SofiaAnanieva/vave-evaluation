package org.argouml.persistence;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectSettings;
import org.argouml.notation.NotationSettings;
import org.argouml.uml.diagram.DiagramSettings;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.argouml.persistence.ArgoTokenTable;


class ArgoParser extends SAXParserBase {
	private static final Logger LOG = Logger.getLogger(ArgoParser.class);
	private Project project;
	private ProjectSettings ps;
	private DiagramSettings diagramDefaults;
	private NotationSettings notationSettings;
	private ArgoTokenTable tokens = new ArgoTokenTable();
	private List<String>memberList = new ArrayList<String>();
	public ArgoParser() {
		super();
	}
	public void readProject(Project theProject,InputSource source)throws SAXException {
		if (source == null) {
			throw new IllegalArgumentException("An InputSource must be supplied");
		}
		preRead(theProject);
		try {
			parse(source);
		}catch (SAXException e) {
			logError(source.toString(),e);
			throw e;
		}
	}
	public void readProject(Project theProject,Reader reader)throws SAXException {
		if (reader == null) {
			throw new IllegalArgumentException("A reader must be supplied");
		}
		preRead(theProject);
		try {
			parse(reader);
		}catch (SAXException e) {
			logError(reader.toString(),e);
			throw e;
		}
	}
	private void preRead(Project theProject) {
		LOG.info("=======================================");
		LOG.info("== READING PROJECT " + theProject);
		project = theProject;
		ps = project.getProjectSettings();
		diagramDefaults = ps.getDefaultDiagramSettings();
		notationSettings = ps.getNotationSettings();
	}
	private void logError(String projectName,SAXException e) {
		LOG.error("Exception reading project================",e);
		LOG.error(projectName);
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project newProj) {
		project = newProj;
		ps = project.getProjectSettings();
	}
	public void handleStartElement(XMLElement e)throws SAXException {
		if (DBG) {
			LOG.debug("NOTE: ArgoParser handleStartTag:" + e.getName());
		}
		switch (tokens.toToken(e.getName(),true)) {case ArgoTokenTable.TOKEN_ARGO:
			handleArgo(e);
			break;
		case ArgoTokenTable.TOKEN_DOCUMENTATION:
			handleDocumentation(e);
			break;
		case ArgoTokenTable.TOKEN_SETTINGS:
			handleSettings(e);
			break;
		default:
			if (DBG) {
				LOG.warn("WARNING: unknown tag:" + e.getName());
			}
			break;
		}
	}
	@SuppressWarnings("deprecation")public void handleEndElement(XMLElement e)throws SAXException {
		if (DBG) {
			LOG.debug("NOTE: ArgoParser handleEndTag:" + e.getName() + ".");
		}
		switch (tokens.toToken(e.getName(),false)) {case ArgoTokenTable.TOKEN_MEMBER:
			handleMember(e);
			break;
		case ArgoTokenTable.TOKEN_AUTHORNAME:
			handleAuthorName(e);
			break;
		case ArgoTokenTable.TOKEN_AUTHOREMAIL:
			handleAuthorEmail(e);
			break;
		case ArgoTokenTable.TOKEN_VERSION:
			handleVersion(e);
			break;
		case ArgoTokenTable.TOKEN_DESCRIPTION:
			handleDescription(e);
			break;
		case ArgoTokenTable.TOKEN_SEARCHPATH:
			handleSearchpath(e);
			break;
		case ArgoTokenTable.TOKEN_HISTORYFILE:
			handleHistoryfile(e);
			break;
		case ArgoTokenTable.TOKEN_NOTATIONLANGUAGE:
			handleNotationLanguage(e);
			break;
		case ArgoTokenTable.TOKEN_SHOWBOLDNAMES:
			handleShowBoldNames(e);
			break;
		case ArgoTokenTable.TOKEN_USEGUILLEMOTS:
			handleUseGuillemots(e);
			break;
		case ArgoTokenTable.TOKEN_SHOWVISIBILITY:
			handleShowVisibility(e);
			break;
		case ArgoTokenTable.TOKEN_SHOWMULTIPLICITY:
			handleShowMultiplicity(e);
			break;
		case ArgoTokenTable.TOKEN_SHOWINITIALVALUE:
			handleShowInitialValue(e);
			break;
		case ArgoTokenTable.TOKEN_SHOWPROPERTIES:
			handleShowProperties(e);
			break;
		case ArgoTokenTable.TOKEN_SHOWTYPES:
			handleShowTypes(e);
			break;
		case ArgoTokenTable.TOKEN_SHOWSTEREOTYPES:
			handleShowStereotypes(e);
			break;
		case ArgoTokenTable.TOKEN_SHOWSINGULARMULTIPLICITIES:
			handleShowSingularMultiplicities(e);
			break;
		case ArgoTokenTable.TOKEN_DEFAULTSHADOWWIDTH:
			handleDefaultShadowWidth(e);
			break;
		case ArgoTokenTable.TOKEN_FONTNAME:
			handleFontName(e);
			break;
		case ArgoTokenTable.TOKEN_FONTSIZE:
			handleFontSize(e);
			break;
		case ArgoTokenTable.TOKEN_GENERATION_OUTPUT_DIR:
			break;
		case ArgoTokenTable.TOKEN_SHOWASSOCIATIONNAMES:
			handleShowAssociationNames(e);
			break;
		case ArgoTokenTable.TOKEN_HIDEBIDIRECTIONALARROWS:
			handleHideBidirectionalArrows(e);
			break;
		case ArgoTokenTable.TOKEN_ACTIVE_DIAGRAM:
			handleActiveDiagram(e);
			break;
		default:
			if (DBG) {
				LOG.warn("WARNING: unknown end tag:" + e.getName());
			}
			break;
		}
	}
	@Override protected boolean isElementOfInterest(String name) {
		return tokens.contains(name);
	}
	protected void handleArgo(@SuppressWarnings("unused")XMLElement e) {
	}
	protected void handleDocumentation(@SuppressWarnings("unused")XMLElement e) {
	}
	protected void handleSettings(@SuppressWarnings("unused")XMLElement e) {
	}
	protected void handleAuthorName(XMLElement e) {
		String authorname = e.getText().trim();
		project.setAuthorname(authorname);
	}
	protected void handleAuthorEmail(XMLElement e) {
		String authoremail = e.getText().trim();
		project.setAuthoremail(authoremail);
	}
	protected void handleVersion(XMLElement e) {
		String version = e.getText().trim();
		project.setVersion(version);
	}
	protected void handleDescription(XMLElement e) {
		String description = e.getText().trim();
		project.setDescription(description);
	}
	protected void handleSearchpath(XMLElement e) {
		String searchpath = e.getAttribute("href").trim();
		project.addSearchPath(searchpath);
	}
	protected void handleMember(XMLElement e)throws SAXException {
		if (e == null) {
			throw new SAXException("XML element is null");
		}
		String type = e.getAttribute("type");
		memberList.add(type);
	}
	protected void handleHistoryfile(XMLElement e) {
		if (e.getAttribute("name") == null) {
			return;
		}
		String historyfile = e.getAttribute("name").trim();
		project.setHistoryFile(historyfile);
	}
	protected void handleNotationLanguage(XMLElement e) {
		String language = e.getText().trim();
		boolean success = ps.setNotationLanguage(language);
	}
	protected void handleShowBoldNames(XMLElement e) {
		String ug = e.getText().trim();
		diagramDefaults.setShowBoldNames(Boolean.parseBoolean(ug));
	}
	protected void handleUseGuillemots(XMLElement e) {
		String ug = e.getText().trim();
		ps.setUseGuillemots(ug);
	}
	protected void handleShowVisibility(XMLElement e) {
		String showVisibility = e.getText().trim();
		notationSettings.setShowVisibilities(Boolean.parseBoolean(showVisibility));
	}
	protected void handleShowMultiplicity(XMLElement e) {
		String showMultiplicity = e.getText().trim();
		notationSettings.setShowMultiplicities(Boolean.parseBoolean(showMultiplicity));
	}
	protected void handleShowInitialValue(XMLElement e) {
		String showInitialValue = e.getText().trim();
		notationSettings.setShowInitialValues(Boolean.parseBoolean(showInitialValue));
	}
	protected void handleShowProperties(XMLElement e) {
		String showproperties = e.getText().trim();
		notationSettings.setShowProperties(Boolean.parseBoolean(showproperties));
	}
	protected void handleShowTypes(XMLElement e) {
		String showTypes = e.getText().trim();
		notationSettings.setShowTypes(Boolean.parseBoolean(showTypes));
	}
	protected void handleShowStereotypes(XMLElement e) {
		String showStereotypes = e.getText().trim();
		ps.setShowStereotypes(Boolean.parseBoolean(showStereotypes));
	}
	protected void handleShowSingularMultiplicities(XMLElement e) {
		String showSingularMultiplicities = e.getText().trim();
		notationSettings.setShowSingularMultiplicities(Boolean.parseBoolean(showSingularMultiplicities));
	}
	protected void handleDefaultShadowWidth(XMLElement e) {
		String dsw = e.getText().trim();
		diagramDefaults.setDefaultShadowWidth(Integer.parseInt(dsw));
	}
	protected void handleFontName(XMLElement e) {
		String dsw = e.getText().trim();
		diagramDefaults.setFontName(dsw);
	}
	protected void handleFontSize(XMLElement e) {
		String dsw = e.getText().trim();
		try {
			diagramDefaults.setFontSize(Integer.parseInt(dsw));
		}catch (NumberFormatException e1) {
			LOG.error("NumberFormatException while parsing Font Size",e1);
		}
	}
	protected void handleShowAssociationNames(XMLElement e) {
		String showAssociationNames = e.getText().trim();
		notationSettings.setShowAssociationNames(Boolean.parseBoolean(showAssociationNames));
	}
	protected void handleHideBidirectionalArrows(XMLElement e) {
		String hideBidirectionalArrows = e.getText().trim();
		diagramDefaults.setShowBidirectionalArrows(!Boolean.parseBoolean(hideBidirectionalArrows));
	}
	protected void handleActiveDiagram(XMLElement e) {
		project.setSavedDiagramName(e.getText().trim());
	}
	public List<String>getMemberList() {
		return memberList;
	}
}



