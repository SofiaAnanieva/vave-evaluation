package org.argouml.uml.cognitive.critics;

import org.apache.log4j.Logger;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.Translator;
import org.argouml.model.Model;
import org.argouml.ocl.CriticOclEvaluator;
import org.argouml.uml.cognitive.UMLToDoItem;
import org.tigris.gef.ocl.ExpansionException;


public class CrUML extends Critic {
	private static final Logger LOG = Logger.getLogger(CrUML.class);
	private String localizationPrefix = "critics";
	public CrUML() {
	}
	public CrUML(String nonDefaultLocalizationPrefix) {
		if (nonDefaultLocalizationPrefix != null) {
			this.localizationPrefix = nonDefaultLocalizationPrefix;
			setupHeadAndDesc();
		}
	}
	public void setResource(String key) {
		super.setHeadline(getLocalizedString(key,"-head"));
		super.setDescription(getLocalizedString(key,"-desc"));
	}
	protected String getLocalizedString(String suffix) {
		return getLocalizedString(getClassSimpleName(),suffix);
	}
	protected String getLocalizedString(String key,String suffix) {
		return Translator.localize(localizationPrefix + "." + key + suffix);
	}
	protected String getInstructions() {
		return getLocalizedString("-ins");
	}
	protected String getDefaultSuggestion() {
		return getLocalizedString("-sug");
	}
	public final void setupHeadAndDesc() {
		setResource(getClassSimpleName());
	}
	@Override public boolean predicate(Object dm,Designer dsgr) {
		if (Model.getFacade().isAModelElement(dm)&&Model.getUmlFactory().isRemoved(dm)) {
			return NO_PROBLEM;
		}else {
			return predicate2(dm,dsgr);
		}
	}
	public boolean predicate2(Object dm,Designer dsgr) {
		return super.predicate(dm,dsgr);
	}
	private static final String OCL_START = "<ocl>";
	private static final String OCL_END = "</ocl>";
	public String expand(String res,ListSet offs) {
		if (offs.size() == 0) {
			return res;
		}
		Object off1 = offs.get(0);
		StringBuffer beginning = new StringBuffer("");
		int matchPos = res.indexOf(OCL_START);
		while (matchPos != -1) {
			int endExpr = res.indexOf(OCL_END,matchPos + 1);
			if (endExpr == -1) {
				break;
			}
			if (matchPos > 0) {
				beginning.append(res.substring(0,matchPos));
			}
			String expr = res.substring(matchPos + OCL_START.length(),endExpr);
			String evalStr = null;
			try {
				evalStr = CriticOclEvaluator.getInstance().evalToString(off1,expr);
			}catch (ExpansionException e) {
				LOG.error("Failed to evaluate critic expression",e);
			}
			if (expr.endsWith("")&&evalStr.equals("")) {
				evalStr = Translator.localize("misc.name.anon");
			}
			beginning.append(evalStr);
			res = res.substring(endExpr + OCL_END.length());
			matchPos = res.indexOf(OCL_START);
		}
		if (beginning.length() == 0) {
			return res;
		}else {
			return beginning.append(res).toString();
		}
	}
	@Override public ToDoItem toDoItem(Object dm,Designer dsgr) {
		return new UMLToDoItem(this,dm,dsgr);
	}
	private final String getClassSimpleName() {
		String className = getClass().getName();
		return className.substring(className.lastIndexOf('.') + 1);
	}
	private static final long serialVersionUID = 1785043010468681602l;
}



