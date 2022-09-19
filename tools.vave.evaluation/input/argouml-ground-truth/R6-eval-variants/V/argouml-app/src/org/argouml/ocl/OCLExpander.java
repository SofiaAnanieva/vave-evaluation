package org.argouml.ocl;

import java.util.Map;


public class OCLExpander extends org.tigris.gef.ocl.OCLExpander {
	public OCLExpander(Map templates) {
		super(templates);
	}
	protected void createEvaluator() {
		evaluator = new OCLEvaluator();
	}
}



