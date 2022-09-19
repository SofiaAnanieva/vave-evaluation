package lancs.mobilemedia.sms;


public class SmsSenderThread implements Runnable {
	private String smsPort;
	private String destinationAddress;
	private String messageText = "default";
	private byte[]binData;
	public SmsSenderThread(String smsPort,String destinationAddress,String messageText) {
		System.out.println("SmsSenderThread:: 3 Param Constructor: " + smsPort + "," + destinationAddress + "," + messageText);
		this.messageText = messageText;
		this.destinationAddress = destinationAddress;
		this.smsPort = smsPort;
	}
	public void run() {
		System.out.println("SmsSenderThread::run: Sending message: " + messageText + " to: " + destinationAddress);
		SmsMessaging smsMessenger = new SmsMessaging(smsPort,destinationAddress);
		smsMessenger.sendImage(this.binData);
		System.out.println("Finishing SMSSender run()");
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public void setBinaryData(byte[]data) {
		System.out.println("SmsSenderThread: setBinaryData of length: " + data.);
		this.binData = data;
	}
	public String getSmsPort() {
		return smsPort;
	}
	public void setSmsPort(String smsPort) {
		this.smsPort = smsPort;
	}
}



