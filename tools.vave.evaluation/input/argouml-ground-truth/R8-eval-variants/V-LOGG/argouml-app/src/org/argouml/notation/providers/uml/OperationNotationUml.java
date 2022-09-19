package org.argouml.notation.providers.uml;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
import org.argouml.notation.providers.OperationNotation;
import org.argouml.uml.StereotypeUtility;
import org.argouml.util.MyTokenizer;
import org.argouml.notation.providers.uml.NotationUtilityUml;


public class OperationNotationUml extends OperationNotation {
	private static final String RECEPTION_KEYWORD = "signal";
	public OperationNotationUml(Object operation) {
		super(operation);
	}
	public void parse(Object modelElement,String text) {
		try {
			parseOperationFig(Model.getFacade().getOwner(modelElement),modelElement,text);
		}catch (ParseException pe) {
			String msg = "statusmsg.bar.error.parsing.operation";
			Object[]args =  {pe.getLocalizedMessage(),Integer.valueOf(pe.getErrorOffset())};
			ArgoEventPump.fireEvent(new ArgoHelpEvent(ArgoEventTypes.HELP_CHANGED,this,Translator.messageFormat(msg,args)));
		}
	}
	public void parseOperationFig(Object classifier,Object operation,String text)throws ParseException {
		if (classifier == null||operation == null) {
			return;
		}
		ParseException pex = null;
		int start = 0;
		int end = NotationUtilityUml.indexOfNextCheckedSemicolon(text,start);
		Project currentProject = ProjectManager.getManager().getCurrentProject();
		if (end == -1) {
			currentProject.moveToTrash(operation);
			return;
		}
		String s = text.substring(start,end).trim();
		if (s.length() == 0) {
			currentProject.moveToTrash(operation);
			return;
		}
		parseOperation(s,operation);
		int i = Model.getFacade().getFeatures(classifier).indexOf(operation);
		start = end + 1;
		end = NotationUtilityUml.indexOfNextCheckedSemicolon(text,start);
		while (end > start&&end <= text.length()) {
			s = text.substring(start,end).trim();
			if (s.length() > 0) {
				Object returnType = currentProject.getDefaultReturnType();
				Object newOp = Model.getCoreFactory().buildOperation(classifier,returnType);
				if (newOp != null) {
					try {
						parseOperation(s,newOp);
						if (i != -1) {
							Model.getCoreHelper().addFeature(classifier,++i,newOp);
						}else {
							Model.getCoreHelper().addFeature(classifier,newOp);
						}
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
	public void parseOperation(String s,Object op)throws ParseException {
		MyTokenizer st;
		boolean hasColon = false;
		String name = null;
		String parameterlist = null;
		StringBuilder stereotype = null;
		String token;
		String type = null;
		String visibility = null;
		List<String>properties = null;
		int paramOffset = 0;
		s = s.trim();
		if (s.length() > 0&&NotationUtilityUml.VISIBILITYCHARS.indexOf(s.charAt(0)) >= 0) {
			visibility = s.substring(0,1);
			s = s.substring(1);
		}
		try {
			st = new MyTokenizer(s," ,\t,<<,«,»,>>,:,=,{,},\\,",NotationUtilityUml.operationCustomSep);
			while (st.hasMoreTokens()) {
				token = st.nextToken();
				if (" ".equals(token)||"\t".equals(token)||",".equals(token)) {
					continue;
				}else if ("<<".equals(token)||"«".equals(token)) {
					if (stereotype != null) {
						parseError("operation.stereotypes",st.getTokenIndex());
					}
					stereotype = new StringBuilder();
					while (true) {
						token = st.nextToken();
						if (">>".equals(token)||"»".equals(token)) {
							break;
						}
						stereotype.append(token);
					}
				}else if ("{".equals(token)) {
					properties = tokenOpenBrace(st,properties);
				}else if (":".equals(token)) {
					hasColon = true;
				}else if ("=".equals(token)) {
					parseError("operation.default-values",st.getTokenIndex());
				}else if (token.charAt(0) == '('&&!hasColon) {
					if (parameterlist != null) {
						parseError("operation.two-parameter-lists",st.getTokenIndex());
					}
					parameterlist = token;
				}else {
					if (hasColon) {
						if (type != null) {
							parseError("operation.two-types",st.getTokenIndex());
						}
						if (token.length() > 0&&(token.charAt(0) == '\"'||token.charAt(0) == '\'')) {
							parseError("operation.type-quoted",st.getTokenIndex());
						}
						if (token.length() > 0&&token.charAt(0) == '(') {
							parseError("operation.type-expr",st.getTokenIndex());
						}
						type = token;
					}else {
						if (name != null&&visibility != null) {
							parseError("operation.extra-text",st.getTokenIndex());
						}
						if (token.length() > 0&&(token.charAt(0) == '\"'||token.charAt(0) == '\'')) {
							parseError("operation.name-quoted",st.getTokenIndex());
						}
						if (token.length() > 0&&token.charAt(0) == '(') {
							parseError("operation.name-expr",st.getTokenIndex());
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
			parseError("operation.unexpected-end-operation",s.length());
		}catch (ParseException pre) {
			throw pre;
		}
		if (parameterlist != null) {
			if (parameterlist.charAt(parameterlist.length() - 1) != ')') {
				parseError("operation.parameter-list-incomplete",paramOffset + parameterlist.length() - 1);
			}
			paramOffset++;
			parameterlist = parameterlist.substring(1,parameterlist.length() - 1);
			NotationUtilityUml.parseParamList(op,parameterlist,paramOffset);
		}
		if (visibility != null) {
			Model.getCoreHelper().setVisibility(op,NotationUtilityUml.getVisibility(visibility.trim()));
		}
		if (name != null) {
			Model.getCoreHelper().setName(op,name.trim());
		}else if (Model.getFacade().getName(op) == null||"".equals(Model.getFacade().getName(op))) {
			Model.getCoreHelper().setName(op,"anonymous");
		}
		if (type != null) {
			Object ow = Model.getFacade().getOwner(op);
			Object ns = null;
			if (ow != null&&Model.getFacade().getNamespace(ow) != null) {
				ns = Model.getFacade().getNamespace(ow);
			}else {
				ns = Model.getFacade().getModel(op);
			}
			Object mtype = NotationUtilityUml.getType(type.trim(),ns);
			setReturnParameter(op,mtype);
		}
		if (properties != null) {
			NotationUtilityUml.setProperties(op,properties,NotationUtilityUml.operationSpecialStrings);
		}
		if (!Model.getFacade().isAReception(op)||!RECEPTION_KEYWORD.equals(stereotype.toString())) {
			StereotypeUtility.dealWithStereotypes(op,stereotype,true);
		}
	}
	private void parseError(String message,int offset)throws ParseException {
		throw new ParseException(Translator.localize("parsing.error." + message),offset);
	}
	private List<String>tokenOpenBrace(MyTokenizer st,List<String>properties)throws ParseException {
		String token;
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
					String msg = "parsing.error.operation.prop-stereotypes";
					Object[]args =  {propname};
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
		return properties;
	}
	private void setReturnParameter(Object op,Object type) {
		Object param = null;
		Iterator it = Model.getFacade().getParameters(op).iterator();
		while (it.hasNext()) {
			Object p = it.next();
			if (Model.getFacade().isReturn(p)) {
				param = p;
				break;
			}
		}
		while (it.hasNext()) {
			Object p = it.next();
			if (Model.getFacade().isReturn(p)) {
				ProjectManager.getManager().getCurrentProject().moveToTrash(p);
			}
		}
		if (param == null) {
			Object returnType = ProjectManager.getManager().getCurrentProject().getDefaultReturnType();
			param = Model.getCoreFactory().buildParameter(op,returnType);
		}
		Model.getCoreHelper().setType(param,type);
	}
	public String getParsingHelp() {
		return"parsing.help.operation";
	}
	public String toString(Object modelElement,NotationSettings settings) {
		return toString(modelElement,settings.isUseGuillemets(),settings.isShowVisibilities(),settings.isShowTypes(),settings.isShowProperties());
	}
	@SuppressWarnings("deprecation")@Deprecated public String toString(Object modelElement,Map args) {
		Project p = ProjectManager.getManager().getCurrentProject();
		ProjectSettings ps = p.getProjectSettings();
		return toString(modelElement,ps.getUseGuillemotsValue(),ps.getShowVisibilityValue(),ps.getShowTypesValue(),ps.getShowPropertiesValue());
	}
	private String toString(Object modelElement,boolean useGuillemets,boolean showVisibility,boolean showTypes,boolean showProperties) {
		try {
			String stereoStr = NotationUtilityUml.generateStereotype(Model.getFacade().getStereotypes(modelElement),useGuillemets);
			boolean isReception = Model.getFacade().isAReception(modelElement);
			if (isReception) {
				stereoStr = NotationUtilityUml.generateStereotype(RECEPTION_KEYWORD,useGuillemets) + " " + stereoStr;
			}
			StringBuffer genStr = new StringBuffer(30);
			if ((stereoStr != null)&&(stereoStr.length() > 0)) {
				genStr.append(stereoStr).append(" ");
			}
			if (showVisibility) {
				String visStr = NotationUtilityUml.generateVisibility2(modelElement);
				if (visStr != null) {
					genStr.append(visStr);
				}
			}
			String nameStr = Model.getFacade().getName(modelElement);
			if ((nameStr != null)&&(nameStr.length() > 0)) {
				genStr.append(nameStr);
			}
			if (showTypes) {
				StringBuffer parameterStr = new StringBuffer();
				parameterStr.append("(").append(getParameterList(modelElement)).append(")");
				StringBuffer returnParasSb = getReturnParameters(modelElement,isReception);
				genStr.append(parameterStr).append(" ");
				if ((returnParasSb != null)&&(returnParasSb.length() > 0)) {
					genStr.append(returnParasSb).append(" ");
				}
			}else {
				genStr.append("()");
			}
			if (showProperties) {
				StringBuffer propertySb = getProperties(modelElement,isReception);
				if (propertySb.length() > 0) {
					genStr.append(propertySb);
				}
			}
			return genStr.toString().trim();
		}catch (InvalidElementException e) {
			return"";
		}
	}
	private StringBuffer getParameterList(Object modelElement) {
		StringBuffer parameterListBuffer = new StringBuffer();
		Collection coll = Model.getFacade().getParameters(modelElement);
		Iterator it = coll.iterator();
		int counter = 0;
		while (it.hasNext()) {
			Object parameter = it.next();
			if (!Model.getFacade().hasReturnParameterDirectionKind(parameter)) {
				counter++;
				parameterListBuffer.append(NotationUtilityUml.generateParameter(parameter));
				parameterListBuffer.append(",");
			}
		}
		if (counter > 0) {
			parameterListBuffer.delete(parameterListBuffer.length() - 1,parameterListBuffer.length());
		}
		return parameterListBuffer;
	}
	private StringBuffer getReturnParameters(Object modelElement,boolean isReception) {
		StringBuffer returnParasSb = new StringBuffer();
		if (!isReception) {
			Collection coll = Model.getCoreHelper().getReturnParameters(modelElement);
			if (coll != null&&coll.size() > 0) {
				returnParasSb.append(": ");
				Iterator it2 = coll.iterator();
				while (it2.hasNext()) {
					Object type = Model.getFacade().getType(it2.next());
					if (type != null) {
						returnParasSb.append(Model.getFacade().getName(type));
					}
					returnParasSb.append(",");
				}
				if (returnParasSb.length() == 3) {
					returnParasSb.delete(0,returnParasSb.length());
				}else {
					returnParasSb.delete(returnParasSb.length() - 1,returnParasSb.length());
				}
			}
		}
		return returnParasSb;
	}
	private StringBuffer getProperties(Object modelElement,boolean isReception) {
		StringBuffer propertySb = new StringBuffer().append("{");
		if (Model.getFacade().isQuery(modelElement)) {
			propertySb.append("query,");
		}
		if (Model.getFacade().isRoot(modelElement)) {
			propertySb.append("root,");
		}
		if (Model.getFacade().isLeaf(modelElement)) {
			propertySb.append("leaf,");
		}
		if (!isReception) {
			if (Model.getFacade().getConcurrency(modelElement) != null) {
				propertySb.append(Model.getFacade().getName(Model.getFacade().getConcurrency(modelElement)));
				propertySb.append(',');
			}
		}
		if (propertySb.length() > 1) {
			propertySb.delete(propertySb.length() - 1,propertySb.length());
			propertySb.append("}");
		}else {
			propertySb = new StringBuffer();
		}
		return propertySb;
	}
	private StringBuffer getTaggedValues(Object modelElement) {
		StringBuffer taggedValuesSb = new StringBuffer();
		Iterator it3 = Model.getFacade().getTaggedValues(modelElement);
		if (it3 != null&&it3.hasNext()) {
			while (it3.hasNext()) {
				taggedValuesSb.append(NotationUtilityUml.generateTaggedValue(it3.next()));
				taggedValuesSb.append(",");
			}
			taggedValuesSb.delete(taggedValuesSb.length() - 1,taggedValuesSb.length());
		}
		return taggedValuesSb;
	}
}



