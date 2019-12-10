package demo.don.amazon.findmedian.api;

public interface MedianFinder {
	public final int DEFAULT_MAX_FINDER = 1000000;

	public abstract String getDescription();

	public abstract double getMedian();

	public abstract int size();
}
