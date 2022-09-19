package org.argouml.notation.providers.java;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoHelpEvent;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.ModelElementNameNotation;
import org.argouml.util.MyTokenizer;


public class ModelElementNameNotationJava extends ModelElementNameNotation {
	public ModelElementNameNotationJava(Object name) {
		super(name);
	}
	public void parse(Object modelElement,String text) {
		try {
			parseModelElement(modelElement,text);
		}catch (ParseException pe) {
			String msg = "statusmsg.bar.error.parsing.node-modelelement";
			Object[]args =  {pe.getLocalizedMessage(),Integer.valueOf(pe.getErrorOffset())};
			ArgoEventPump.fireEvent(new ArgoHelpEvent(ArgoEventTypes.HELP_CHANGED,this,Translator.messageFormat(msg,args)));
		}
	}
	static void parseModelElement(Object modelElement,String text)throws ParseException {
		MyTokenizer st;
		boolean abstrac = false;
		boolean fina = false;
		boolean publi = false;
		boolean privat = false;
		boolean protect = false;
		String token;
		List<String>path = null;
		String name = null;
		try {
			st = new MyTokenizer(text," ,.,abstract,final,public,private,protected");
			while (st.hasMoreTokens()) {
				token = st.nextToken();
				if (" ".equals(token)) {
				}else if ("abstract".equals(token)) {
					abstrac = true;
				}else if ("final".equals(token)) {
					fina = true;
				}else if ("public".equals(token)) {
					publi = true;
				}else if ("private".equals(token)) {
					privat = true;
				}else if ("protected".equals(token)) {
					protect = true;
				}else if (".".equals(token)) {
					if (name != null) {
						name = name.trim();
					}
					if (path != null&&(name == null||"".equals(name))) {
						String msg = "parsing.error.model-element-name.anon-qualifiers";
						throw new ParseException(Translator.localize(msg),st.getTokenIndex());
					}
					if (path == null) {
						path = new ArrayList<String>();
					}
					if (name != null) {
						path.add(name);
					}
					name = null;
				}else {
					if (name != null) {
						String msg = "parsing.error.model-element-name.twin-names";
						throw new ParseException(Translator.localize(msg),st.getTokenIndex());
					}
					name = token;
				}
			}
		}catch (NoSuchElementException nsee) {
			String msg = "parsing.error.model-element-name.unexpected-name-element";
			throw new ParseException(Translator.localize(msg),text.length());
		}catch (ParseException pre) {
			throw pre;
		}
		if (name != null) {
			name = name.trim();
		}
		if (path != null&&(name == null||"".equals(name))) {
			String msg = "parsing.error.model-element-name.must-end-with-name";
			throw new ParseException(Translator.localize(msg),0);
		}
		if (!isValidJavaClassName(name)) {
			throw new ParseException("Invalid class name for Java: " + name,0);
		}
		if (path != null) {
			Object nspe = Model.getModelManagementHelper().getElement(path,Model.getFacade().getModel(modelElement));
			if (nspe == null||!(Model.getFacade().isANamespace(nspe))) {
				String msg = "parsing.error.model-element-name.namespace-unresolved";
				throw new ParseException(Translator.localize(msg),0);
			}
			Object model = ProjectManager.getManager().getCurrentProject().getRoot();
			if (!Model.getCoreHelper().getAllPossibleNamespaces(modelElement,model).contains(nspe)) {
				String msg = "parsing.error.model-element-name.namespace-invalid";
				throw new ParseException(Translator.localize(msg),0);
			}
			Model.getCoreHelper().addOwnedElement(nspe,modelElement);
		}
		Model.getCoreHelper().setName(modelElement,name);
		if (abstrac) {
			Model.getCoreHelper().setAbstract(modelElement,abstrac);
		}
		if (fina) {
			Model.getCoreHelper().setLeaf(modelElement,fina);
		}
		if (publi) {
			Model.getCoreHelper().setVisibility(modelElement,Model.getVisibilityKind().getPublic());
		}
		if (privat) {
			Model.getCoreHelper().setVisibility(modelElement,Model.getVisibilityKind().getPrivate());
		}
		if (protect) {
			Model.getCoreHelper().setVisibility(modelElement,Model.getVisibilityKind().getProtected());
		}
	}
	private static boolean isValidJavaClassName(String name) {
		return true;
	}
	public String getParsingHelp() {
		return"parsing.help.java.fig-nodemodelelement";
	}
	@SuppressWarnings("deprecation")@Deprecated public String toString(Object modelElement,Map args) {
		String name;
		name = Model.getFacade().getName(modelElement);
		if (name == null) {
			return"";
		}
		return NotationUtilityJava.generateLeaf(modelElement,args) + NotationUtilityJava.generateAbstract(modelElement,args) + NotationUtilityJava.generateVisibility(modelElement,args) + NotationUtilityJava.generatePath(modelElement,args) + name;
	}
	public String toString(Object modelElement,NotationSettings settings) {
		String name;
		name = Model.getFacade().getName(modelElement);
		if (name == null) {
			return"";
		}
		String visibility = "";
		if (settings.isShowVisibilities()) {
			visibility = NotationUtilityJava.generateVisibility(modelElement);
		}
		String path = "";
		if (settings.isShowPaths()) {
			path = NotationUtilityJava.generatePath(modelElement);
		}
		return NotationUtilityJava.generateLeaf(modelElement) + NotationUtilityJava.generateAbstract(modelElement) + visibility + path + name;
	}
}



