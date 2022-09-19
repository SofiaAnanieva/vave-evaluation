package org.argouml.cognitive.critics;

import java.io.Serializable;
import java.util.Date;


public class SnoozeOrder implements Serializable {
	private static final long INITIAL_INTERVAL_MS = 1000 * 60 * 10;
	private Date snoozeUntil;
	private Date snoozeAgain;
	private long interval;
	private Date now = new Date();
	private Date getNow() {
		now.setTime(System.currentTimeMillis());
		return now;
	}
	public SnoozeOrder() {
		snoozeUntil = new Date(0);
		snoozeAgain = new Date(0);
	}
	public boolean getSnoozed() {
		return snoozeUntil.after(getNow());
	}
	public void setSnoozed(boolean h) {
		if (h) {
			snooze();
		}else {
			unsnooze();
		}
	}
	public void snooze() {
		if (snoozeAgain.after(getNow())) {
			interval = nextInterval(interval);
		}else {
			interval = INITIAL_INTERVAL_MS;
		}
		long n = (getNow()).getTime();
		snoozeUntil.setTime(n + interval);
		snoozeAgain.setTime(n + interval + INITIAL_INTERVAL_MS);
	}
	public void unsnooze() {
		snoozeUntil = new Date(0);
	}
	protected long nextInterval(long last) {
		return last * 2;
	}
	private static final long serialVersionUID = -7133285313405407967l;
}



