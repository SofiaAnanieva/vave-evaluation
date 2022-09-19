package org.argouml.model.mdr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.argouml.model.DataTypesFactory;
import org.omg.uml.foundation.datatypes.ActionExpression;
import org.omg.uml.foundation.datatypes.ArgListsExpression;
import org.omg.uml.foundation.datatypes.BooleanExpression;
import org.omg.uml.foundation.datatypes.Expression;
import org.omg.uml.foundation.datatypes.IterationExpression;
import org.omg.uml.foundation.datatypes.MappingExpression;
import org.omg.uml.foundation.datatypes.Multiplicity;
import org.omg.uml.foundation.datatypes.MultiplicityRange;
import org.omg.uml.foundation.datatypes.ObjectSetExpression;
import org.omg.uml.foundation.datatypes.ProcedureExpression;
import org.omg.uml.foundation.datatypes.TimeExpression;
import org.omg.uml.foundation.datatypes.TypeExpression;


class DataTypesFactoryMDRImpl extends AbstractUmlModelFactoryMDR implements DataTypesFactory {
	private static final Logger LOG = Logger.getLogger(DataTypesFactoryMDRImpl.class);
	private MDRModelImplementation modelImpl;
	DataTypesFactoryMDRImpl(MDRModelImplementation implementation) {
			this.modelImpl = implementation;
		}
	public ActionExpression createActionExpression(String language,String body) {
		ActionExpression myActionExpression = modelImpl.getUmlPackage().getDataTypes().getActionExpression().createActionExpression(language,body);
		super.initialize(myActionExpression);
		return myActionExpression;
	}
	public ArgListsExpression createArgListsExpression(String language,String body) {
		ArgListsExpression myArgListsExpression = modelImpl.getUmlPackage().getDataTypes().getArgListsExpression().createArgListsExpression(language,body);
		super.initialize(myArgListsExpression);
		return myArgListsExpression;
	}
	public BooleanExpression createBooleanExpression(String language,String body) {
		BooleanExpression myBooleanExpression = modelImpl.getUmlPackage().getDataTypes().getBooleanExpression().createBooleanExpression(language,body);
		super.initialize(myBooleanExpression);
		return myBooleanExpression;
	}
	public Expression createExpression(String language,String body) {
		Expression myExpression = modelImpl.getUmlPackage().getDataTypes().getExpression().createExpression(language,body);
		super.initialize(myExpression);
		return myExpression;
	}
	public IterationExpression createIterationExpression(String language,String body) {
		IterationExpression myIterationExpression = modelImpl.getUmlPackage().getDataTypes().getIterationExpression().createIterationExpression(language,body);
		super.initialize(myIterationExpression);
		return myIterationExpression;
	}
	public MappingExpression createMappingExpression(String language,String body) {
		MappingExpression myMappingExpression = modelImpl.getUmlPackage().getDataTypes().getMappingExpression().createMappingExpression(language,body);
		super.initialize(myMappingExpression);
		return myMappingExpression;
	}
	public ObjectSetExpression createObjectSetExpression(String language,String body) {
		ObjectSetExpression myObjectSetExpression = modelImpl.getUmlPackage().getDataTypes().getObjectSetExpression().createObjectSetExpression(language,body);
		super.initialize(myObjectSetExpression);
		return myObjectSetExpression;
	}
	public ProcedureExpression createProcedureExpression(String language,String body) {
		ProcedureExpression myProcedureExpression = modelImpl.getUmlPackage().getDataTypes().getProcedureExpression().createProcedureExpression(language,body);
		super.initialize(myProcedureExpression);
		return myProcedureExpression;
	}
	public TimeExpression createTimeExpression(String language,String body) {
		TimeExpression myTimeExpression = modelImpl.getUmlPackage().getDataTypes().getTimeExpression().createTimeExpression(language,body);
		super.initialize(myTimeExpression);
		return myTimeExpression;
	}
	public TypeExpression createTypeExpression(String language,String body) {
		TypeExpression myTypeExpression = modelImpl.getUmlPackage().getDataTypes().getTypeExpression().createTypeExpression(language,body);
		super.initialize(myTypeExpression);
		return myTypeExpression;
	}
	public Multiplicity createMultiplicity(int lower,int upper) {
		Multiplicity multiplicity = modelImpl.getUmlPackage().getDataTypes().getMultiplicity().createMultiplicity();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Multiplicity created for range " + lower + ".." + upper);
		}
		multiplicity.getRange().add(createMultiplicityRange(lower,upper));
		super.initialize(multiplicity);
		return multiplicity;
	}
	public Multiplicity createMultiplicity(List range) {
		Multiplicity multiplicity = modelImpl.getUmlPackage().getDataTypes().getMultiplicity().createMultiplicity();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Multiplicity created for list " + range);
		}
		multiplicity.getRange().addAll(range);
		super.initialize(multiplicity);
		return multiplicity;
	}
	public Multiplicity createMultiplicity(String str) {
		List<MultiplicityRange>ranges = Collections.unmodifiableList(parseRanges(str));
		return createMultiplicity(ranges);
	}
	private List<MultiplicityRange>parseRanges(String str) {
		List<MultiplicityRange>rc = new ArrayList<MultiplicityRange>();
		if ("".equals(str)) {
			rc.add(createMultiplicityRange("1..1"));
			return rc;
		}
		StringTokenizer stk = new StringTokenizer(str,",");
		while (stk.hasMoreTokens()) {
			rc.add(createMultiplicityRange(stk.nextToken()));
		}
		return rc;
	}
	public MultiplicityRange createMultiplicityRange(String str) {
		StringTokenizer stk = new StringTokenizer(str,". ");
		if (!stk.hasMoreTokens()) {
			throw new IllegalArgumentException("empty multiplicity range");
		}
		int lower = stringToBound(stk.nextToken());
		int upper = 0;
		if (!stk.hasMoreTokens()) {
			upper = lower;
			if (lower == -1) {
				lower = 0;
			}
		}else {
			upper = stringToBound(stk.nextToken());
			if (stk.hasMoreTokens()) {
				throw new IllegalArgumentException("illegal range specification");
			}
		}
		return createMultiplicityRange(lower,upper);
	}
	public MultiplicityRange createMultiplicityRange(int lower,int upper) {
		MultiplicityRange range = modelImpl.getUmlPackage().getDataTypes().getMultiplicityRange().createMultiplicityRange(lower,upper);
		return range;
	}
	static String boundToString(int i) {
		if (i == -1) {
			return"*";
		}else {
			return"" + i;
		}
	}
	private static int stringToBound(String b) {
		try {
			if (b.equals("n")||b.equals("*")) {
				return-1;
			}else {
				return Integer.parseInt(b);
			}
		}catch (Exception ex) {
			throw new IllegalArgumentException("illegal range bound : " + (b == null?"null":b));
		}
	}
}



