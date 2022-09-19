package org.argouml.ocl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.profile.internal.ocl.DefaultOclEvaluator;
import org.argouml.profile.internal.ocl.InvalidOclException;
import org.argouml.profile.internal.ocl.ModelInterpreter;
import org.argouml.profile.internal.ocl.OclExpressionEvaluator;
import org.argouml.profile.internal.ocl.uml14.Uml14ModelInterpreter;
import org.tigris.gef.ocl.ExpansionException;


@Deprecated public class OCLEvaluator extends org.tigris.gef.ocl.OCLEvaluator {
	private OclExpressionEvaluator evaluator = new DefaultOclEvaluator();
	private HashMap<String,Object>vt = new HashMap<String,Object>();
	private ModelInterpreter modelInterpreter = new Uml14ModelInterpreter();
	public OCLEvaluator() {
	}
	protected synchronized String evalToString(Object self,String expr)throws ExpansionException {
		if ("self".equals(expr)) {
			expr = "self.name";
		}
		vt.clear();
		vt.put("self",self);
		try {
			return value2String(evaluator.evaluate(vt,modelInterpreter,expr));
		}catch (InvalidOclException e) {
			return"<ocl>invalid expression</ocl>";
		}
	}
	protected synchronized String evalToString(Object self,String expr,String sep)throws ExpansionException {
		_scratchBindings.put("self",self);
		java.util.
				List values = eval(_scratchBindings,expr);
		_strBuf.setLength(0);
		Iterator iter = values.iterator();
		while (iter.hasNext()) {
			Object v = value2String(iter.next());
			if (!"".equals(v)) {
				_strBuf.append(v);
				if (iter.hasNext()) {
					_strBuf.append(sep);
				}
			}
		}
		return _strBuf.toString();
	}
	private String value2String(Object v) {
		if (Model.getFacade().isAExpression(v)) {
			v = Model.getFacade().getBody(v);
			if ("".equals(v)) {
				v = "(unspecified)";
			}
		}else if (Model.getFacade().isAUMLElement(v)) {
			v = Model.getFacade().getName(v);
			if ("".equals(v)) {
				v = Translator.localize("misc.name.anon");
			}
		}else if (v instanceof Collection) {
			String acc = "[";
			Collection collection = (Collection) v;
			for (Object object:collection) {
				acc += value2String(object) + ",";
			}
			acc += "]";
			v = acc;
		}
		return"" + v;
	}
}



