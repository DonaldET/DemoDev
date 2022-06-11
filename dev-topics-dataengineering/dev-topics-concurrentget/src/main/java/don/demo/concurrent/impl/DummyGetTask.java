package don.demo.concurrent.impl;

import java.util.concurrent.TimeUnit;

import don.demo.concurrent.abstractions.GetTask;
import don.demo.concurrent.abstractions.ProcessState;
import don.demo.concurrent.data.DataDefinition;

/**
 * Represents a task that has a blocking delay (e.g., HTTP GET request) and does
 * minor processing post retrieval
 */
public class DummyGetTask implements GetTask {

	private ProcessState processState;
	private DataDefinition.Remote remote;

	public DummyGetTask(ProcessState processState, DataDefinition.Remote remote) {
		super();
		this.processState = processState;
		this.remote = remote;
	}

	@Override
	public void accept(String t) {
		if (!t.equals(remote.id())) {
			throw new IllegalArgumentException("expected " + remote.id() + ", but had t");
		}
		processState.taskCount.incrementAndGet();
		try {
			acceptImpl(t);
		} catch (Exception ex) {
			processState.failedTaskCount.incrementAndGet();
			System.err.println("\n**** Caught and ignoring error " + ex.getMessage());
		}
	}

	private void acceptImpl(String t) {
		try {
			TimeUnit.MILLISECONDS.sleep(remote.delay());
		} catch (InterruptedException ex) {
			// ignore the interrupt
		}

		if ("remote1".equals(t)) {
			throw new IllegalArgumentException("Planned failure for task " + t);
		}

		byte[] data = new byte[remote.length()];
		processState.rand.nextBytes(data);
		processState.byteCount.addAndGet(remote.length());
		long sum = 0;
		for (int i = 0; i < remote.length(); i++) {
			sum += (0x00000000000000FF & data[i]);
		}
		processState.checkSum.addAndGet(sum);
	}
}
