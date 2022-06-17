package don.demo.concurrent.impl;

import java.util.Random;

import don.demo.concurrent.abstractions.GetTask;
import don.demo.concurrent.abstractions.ProcessState;
import don.demo.concurrent.data.DataDefinition;

/**
 * Represents a task that has a blocking delay (e.g., HTTP GET request) and does
 * minor processing post retrieval
 */
public class HeavyComputeGetTask extends DummyGetTask implements GetTask {
	private final Random rand = new Random(3771);
	private static final long WORK_LENGTH = 10000000L;

	public HeavyComputeGetTask(ProcessState processState, DataDefinition.Remote remote) {
		super(processState, remote);
	}

	@Override
	protected void acceptImpl(String t) {
		super.acceptImpl(t);
		rand.doubles().limit(WORK_LENGTH).map(x -> Math.sin(x)).sum();
		rand.doubles().limit(WORK_LENGTH).map(x -> Math.cos(x)).sum();
		rand.doubles().limit(WORK_LENGTH).map(x -> Math.sqrt(x * x)).average();
		rand.doubles().limit(WORK_LENGTH).map(x -> Math.sin(x + 1.0)).sum();
		rand.doubles().limit(WORK_LENGTH).map(x -> Math.cos(x + 1.0)).sum();
	}
}
