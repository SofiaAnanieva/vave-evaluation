package lancs.mobilemedia.core.threads;


public class BaseThread implements Runnable {
	public BaseThread() {
		System.out.println("BaseThread:: 0 Param Constructor used: Using default values");
	}
	public void run() {
		System.out.println("Starting BaseThread::run()");
		System.out.println("Finishing Baseathread::run()");
	}
}



