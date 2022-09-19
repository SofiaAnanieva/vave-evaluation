package org.argouml.notation.providers.uml;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ProjectSettings;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.AttributeNotation;
import org.argouml.uml.StereotypeUtility;
import org.argouml.util.MyTokenizer;
import org.argouml.notation.providers.uml.NotationUtilityUml;


public class AttributeNotationUml extends AttributeNotation {
	private static final AttributeNotationUml INSTANCE = new AttributeNotationUml();
	public static final AttributeNotationUml getInstance() {
		return INSTANCE;
	}
	protected AttributeNotationUml() {
		super();
	}
	public void parse(Object modelElement,String text) {
		try {
			parseAttributeFig(Model.getFacade().getOwner(modelElement),modelElement,text);
		}catch (ParseException pe) {
			String msg = "statusmsg.bar.error.parsing.attribute";
			Object[]args =  {pe.getLocalizedMessage(),Integer.valueOf(pe.getErrorOffset())};
			ArgoEventPump.fireEvent(new ArgoHelpEvent(ArgoEventTypes.HELP_CHANGED,this,Translator.messageFormat(msg,args)));
		}
	}
	public void parseAttributeFig(Object classifier,Object attribute,String text)throws ParseException {
		if (classifier == null||attribute == null) {
			return;
		}
		Project project = ProjectManager.getManager().getCurrentProject();
		ParseException pex = null;
		int start = 0;
		int end = NotationUtilityUml.indexOfNextCheckedSemicolon(text,start);
		if (end == -1) {
			project.moveToTrash(attribute);
			return;
		}
		String s = text.substring(start,end).trim();
		if (s.length() == 0) {
			project.moveToTrash(attribute);
			return;
		}
		parseAttribute(s,attribute);
		int i = Model.getFacade().getFeatures(classifier).indexOf(attribute);
		start = end + 1;
		end = NotationUtilityUml.indexOfNextCheckedSemicolon(text,start);
		while (end > start&&end <= text.length()) {
			s = text.substring(start,end).trim();
			if (s.length() > 0) {
				Object attrType = project.getDefaultAttributeType();
				Object newAttribute = Model.getUmlFactory().buildNode(Model.getMetaTypes().getAttribute());
				Model.getCoreHelper().setType(newAttribute,attrType);
				if (newAttribute != null) {
					if (i != -1) {
						Model.getCoreHelper().addFeature(classifier,++i,newAttribute);
					}else {
						Model.getCoreHelper().addFeature(classifier,newAttribute);
					}
					try {
						parseAttribute(s,newAttribute);
						Model.getCoreHelper().setStatic(newAttribute,Model.getFacade().isStatic(attribute));
					}catch (ParseException ex) {
						if (pex == null) {
							pex = ex;
						}
					}
				}
			}
			start = end + 1;
			end = NotationUtilityUml.indexOfNextCheckedSemicolon(text,start);
		}
		if (pex != null) {
			throw pex;
		}
	}
	protected void parseAttribute(String text,Object attribute)throws ParseException {
		StringBuilder multiplicity = null;
		String name = null;
		List<String>properties = null;
		StringBuilder stereotype = null;
		String token;
		String type = null;
		StringBuilder value = null;
		String visibility = null;
		boolean hasColon = false;
		boolean hasEq = false;
		int multindex = -1;
		MyTokenizer st;
		text = text.trim();
		if (text.length() > 0&&NotationUtilityUml.VISIBILITYCHARS.indexOf(text.charAt(0)) >= 0) {
			visibility = text.substring(0,1);
			text = text.substring(1);
		}
		try {
			st = new MyTokenizer(text," ,\t,<<,«,»,>>,[,],:,=,{,},\\,",NotationUtilityUml.attributeCustomSep);
			while (st.hasMoreTokens()) {
				token = st.nextToken();
				if (" ".equals(token)||"\t".equals(token)||",".equals(token)) {
					if (hasEq) {
						value.append(token);
					}
				}else if ("<<".equals(token)||"«".equals(token)) {
					if (hasEq) {
						value.append(token);
					}else {
						if (stereotype != null) {
							String msg = "parsing.error.attribute.two-sets-stereotypes";
							throw new ParseException(Translator.localize(msg),st.getTokenIndex());
						}
						stereotype = new StringBuilder();
						while (true) {
							token = st.nextToken();
							if (">>".equals(token)||"»".equals(token)) {
								break;
							}
							stereotype.append(token);
						}
					}
				}else if ("[".equals(token)) {
					if (hasEq) {
						value.append(token);
					}else {
						if (multiplicity != null) {
							String msg = "parsing.error.attribute.two-multiplicities";
							throw new ParseException(Translator.localize(msg),st.getTokenIndex());
						}
						multiplicity = new StringBuilder();
						multindex = st.getTokenIndex() + 1;
						while (true) {
							token = st.nextToken();
							if ("]".equals(token)) {
								break;
							}
							multiplicity.append(token);
						}
					}
				}else if ("{".equals(token)) {
					StringBuilder propname = new StringBuilder();
					String propvalue = null;
					if (properties == null) {
						properties = new ArrayList<String>();
					}
					while (true) {
						token = st.nextToken();
						if (",".equals(token)||"}".equals(token)) {
							if (propname.length() > 0) {
								properties.add(propname.toString());
								properties.add(propvalue);
							}
							propname = new StringBuilder();
							propvalue = null;
							if ("}".equals(token)) {
								break;
							}
						}else if ("=".equals(token)) {
							if (propvalue != null) {
								String msg = "parsing.error.attribute.prop-two-values";
								Object[]args =  {propvalue};
								throw new ParseException(Translator.localize(msg,args),st.getTokenIndex());
							}
							propvalue = "";
						}else if (propvalue == null) {
							propname.append(token);
						}else {
							propvalue += token;
						}
					}
					if (propname.length() > 0) {
						properties.add(propname.toString());
						properties.add(propvalue);
					}
				}else if (":".equals(token)) {
					hasColon = true;
					hasEq = false;
				}else if ("=".equals(token)) {
					if (value != null) {
						String msg = "parsing.error.attribute.two-default-values";
						throw new ParseException(Translator.localize(msg),st.getTokenIndex());
					}
					value = new StringBuilder();
					hasColon = false;
					hasEq = true;
				}else {
					if (hasColon) {
						if (type != null) {
							String msg = "parsing.error.attribute.two-types";
							throw new ParseException(Translator.localize(msg),st.getTokenIndex());
						}
						if (token.length() > 0&&(token.charAt(0) == '\"'||token.charAt(0) == '\'')) {
							String msg = "parsing.error.attribute.quoted";
							throw new ParseException(Translator.localize(msg),st.getTokenIndex());
						}
						if (token.length() > 0&&token.charAt(0) == '(') {
							String msg = "parsing.error.attribute.is-expr";
							throw new ParseException(Translator.localize(msg),st.getTokenIndex());
						}
						type = token;
					}else if (hasEq) {
						value.append(token);
					}else {
						if (name != null&&visibility != null) {
							String msg = "parsing.error.attribute.extra-text";
							throw new ParseException(Translator.localize(msg),st.getTokenIndex());
						}
						if (token.length() > 0&&(token.charAt(0) == '\"'||token.charAt(0) == '\'')) {
							String msg = "parsing.error.attribute.name-quoted";
							throw new ParseException(Translator.localize(msg),st.getTokenIndex());
						}
						if (token.length() > 0&&token.charAt(0) == '(') {
							String msg = "parsing.error.attribute.name-expr";
							throw new ParseException(Translator.localize(msg),st.getTokenIndex());
						}
						if (name == null&&visibility == null&&token.length() > 1&&NotationUtilityUml.VISIBILITYCHARS.indexOf(token.charAt(0)) >= 0) {
							visibility = token.substring(0,1);
							token = token.substring(1);
						}
						if (name != null) {
							visibility = name;
							name = token;
						}else {
							name = token;
						}
					}
				}
			}
		}catch (NoSuchElementException nsee) {
			String msg = "parsing.error.attribute.unexpected-end-attribute";
			throw new ParseException(Translator.localize(msg),text.length());
		}
		dealWithVisibility(attribute,visibility);
		dealWithName(attribute,name);
		dealWithType(attribute,type);
		dealWithValue(attribute,value);
		dealWithMultiplicity(attribute,multiplicity,multindex);
		dealWithProperties(attribute,properties);
		StereotypeUtility.dealWithStereotypes(attribute,stereotype,true);
	}
	private void dealWithProperties(Object attribute,List<String>properties) {
		if (properties != null) {
			NotationUtilityUml.setProperties(attribute,properties,NotationUtilityUml.attributeSpecialStrings);
		}
	}
	private void dealWithMultiplicity(Object attribute,StringBuilder multiplicity,int multindex)throws ParseException {
		if (multiplicity != null) {
			try {
				Model.getCoreHelper().setMultiplicity(attribute,Model.getDataTypesFactory().createMultiplicity(multiplicity.toString().trim()));
			}catch (IllegalArgumentException iae) {
				String msg = "parsing.error.attribute.bad-multiplicity";
				Object[]args =  {iae};
				throw new ParseException(Translator.localize(msg,args),multindex);
			}
		}
	}
	private void dealWithValue(Object attribute,StringBuilder value) {
		if (value != null) {
			Project project = ProjectManager.getManager().getCurrentProject();
			ProjectSettings ps = project.getProjectSettings();
			Object initExpr = Model.getDataTypesFactory().createExpression(ps.getNotationLanguage(),value.toString().trim());
			Model.getCoreHelper().setInitialValue(attribute,initExpr);
		}
	}
	private void dealWithType(Object attribute,String type) {
		if (type != null) {
			Object ow = Model.getFacade().getOwner(attribute);
			Object ns = null;
			if (ow != null&&Model.getFacade().getNamespace(ow) != null) {
				ns = Model.getFacade().getNamespace(ow);
			}else {
				ns = Model.getFacade().getModel(attribute);
			}
			Model.getCoreHelper().setType(attribute,NotationUtilityUml.getType(type.trim(),ns));
		}
	}
	private void dealWithName(Object attribute,String name) {
		if (name != null) {
			Model.getCoreHelper().setName(attribute,name.trim());
		}else if (Model.getFacade().getName(attribute) == null||"".equals(Model.getFacade().getName(attribute))) {
			Model.getCoreHelper().setName(attribute,"anonymous");
		}
	}
	private void dealWithVisibility(Object attribute,String visibility) {
		if (visibility != null) {
			Model.getCoreHelper().setVisibility(attribute,NotationUtilityUml.getVisibility(visibility.trim()));
		}
	}
	public String getParsingHelp() {
		return"parsing.help.attribute";
	}
	@Override public String toString(Object modelElement,NotationSettings settings) {
		return toString(modelElement,settings.isUseGuillemets(),settings.isShowVisibilities(),settings.isShowMultiplicities(),settings.isShowTypes(),settings.isShowInitialValues(),settings.isShowProperties());
	}
	@SuppressWarnings("deprecation")@Deprecated public String toString(Object modelElement,Map args) {
		Project p = ProjectManager.getManager().getCurrentProject();
		ProjectSettings ps = p.getProjectSettings();
		return toString(modelElement,ps.getUseGuillemotsValue(),ps.getShowVisibilityValue(),ps.getShowMultiplicityValue(),ps.getShowTypesValue(),ps.getShowInitialValueValue(),ps.getShowPropertiesValue());
	}
	private String toString(Object modelElement,boolean useGuillemets,boolean showVisibility,boolean showMultiplicity,boolean showTypes,boolean showInitialValues,boolean showProperties) {
		try {
			String stereo = NotationUtilityUml.generateStereotype(modelElement,useGuillemets);
			String name = Model.getFacade().getName(modelElement);
			String multiplicity = generateMultiplicity(Model.getFacade().getMultiplicity(modelElement));
			String type = "";
			if (Model.getFacade().getType(modelElement) != null) {
				type = Model.getFacade().getName(Model.getFacade().getType(modelElement));
			}
			StringBuilder sb = new StringBuilder(20);
			if ((stereo != null)&&(stereo.length() > 0)) {
				sb.append(stereo).append(" ");
			}
			if (showVisibility) {
				String visibility = NotationUtilityUml.generateVisibility2(modelElement);
				if (visibility != null&&visibility.length() > 0) {
					sb.append(visibility);
				}
			}
			if ((name != null)&&(name.length() > 0)) {
				sb.append(name).append(" ");
			}
			if ((multiplicity != null)&&(multiplicity.length() > 0)&&showMultiplicity) {
				sb.append("[").append(multiplicity).append("]").append(" ");
			}
			if ((type != null)&&(type.length() > 0)&&showTypes) {
				sb.append(": ").append(type).append(" ");
			}
			if (showInitialValues) {
				Object iv = Model.getFacade().getInitialValue(modelElement);
				if (iv != null) {
					String initialValue = (String) Model.getFacade().getBody(iv);
					if (initialValue != null&&initialValue.length() > 0) {
						sb.append(" = ").append(initialValue).append(" ");
					}
				}
			}
			if (showProperties) {
				String changeableKind = "";
				if (Model.getFacade().isReadOnly(modelElement)) {
					changeableKind = "frozen";
				}
				if (Model.getFacade().getChangeability(modelElement) != null) {
					if (Model.getChangeableKind().getAddOnly().equals(Model.getFacade().getChangeability(modelElement))) {
						changeableKind = "addOnly";
					}
				}
				StringBuilder properties = new StringBuilder();
				if (changeableKind.length() > 0) {
					properties.append("{ ").append(changeableKind).append(" }");
				}
				if (properties.length() > 0) {
					sb.append(properties);
				}
			}
			return sb.toString().trim();
		}catch (InvalidElementException e) {
			return"";
		}
	}
	private static String generateMultiplicity(Object m) {
		if (m == null||"1".equals(Model.getFacade().toString(m))) {
			return"";
		}
		return Model.getFacade().toString(m);
	}
}



