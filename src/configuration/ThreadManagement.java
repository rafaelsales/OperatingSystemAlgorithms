package configuration;

public interface ThreadManagement extends Runnable {

	public void stop()throws InterruptedException;
	public void start();
	
}
