package demo.don.amazon.findmedian.api;

import java.util.Random;

public interface StreamSupplier {
	public final int DEFAULT_MAX_STREAM = 1000000;

	public abstract int getNext();

	public abstract int size();

	public abstract int remaining();

	default int[] getRandomArray(int capacity, int upperBound) {
		final Random r = new Random(6793);
		final int[] stream = new int[capacity];
		for (int i = 0; i < capacity; i++) {
			stream[i] = r.nextInt(upperBound);
		}
		return stream;
	}
}
