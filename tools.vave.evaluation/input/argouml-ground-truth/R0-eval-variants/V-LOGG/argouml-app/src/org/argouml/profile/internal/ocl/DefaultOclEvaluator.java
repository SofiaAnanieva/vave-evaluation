package org.argouml.profile.internal.ocl;

import java.io.PushbackReader;
import java.io.StringReader;
import java.util.Map;
import org.apache.log4j.Logger;
import tudresden.ocl.parser.OclParser;
import tudresden.ocl.parser.lexer.Lexer;
import tudresden.ocl.parser.node.Start;


public class DefaultOclEvaluator implements OclExpressionEvaluator {
	private static final Logger LOG = Logger.getLogger(DefaultOclEvaluator.class);
	private static OclExpressionEvaluator instance = null;
	public static OclExpressionEvaluator getInstance() {
		if (instance == null) {
			instance = new DefaultOclEvaluator();
		}
		return instance;
	}
	public Object evaluate(Map<String,Object>vt,ModelInterpreter mi,String ocl)throws InvalidOclException {
		LOG.debug("OCL: " + ocl);
		if (ocl.contains("ore")) {
			System.out.println("VOILA!");
		}
		Lexer lexer = new Lexer(new PushbackReader(new StringReader("context X inv: " + ocl),2));
		OclParser parser = new OclParser(lexer);
		Start tree = null;
		try {
			tree = parser.parse();
		}catch (Exception e) {
			throw new InvalidOclException(ocl);
		}
		EvaluateExpression ee = new EvaluateExpression(vt,mi);
		tree.apply(ee);
		return ee.getValue();
	}
}



