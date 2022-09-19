package org.argouml.uml.cognitive.checklist;

import org.apache.log4j.Logger;
import org.argouml.cognitive.checklist.CheckItem;
import org.argouml.i18n.Translator;
import org.argouml.model.InvalidElementException;
import org.argouml.ocl.CriticOclEvaluator;
import org.argouml.ocl.OCLEvaluator;
import org.tigris.gef.ocl.ExpansionException;


public class UMLCheckItem extends CheckItem {
	private static final Logger LOG = Logger.getLogger(UMLCheckItem.class);
	public UMLCheckItem(String c,String d) {
		super(c,d);
	}
	public UMLCheckItem(String c,String d,String m,org.tigris.gef.util.Predicate p) {
		super(c,d,m,p);
	}
	public UMLCheckItem(String c,String d,String m,org.argouml.util.Predicate p) {
		super(c,d,m,p);
	}
	@Override public String expand(String res,Object dm) {
		int searchPos = 0;
		int matchPos = res.indexOf(OCLEvaluator.OCL_START,searchPos);
		while (matchPos != -1) {
			int endExpr = res.indexOf(OCLEvaluator.OCL_END,matchPos + 1);
			String expr = res.substring(matchPos + OCLEvaluator.OCL_START.length(),endExpr);
			String evalStr = null;
			try {
				evalStr = CriticOclEvaluator.getInstance().evalToString(dm,expr);
			}catch (ExpansionException e) {
				LOG.error("Failed to evaluate critic expression",e);
			}catch (InvalidElementException e) {
				evalStr = Translator.localize("misc.name.deleted");
			}
			LOG.debug("expr=\'" + expr + "\' = \'" + evalStr + "\'");
			res = res.substring(0,matchPos) + evalStr + res.substring(endExpr + OCLEvaluator.OCL_END.length());
			searchPos = endExpr + 1;
			matchPos = res.indexOf(OCLEvaluator.OCL_START,searchPos);
		}
		return res;
	}
}



