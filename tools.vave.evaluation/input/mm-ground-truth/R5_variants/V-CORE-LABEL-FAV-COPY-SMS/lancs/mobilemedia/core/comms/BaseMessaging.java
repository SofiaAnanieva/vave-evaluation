package lancs.mobilemedia.core.comms;

import java.io.IOException;
import java.io.InterruptedIOException;
import javax.wireless.messaging.MessageConnection;


public abstract class BaseMessaging {
	public abstract boolean sendImage(byte[]imageData);
	public abstract byte[]receiveImage()throws InterruptedIOException,IOException;
	public abstract void cleanUpConnections(MessageConnection conn);
}



