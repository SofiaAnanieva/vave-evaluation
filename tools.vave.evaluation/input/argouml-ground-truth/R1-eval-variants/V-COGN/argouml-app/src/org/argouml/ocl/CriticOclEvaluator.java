package org.argouml.ocl;

import org.tigris.gef.ocl.ExpansionException;


@Deprecated public class CriticOclEvaluator {
	private static final CriticOclEvaluator INSTANCE = new CriticOclEvaluator();
	private static final OCLEvaluator EVALUATOR = new OCLEvaluator();
	private CriticOclEvaluator() {
	}
	public static final CriticOclEvaluator getInstance() {
		return INSTANCE;
	}
	public synchronized String evalToString(Object self,String expr)throws ExpansionException {
		return EVALUATOR.evalToString(self,expr);
	}
	public synchronized String evalToString(Object self,String expr,String sep)throws ExpansionException {
		return EVALUATOR.evalToString(self,expr,sep);
	}
}



