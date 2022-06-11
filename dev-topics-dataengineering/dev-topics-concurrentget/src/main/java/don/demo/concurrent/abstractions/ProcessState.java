package don.demo.concurrent.abstractions;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Running task state.
 */
public class ProcessState {
	public final AtomicInteger taskCount = new AtomicInteger();
	public final AtomicInteger failedTaskCount = new AtomicInteger();
	public final AtomicLong byteCount = new AtomicLong();
	public final AtomicLong checkSum = new AtomicLong();
	public final Random rand = new Random(4229);

	@Override
	public String toString() {
		return "ProcessState [taskCount      : " + taskCount + ",\n              failedTaskCount: " + failedTaskCount
				+ ",\n              byteCount      : " + byteCount + ",\n              checkSum       : " + checkSum
				+ "]";
	}
}
