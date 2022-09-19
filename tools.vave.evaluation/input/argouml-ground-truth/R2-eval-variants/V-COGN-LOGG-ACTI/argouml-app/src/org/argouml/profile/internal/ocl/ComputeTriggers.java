package org.argouml.profile.internal.ocl;

import java.util.ArrayList;
import java.util.List;
import tudresden.ocl.parser.analysis.DepthFirstAdapter;
import tudresden.ocl.parser.node.AClassifierContext;


public class ComputeTriggers extends DepthFirstAdapter {
	private List<String>triggs = new ArrayList<String>();
	@Override public void caseAClassifierContext(AClassifierContext node) {
		String str = "" + node.getPathTypeName();
		triggs.add(str.trim().toLowerCase());
	}
	public List<String>getTriggers() {
		return triggs;
	}
}



