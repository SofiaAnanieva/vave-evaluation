package org.argouml.persistence;

import java.util.List;
import java.util.Vector;
import org.argouml.cognitive.ResolvedCritic;


public class ResolvedCriticXMLHelper {
	private final ResolvedCritic item;
	public ResolvedCriticXMLHelper(ResolvedCritic rc) {
		if (rc == null) {
			throw new IllegalArgumentException("There must be a ResolvedCritic supplied.");
		}
		item = rc;
	}
	public String getCritic() {
		return item.getCritic();
	}
	public Vector<OffenderXMLHelper>getOffenderList() {
		List<String>in = item.getOffenderList();
		Vector<OffenderXMLHelper>out;
		if (in == null) {
			return null;
		}
		out = new Vector<OffenderXMLHelper>();
		for (String elem:in) {
			try {
				OffenderXMLHelper helper = new OffenderXMLHelper(elem);
				out.addElement(helper);
			}catch (ClassCastException cce) {
			}
		}
		return out;
	}
}



