package org.argouml.profile.internal.ocl;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.argouml.model.MetaTypes;
import org.argouml.model.Model;
import tudresden.ocl.parser.analysis.DepthFirstAdapter;
import tudresden.ocl.parser.node.AClassifierContext;


public class ComputeDesignMaterials extends DepthFirstAdapter {
	private Set<Object>dms = new HashSet<Object>();
	@Override public void caseAClassifierContext(AClassifierContext node) {
		String str = ("" + node.getPathTypeName()).trim();
		if (str.equals("Class")) {
			dms.add(Model.getMetaTypes().getUMLClass());
		}else {
			try {
				Method m = MetaTypes.class.getDeclaredMethod("get" + str,new Class[0]);
				if (m != null) {
					dms.add(m.invoke(Model.getMetaTypes(),new Object[0]));
				}
			}catch (Exception e) {
			}
		}
	}
	public Set<Object>getCriticizedDesignMaterials() {
		return dms;
	}
}



