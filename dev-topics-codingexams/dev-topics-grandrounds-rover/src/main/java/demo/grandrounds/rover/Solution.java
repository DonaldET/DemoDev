package demo.grandrounds.rover;

import java.util.HashMap;
import java.util.Map;

/**
 * A planetary rover vehicle lands on a flat plateau represented by a Cartesian
 * grid, which defines positions on the plateau. The grid and rover have
 * orientations of North, South, East, and West. The rover can move forward or
 * backward one unit per command, and can turn 90 degrees left or right from the
 * current heading.
 */
public class Solution {

	enum Heading {
		North(0, 1), South(0, -1), East(1, 0), West(-1, 0);

		private int deltaX;
		private int deltaY;

		private Heading(int deltaX, int deltaY) {
			this.deltaX = deltaX;
			this.deltaY = deltaY;
		}

		public int getDeltaX() {
			return deltaX;
		}

		public int getDeltaY() {
			return deltaY;
		}
	}

	public static class Position {
		public final int x;
		public final int y;

		public Position(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return "[" + x + ", " + y + "]";
		}
	}

	public static class Grid {
		public final int xmin;
		public final int xmax;
		public final int ymin;
		public final int ymax;

		public Grid(int xmin, int xmax, int ymin, int ymax) {
			super();
			assert (xmin < xmax);
			assert (xmax - xmin + 1 > 2);
			assert (ymin < ymax);
			assert (ymax - ymin + 1 > 2);

			this.xmin = xmin;
			this.xmax = xmax;
			this.ymin = ymin;
			this.ymax = ymax;
		}
	}

	public static class RoverState {
		public final boolean isOffGrid;
		public final int lastCommandIndex;
		public final Position position;
		public final Heading heading;

		public RoverState(boolean isOffGrid, int lastCommandIndex, Position position, Heading heading) {
			super();
			this.isOffGrid = isOffGrid;
			this.lastCommandIndex = lastCommandIndex;
			this.position = position;
			this.heading = heading;
		}

		@Override
		public String toString() {
			return "[" + getClass().getSimpleName() + " - 0x" + Integer.toHexString(hashCode()) + "; isOffGrid="
					+ isOffGrid + ", lastCommandIndex=" + lastCommandIndex + ", position=" + position + ", heading="
					+ heading + "]";
		}
	}

	/**
	 * Command a rover to execute a movement around a grid, as defined by its state
	 * object. Movement is expressed using one letter commands.
	 */
	public static RoverState moveRover(Grid grid, RoverState roverStart, String commands) {
		RoverState rover = copyRoverState(roverStart);
		if (commands == null || commands.isEmpty() || rover.isOffGrid) {
			return rover;
		}

		RoverState last = copyRoverState(roverStart);
		for (int i = 0; i < commands.length(); i++) {
			String cmd = commands.substring(i, i + 1);
			RoverCommand rc = strategy.get(cmd);
			if (rc == null) {
				throw new IllegalArgumentException("Unrecognized command [" + cmd + "]");
			}
			rover = rc.execute(grid, last, i, cmd);
			if (rover.isOffGrid) {
				return rover;
			}
			last = rover;
		}

		return last;
	}

	private static RoverState copyRoverState(RoverState roverSrc) {
		return new RoverState(roverSrc.isOffGrid, roverSrc.lastCommandIndex,
				new Position(roverSrc.position.x, roverSrc.position.y), roverSrc.heading);
	}

	/**
	 * One of many command strategies that produce a new state from the old state
	 */
	public static interface RoverCommand {
		public abstract RoverState execute(Grid grid, RoverState initial, int Idx, String id);
	}

	//
	// Command implementations
	//

	private static class RoverHalt implements RoverCommand {
		@Override
		public RoverState execute(Grid grid, RoverState initial, int idx, String id) {
			return initial;
		}
	}

	private static class RoverMove implements RoverCommand {
		@Override
		public RoverState execute(Grid grid, RoverState initial, int idx, String id) {
			int newX = initial.position.x + initial.heading.getDeltaX();
			int newY = initial.position.y + initial.heading.getDeltaY();
			boolean isOffGrid = (newX < grid.xmin || newX > grid.xmax) || (newY < grid.ymin || newY > grid.ymax);

			return new RoverState(isOffGrid, idx, isOffGrid ? initial.position : new Position(newX, newY),
					initial.heading);
		}
	}

	private static class RoverBackup implements RoverCommand {
		@Override
		public RoverState execute(Grid grid, RoverState initial, int idx, String id) {
			int newX = initial.position.x - initial.heading.getDeltaX();
			int newY = initial.position.y - initial.heading.getDeltaY();
			boolean isOffGrid = (newX < grid.xmin || newX > grid.xmax) || (newY < grid.ymin || newY > grid.ymax);

			return new RoverState(isOffGrid, idx, isOffGrid ? initial.position : new Position(newX, newY),
					initial.heading);
		}
	}

	private static class RoverLeft implements RoverCommand {
		@Override
		public RoverState execute(Grid grid, RoverState initial, int idx, String id) {
			Heading heading = initial.heading;
			if (heading == Heading.North) {
				heading = Heading.West;
			} else if (heading == Heading.East) {
				heading = Heading.North;
			} else if (heading == Heading.South) {
				heading = Heading.East;
			} else if (heading == Heading.West) {
				heading = Heading.South;
			} else {
				throw new IllegalArgumentException("Illegal heading: " + heading);
			}
			return new RoverState(initial.isOffGrid, idx, initial.position, heading);
		}
	}

	private static class RoverRight implements RoverCommand {
		@Override
		public RoverState execute(Grid grid, RoverState initial, int idx, String id) {
			Heading heading = initial.heading;
			if (heading == Heading.North) {
				heading = Heading.East;
			} else if (heading == Heading.East) {
				heading = Heading.South;
			} else if (heading == Heading.South) {
				heading = Heading.West;
			} else if (heading == Heading.West) {
				heading = Heading.North;
			} else {
				throw new IllegalArgumentException("Illegal heading: " + heading);
			}
			return new RoverState(initial.isOffGrid, idx, initial.position, heading);
		}
	}

	/**
	 * Rover command implementations are collected into named strategies
	 */
	private static final Map<String, RoverCommand> strategy = new HashMap<>();
	static {
		strategy.put(".", new RoverHalt());
		strategy.put("M", new RoverMove());
		strategy.put("B", new RoverBackup());
		strategy.put("L", new RoverLeft());
		strategy.put("R", new RoverRight());
	}

	// ----------------------------------------------------------------------------------------

	public static void main(String[] args) {
		System.out.println("\n*** Test Rover Commands ***");

		Grid landingZone = new Grid(-5, 7, -6, 8);
		Position center = new Position(0, 0);
		RoverState centerNorth = new RoverState(false, -1, center, Heading.North);

		String[] labels = { "Empty commands", "Move 3 units as initally positioned (North)",
				"Move 3 units out and back", "Turn left, move 3 units West", "Turn right, move 2 units East",
				"Drive Box: West, North, East, South, West", "Drive to Edge of Grid",
				"Drive off Edge of Grid (return last good position)" };
		String[] commands = { "", "MMM", "MMMBBB", "LMMM", "RMM", "LMMM.RMMM.RMMM.MMM.RMMM.RMMM", "LMMMMM", "LMMMMMM" };
		final int n = labels.length;
		assert (n == commands.length);

		for (int i = 0; i < n; i++) {
			testRunner(labels[i], commands[i], landingZone, centerNorth);
		}
	}

	private static void testRunner(String label, String commands, Grid landingZone, RoverState initial) {
		System.out.println("\n--- " + label + ";\nRover Start:\n" + initial + "\nCommands: [" + commands + "]");
		RoverState destination = moveRover(landingZone, initial, commands);
		System.out.println("Rover Finish:\n" + destination);
	}
}
