package demo.amazon.statemachines;

import java.util.Arrays;

/**
 * We monitor the CPU consumption a distributed process that uses multiple
 * servers, and observe the percentage of CPU used once each second. Our system
 * increases or decreases the number of servers allocated to the distributed
 * process based on the degree of CPU usage. The distributed process is
 * constrained to have more than one server and no more than 1,800 servers.
 * <p>
 * Based on the percentage of CPU usage (<em>P</em>) each second observed, we
 * have the following state transitions:
 * <ul>
 * <li>0 &lt;= <em>P</em> &lt; 50 then half the number of servers</li>
 * <li>50 &gt;= <em>P</em> <strong>and</strong> &lt;= 75 then keep the same
 * number of servers</li>
 * <li>else <em>P</em> &gt; 75 and double the number of servers</li>
 * </ul>
 * The number of servers must meet the constraints above after modification. The
 * number of servers may only be altered no more of that once every ten seconds.
 * <p>
 * Our monitoring algorithm accepts an observation of the percentage of CPU used
 * each second. It adjusts the number of servers as required by the rules above.
 * 
 * @author Donald Trummell
 */
public class Solution2 {
	/*
	 * Complete the 'numberOfServers' function below.
	 *
	 * The function is expected to return an INTEGER and accepts Observations. The
	 * observations are following parameters: 1. INTEGER server count, 2.
	 * INTEGER_ARRAY of CPU usage (0-100).
	 */

	private static int numberOfServers(int serverCount, int[] cpu) {
		if (cpu.length < 1) {
			throw new IllegalArgumentException("nothing to progress");
		}

		if (serverCount < 2 || serverCount > 1800) {
			throw new IllegalArgumentException("server count " + serverCount + " out of range.");
		}

		return numberOfServersImpl(serverCount, cpu);
	}

	private static int numberOfServersImpl(int serverCount, int[] cpu) {
		class State {
			public int servers;
			public int lastChange;

			public State(int servers, int lastChange) {
				this.servers = servers;
				this.lastChange = lastChange;
			}
		}

		int n = cpu.length;
		State state = new State(serverCount, -1);
		for (int i = 0; i < n; i++) {
			if (50 <= cpu[i] && cpu[i] <= 75) {
				// No change needed
				continue;
			}

			if (state.lastChange != -1 && i - state.lastChange < 10) {
				// Unable to adjust
				continue;
			}

			int newServerCount = cpu[i] > 75 ? Math.min(1800, 2 * state.servers) : Math.max(2, state.servers / 2);
			if (newServerCount != state.servers) {
				state.servers = newServerCount;
				state.lastChange = i;
			}
		}

		return state.servers;
	}

	public static void main(String[] args) {
		System.out.println("Final Number of Servers");
		int servers = 10;
		int[] cpuUsed = { 50, 56, 60, 65 };
		int n = cpuUsed.length;
		int nfinal = Solution2.numberOfServers(servers, cpuUsed);
		int nexp = 10;
		System.out.println("CPU:     " + servers + " --> " + Arrays.toString(cpuUsed));
		System.out.println("servers: " + nfinal + " after " + n + " seconds."
				+ (nfinal == nexp ? "" : " " + nfinal + " != " + nexp));

		servers = 10;
		cpuUsed = new int[] { 76, 50, 60 };
		n = cpuUsed.length;
		nfinal = Solution2.numberOfServers(servers, cpuUsed);
		nexp = 20;
		System.out.println("\nCPU:     " + servers + " --> " + Arrays.toString(cpuUsed));
		System.out.println("servers: " + nfinal + " after " + n + " seconds."
				+ (nfinal == nexp ? "" : " " + nfinal + " != " + nexp));

		servers = 10;
		cpuUsed = new int[] { 45, 80, 85, 90 };
		n = cpuUsed.length;
		nfinal = Solution2.numberOfServers(servers, cpuUsed);
		nexp = 5;
		System.out.println("\nCPU:     " + servers + " --> " + Arrays.toString(cpuUsed));
		System.out.println("servers: " + nfinal + " after " + n + " seconds."
				+ (nfinal == nexp ? "" : " " + nfinal + " != " + nexp));

		servers = 10;
		cpuUsed = new int[] { 80, 50, 50, 50, 50, 50, 50, 50, 55, 56, 35 };
		n = cpuUsed.length;
		nfinal = Solution2.numberOfServers(servers, cpuUsed);
		nexp = 10;
		System.out.println("\nCPU:     " + servers + " --> " + Arrays.toString(cpuUsed));
		System.out.println("servers: " + nfinal + " after " + n + " seconds."
				+ (nfinal == nexp ? "" : " " + nfinal + " != " + nexp));

		servers = 10;
		cpuUsed = new int[] { 20, 20 };
		n = cpuUsed.length;
		nfinal = Solution2.numberOfServers(servers, cpuUsed);
		nexp = 5;
		System.out.println("\nCPU:     " + servers + " --> " + Arrays.toString(cpuUsed));
		System.out.println("servers: " + nfinal + " after " + n + " seconds."
				+ (nfinal == nexp ? "" : " " + nfinal + " != " + nexp));

		servers = 10;
		cpuUsed = new int[] { 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20 };
		n = cpuUsed.length;
		nfinal = Solution2.numberOfServers(servers, cpuUsed);
		nexp = 2;
		System.out.println("\nCPU:     " + servers + " --> " + Arrays.toString(cpuUsed));
		System.out.println("servers: " + nfinal + " after " + n + " seconds."
				+ (nfinal == nexp ? "" : " " + nfinal + " != " + nexp));

		servers = 900;
		cpuUsed = new int[] { 80, 80 };
		n = cpuUsed.length;
		nfinal = Solution2.numberOfServers(servers, cpuUsed);
		nexp = 1800;
		System.out.println("\nCPU:     " + servers + " --> " + Arrays.toString(cpuUsed));
		System.out.println("servers: " + nfinal + " after " + n + " seconds."
				+ (nfinal == nexp ? "" : " " + nfinal + " != " + nexp));

		servers = 900;
		cpuUsed = new int[] { 80, 80, 80, 80, 80, 80, 80, 80, 80, 80, 80 };
		n = cpuUsed.length;
		nfinal = Solution2.numberOfServers(servers, cpuUsed);
		nexp = 1800;
		System.out.println("\nCPU:     " + servers + " --> " + Arrays.toString(cpuUsed));
		System.out.println("servers: " + nfinal + " after " + n + " seconds."
				+ (nfinal == nexp ? "" : " " + nfinal + " != " + nexp));
	}
}
