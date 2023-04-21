package don.demo.dataservices.servicesimpl;

public abstract class AbstractAccessor {

	public static long DEFAULT_DELAY = 0;

	public final long milliDelay;

	public AbstractAccessor(long milliDelay) {
		this.milliDelay = milliDelay;
	}

	public AbstractAccessor() {
		this(DEFAULT_DELAY);
	}

	protected void sleepAccessor(String label) {
		if (milliDelay > 0) {
			try {
				Thread.sleep(milliDelay);
			} catch (InterruptedException ex) {
				System.err.println("Thread sleep interruped for [" + label + "]");
			}
		}
	}
}
