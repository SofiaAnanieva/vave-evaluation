package org.argouml.model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.server.UID;


public final class UUIDManager {
	private static final UUIDManager INSTANCE = new UUIDManager();
	private InetAddress address;
	private UUIDManager() {
		try {
			address = InetAddress.getLocalHost();
		}catch (UnknownHostException e) {
			throw new IllegalStateException("UnknownHostException caught - set up your /etc/hosts");
		}
	}
	public static UUIDManager getInstance() {
		return INSTANCE;
	}
	public synchronized String getNewUUID() {
		UID uid = new UID();
		StringBuffer s = new StringBuffer();
		if (address != null) {
			byte[]b = address.getAddress();
			for (int i = 0;i < b.;i++) {
				s.append((new Byte(b[i])).longValue()).append("-");
			}
		}
		s.append(uid.toString());
		return s.toString();
	}
}



