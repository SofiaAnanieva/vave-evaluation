package org.argouml.cognitive;

import java.util.ArrayList;
import java.util.List;
import org.argouml.util.ItemUID;
import org.argouml.cognitive.UnresolvableException;


public class ResolvedCritic {
	private String critic;
	private List<String>offenders;
	public ResolvedCritic(String cr,List<String>offs) {
		critic = cr;
		if (offs != null) {
			offenders = new ArrayList<String>(offs);
		}else {
			offenders = new ArrayList<String>();
		}
	}
	public ResolvedCritic(Critic c,ListSet offs)throws UnresolvableException {
		this(c,offs,true);
	}
	public ResolvedCritic(Critic c,ListSet offs,boolean canCreate)throws UnresolvableException {
		if (c == null) {
			throw new IllegalArgumentException();
		}
		try {
			if (offs != null&&offs.size() > 0) {
				offenders = new ArrayList<String>(offs.size());
				importOffenders(offs,canCreate);
			}else {
				offenders = new ArrayList<String>();
			}
		}catch (UnresolvableException ure) {
			try {
				getCriticString(c);
			}catch (UnresolvableException ure2) {
				throw new UnresolvableException(ure2.getMessage() + "\n" + ure.getMessage());
			}
			throw ure;
		}
		critic = getCriticString(c);
	}
	@Override public int hashCode() {
		if (critic == null) {
			return 0;
		}
		return critic.hashCode();
	}
	@Override public boolean equals(Object obj) {
		ResolvedCritic rc;
		if (obj == null||!(obj instanceof ResolvedCritic)) {
			return false;
		}
		rc = (ResolvedCritic) obj;
		if (critic == null) {
			if (rc.critic != null) {
				return false;
			}
		}else if (!critic.equals(rc.critic)) {
			return false;
		}
		if (offenders == null) {
			return true;
		}
		if (rc.offenders == null) {
			return false;
		}
		for (String offender:offenders) {
			if (offender == null) {
				continue;
			}
			int j;
			for (j = 0;j < rc.offenders.size();j++) {
				if (offender.equals(rc.offenders.get(j))) {
					break;
				}
			}
			if (j >= rc.offenders.size()) {
				return false;
			}
		}
		return true;
	}
	protected String getCriticString(Critic c)throws UnresolvableException {
		if (c == null) {
			throw(new UnresolvableException("Critic is null"));
		}
		String s = c.getClass().toString();
		return s;
	}
	protected void importOffenders(ListSet set,boolean canCreate)throws UnresolvableException {
		String fail = null;
		for (Object obj:set) {
			String id = ItemUID.getIDOfObject(obj,canCreate);
			if (id == null) {
				if (!canCreate) {
					throw new UnresolvableException("ItemUID missing or " + "unable to " + "create for class: " + obj.getClass());
				}
				if (fail == null) {
					fail = obj.getClass().toString();
				}else {
					fail = fail + ", " + obj.getClass().toString();
				}
			}else {
				offenders.add(id);
			}
		}
		if (fail != null) {
			throw new UnresolvableException("Unable to create ItemUID for " + "some class(es): " + fail);
		}
	}
	public String getCritic() {
		return critic;
	}
	public List<String>getOffenderList() {
		return offenders;
	}
	@Override public String toString() {
		StringBuffer sb = new StringBuffer("ResolvedCritic: " + critic + " : ");
		for (int i = 0;i < offenders.size();i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(offenders.get(i));
		}
		return sb.toString();
	}
}



