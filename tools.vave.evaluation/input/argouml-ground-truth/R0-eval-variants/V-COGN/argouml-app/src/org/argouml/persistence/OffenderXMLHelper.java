package org.argouml.persistence;


public class OffenderXMLHelper {
	private final String item;
	public OffenderXMLHelper(String offender) {
		if (offender == null) {
			throw new IllegalArgumentException("An offender string must be supplied");
		}
		item = offender;
	}
	public String getOffender() {
		return item;
	}
}



