package don.demo.concurrent.test;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import don.demo.concurrent.abstractions.ProcessState;
import don.demo.concurrent.data.DataDefinition;
import don.demo.concurrent.data.DataDefinition.Remote;
import don.demo.concurrent.impl.DummyGetTask;

public class TestDummyGetTask {
	ProcessState processState = new ProcessState();
	private static final ArrayList<DataDefinition.Remote> remotes = new ArrayList<DataDefinition.Remote>();

	static {
		remotes.add(new Remote("remote0", 4, 1000));
		remotes.add(new Remote("remote1", 4, 0));
		remotes.add(new Remote("remote2", 4, 0));
	}

	public void setUp() throws Exception {
		processState = new ProcessState();
	}

	@After
	public void tearDown() throws Exception {
		processState = null;
	}

	@Test
	public void testFailedSequence() {
		Assert.assertEquals("initial count bad", 0, processState.taskCount.get());
		Assert.assertEquals("initial failed count bad", 0, processState.failedTaskCount.get());

		new DummyGetTask(processState, remotes.get(0)).accept(remotes.get(0).id());
		Assert.assertEquals("after 0 count bad", 1, processState.taskCount.get());
		Assert.assertEquals("after 0 failed count bad", 0, processState.failedTaskCount.get());
		Assert.assertEquals("after 0 checksum bad", 467771033L, processState.checkSum.get());
		Assert.assertEquals("after 0 byteCount bad", 4L, processState.byteCount.get());

		new DummyGetTask(processState, remotes.get(2)).accept(remotes.get(2).id());
		Assert.assertEquals("after 0 count bad", 2, processState.taskCount.get());
		Assert.assertEquals("after 0 failed count bad", 0, processState.failedTaskCount.get());
		Assert.assertEquals("after 0 checksum bad", -457729379L, processState.checkSum.get());
		Assert.assertEquals("after 0 byteCount bad", 8L, processState.byteCount.get());

		new DummyGetTask(processState, remotes.get(1)).accept(remotes.get(1).id());
		Assert.assertEquals("after 0 count bad", 3, processState.taskCount.get());
		Assert.assertEquals("after 0 failed count bad", 1, processState.failedTaskCount.get());
		Assert.assertEquals("after 0 checksum bad", -457729379L, processState.checkSum.get());
		Assert.assertEquals("after 0 byteCount bad", 8L, processState.byteCount.get());
	}

	@Test
	public void testDelay() {
		long start = System.nanoTime();
		new DummyGetTask(processState, remotes.get(0)).accept(remotes.get(0).id());
		long elapsed = System.nanoTime() - start;
		Assert.assertTrue("wait failed, got " + elapsed, elapsed >= 1000000000);
	}
}
