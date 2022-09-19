package org.argouml.cognitive.checklist;

import java.io.Serializable;
import org.argouml.util.Predicate;
import org.argouml.util.PredicateGefWrapper;
import org.argouml.util.PredicateTrue;


public class CheckItem implements Serializable {
	private String category;
	private String description;
	private String moreInfoURL = "http://argouml.tigris.org/";
	private Predicate predicate = PredicateTrue.getInstance();
	public CheckItem(String c,String d) {
		setCategory(c);
		setDescription(d);
	}
	@SuppressWarnings("deprecation")@Deprecated public CheckItem(String c,String d,String m,org.tigris.gef.util.Predicate p) {
		this(c,d);
		setMoreInfoURL(m);
		predicate = new PredicateGefWrapper(p);
	}
	public CheckItem(String c,String d,String m,Predicate p) {
		this(c,d);
		setMoreInfoURL(m);
		predicate = p;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String c) {
		category = c;
	}
	public String getDescription() {
		return description;
	}
	public String getDescription(Object dm) {
		return expand(description,dm);
	}
	public void setDescription(String d) {
		description = d;
	}
	public String getMoreInfoURL() {
		return moreInfoURL;
	}
	public void setMoreInfoURL(String m) {
		moreInfoURL = m;
	}
	@SuppressWarnings("deprecation")@Deprecated public org.tigris.gef.util.Predicate getPredicate() {
		if (predicate instanceof PredicateGefWrapper) {
			return((PredicateGefWrapper) predicate).getGefPredicate();
		}
		throw new IllegalStateException("Mixing legacy API and new API is not" + "supported.  Please update your code.");
	}
	public Predicate getPredicate2() {
		return predicate;
	}
	@SuppressWarnings("deprecation")@Deprecated public void setPredicate(org.tigris.gef.util.Predicate p) {
		predicate = new PredicateGefWrapper(p);
	}
	public void setPredicate(Predicate p) {
		predicate = p;
	}
	@Override public int hashCode() {
		return getDescription().hashCode();
	}
	@Override public boolean equals(Object o) {
		if (!(o instanceof CheckItem)) {
			return false;
		}
		CheckItem i = (CheckItem) o;
		return getDescription().equals(i.getDescription());
	}
	@Override public String toString() {
		return getDescription();
	}
	public String expand(String desc,Object dm) {
		return desc;
	}
}



