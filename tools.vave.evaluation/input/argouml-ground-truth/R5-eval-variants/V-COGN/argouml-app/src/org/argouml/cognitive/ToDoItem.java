package org.argouml.cognitive;

import java.io.Serializable;
import javax.swing.Icon;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.cognitive.critics.WizardItem;
import org.argouml.util.CollectionUtil;
import org.argouml.cognitive.Poster;
import org.argouml.cognitive.ToDoList;


public class ToDoItem implements Serializable,WizardItem {
	public static final int INTERRUPTIVE_PRIORITY = 9;
	public static final int HIGH_PRIORITY = 1;
	public static final int MED_PRIORITY = 2;
	public static final int LOW_PRIORITY = 3;
	private Poster thePoster;
	private String theHeadline;
	private int thePriority;
	private String theDescription;
	private String theMoreInfoURL;
	private ListSet theOffenders;
	private final Wizard theWizard;
	public ToDoItem(Poster poster,String h,int p,String d,String m,ListSet offs) {
		checkOffs(offs);
		thePoster = poster;
		theHeadline = h;
		theOffenders = offs;
		thePriority = p;
		theDescription = d;
		theMoreInfoURL = m;
		theWizard = null;
	}
	public ToDoItem(Poster poster,String h,int p,String d,String m) {
		thePoster = poster;
		theHeadline = h;
		theOffenders = new ListSet();
		thePriority = p;
		theDescription = d;
		theMoreInfoURL = m;
		theWizard = null;
	}
	public ToDoItem(Critic c,Object dm,Designer dsgr) {
		checkArgument(dm);
		thePoster = c;
		theHeadline = c.getHeadline(dm,dsgr);
		theOffenders = new ListSet(dm);
		thePriority = c.getPriority(theOffenders,dsgr);
		theDescription = c.getDescription(theOffenders,dsgr);
		theMoreInfoURL = c.getMoreInfoURL(theOffenders,dsgr);
		theWizard = c.makeWizard(this);
	}
	public ToDoItem(Critic c,ListSet offs,Designer dsgr) {
		checkOffs(offs);
		thePoster = c;
		theHeadline = c.getHeadline(offs,dsgr);
		theOffenders = offs;
		thePriority = c.getPriority(theOffenders,dsgr);
		theDescription = c.getDescription(theOffenders,dsgr);
		theMoreInfoURL = c.getMoreInfoURL(theOffenders,dsgr);
		theWizard = c.makeWizard(this);
	}
	public ToDoItem(Critic c) {
		thePoster = c;
		theHeadline = c.getHeadline();
		theOffenders = new ListSet();
		thePriority = c.getPriority(null,null);
		theDescription = c.getDescription(null,null);
		theMoreInfoURL = c.getMoreInfoURL(null,null);
		theWizard = c.makeWizard(this);
	}
	protected void checkArgument(Object dm) {
	}
	private void checkOffs(ListSet offs) {
		if (offs == null) {
			throw new IllegalArgumentException("A ListSet of offenders must be supplied.");
		}
		Object offender = CollectionUtil.getFirstItemOrNull(offs);
		if (offender != null) {
			checkArgument(offender);
		}
		if (offs.size() >= 2) {
			offender = offs.get(1);
			checkArgument(offender);
		}
	}
	private String cachedExpandedHeadline;
	private String cachedExpandedDescription;
	public String getHeadline() {
		if (cachedExpandedHeadline == null) {
			cachedExpandedHeadline = thePoster.expand(theHeadline,theOffenders);
		}
		return cachedExpandedHeadline;
	}
	@Deprecated public void setHeadline(String h) {
		theHeadline = h;
		cachedExpandedHeadline = null;
	}
	public String getDescription() {
		if (cachedExpandedDescription == null) {
			cachedExpandedDescription = thePoster.expand(theDescription,theOffenders);
		}
		return cachedExpandedDescription;
	}
	@Deprecated public void setDescription(String d) {
		theDescription = d;
		cachedExpandedDescription = null;
	}
	public String getMoreInfoURL() {
		return theMoreInfoURL;
	}
	@Deprecated public void setMoreInfoURL(String m) {
		theMoreInfoURL = m;
	}
	public int getPriority() {
		return thePriority;
	}
	@Deprecated public void setPriority(int p) {
		thePriority = p;
	}
	public int getProgress() {
		if (theWizard != null) {
			return theWizard.getProgress();
		}
		return 0;
	}
	public ListSet getOffenders() {
		assert theOffenders != null;
		return theOffenders;
	}
	@Deprecated public void setOffenders(ListSet offenders) {
		theOffenders = offenders;
	}
	public Poster getPoster() {
		return thePoster;
	}
	public Icon getClarifier() {
		return thePoster.getClarifier();
	}
	public Wizard getWizard() {
		return theWizard;
	}
	public boolean containsKnowledgeType(String type) {
		return getPoster().containsKnowledgeType(type);
	}
	public boolean supports(Decision d) {
		return getPoster().supports(d);
	}
	public boolean supports(Goal g) {
		return getPoster().supports(g);
	}
	@Override public int hashCode() {
		int code = 0;
		code += getHeadline().hashCode();
		if (getPoster() != null) {
			code += getPoster().hashCode();
		}
		return code;
	}
	@Override public boolean equals(Object o) {
		if (!(o instanceof ToDoItem)) {
			return false;
		}
		ToDoItem i = (ToDoItem) o;
		if (!getHeadline().equals(i.getHeadline())) {
			return false;
		}
		if (!(getPoster() == (i.getPoster()))) {
			return false;
		}
		if (!getOffenders().equals(i.getOffenders())) {
			return false;
		}
		return true;
	}
	public void select() {
		for (Object dm:getOffenders()) {
			if (dm instanceof Highlightable) {
				((Highlightable) dm).setHighlight(true);
			}
		}
	}
	public void deselect() {
		for (Object dm:getOffenders()) {
			if (dm instanceof Highlightable) {
				((Highlightable) dm).setHighlight(false);
			}
		}
	}
	public void action() {
		deselect();
		select();
	}
	public void changed() {
		ToDoList list = Designer.theDesigner().getToDoList();
		list.fireToDoItemChanged(this);
	}
	public void fixIt() {
		thePoster.fixIt(this,null);
	}
	public boolean canFixIt() {
		return thePoster.canFixIt(this);
	}
	public boolean stillValid(Designer d) {
		if (thePoster == null) {
			return true;
		}
		if (theWizard != null&&theWizard.isStarted()&&!theWizard.isFinished()) {
			return true;
		}
		return thePoster.stillValid(this,d);
	}
	@Override public String toString() {
		return this.getClass().getName() + "(" + getHeadline() + ") on " + getOffenders().toString();
	}
	private static final long serialVersionUID = 3058660098451455153l;
}



