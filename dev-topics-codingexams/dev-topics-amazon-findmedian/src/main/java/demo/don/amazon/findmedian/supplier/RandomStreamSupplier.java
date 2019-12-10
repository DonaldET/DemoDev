package demo.don.amazon.findmedian.supplier;

import demo.don.amazon.findmedian.api.StreamSupplier;

public class RandomStreamSupplier implements StreamSupplier {
	private int[] stream = null;
	private int lastGiven = -1;
	private int capacity = 0;

	public RandomStreamSupplier(int[] data) {
		capacity = data.length;
		stream = data.clone();
		lastGiven = -1;
	}

	@Override
	public int getNext() {
		return stream[++lastGiven];
	}

	@Override
	public int size() {
		return capacity;
	}

	public int remaining() {
		return capacity - lastGiven - 1;
	}
}
