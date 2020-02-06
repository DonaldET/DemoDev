package demo.algo.sensor.test;

import don.demo.algo.sensor.MonitorExposureByAreaMapped;
import don.demo.algo.sensor.SensorMonitoring.BoundingBox;
import don.demo.algo.sensor.SensorMonitoring.ExposureSession;
import don.demo.algo.sensor.SensorMonitoring.Rectangle;

/**
 * Create a simple character-based display of the radiated regions indicating
 * the level of exposure. Regions are made relative, beginning at (0,0).
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class DumpSensorData {

	private static final MonitorExposureByAreaMapped mapper = new MonitorExposureByAreaMapped();

	public static void main(String[] args) {
		System.out.print("\nDump of Test Data Sensor Regions");
		for (int id = 0; id < GenerateDataAndTest.testCases - 1; id++) {
			ExposureSession sessionData = GenerateDataAndTest.getSessionData(id);
			System.out.println("\n-----------------------");
			System.out.println("ID: " + id + ";  N: " + sessionData.n + ",  K: " + sessionData.k + ",  Area: "
					+ sessionData.expectedArea);
			for (Rectangle r : sessionData.sessions) {
				System.out.println("   " + r);
			}

			GenerateDataAndTest.testData(id, mapper, false);
			int[] sensorRegion = mapper.getSensorRegions();
			BoundingBox bbox = mapper.getBbox();
			printSensor("Data[" + id + "]", sensorRegion, bbox);
		}
	}

	/**
	 * Display Sensor region count
	 * 
	 * @param label         display label
	 * @param sensorRegions a collection of regions to be displayed
	 * @param bbox          the bounding box that contains all sensor regions
	 */
	public static void printSensor(String label, int sensorRegions[], BoundingBox bbox) {
		int squares = bbox.width * bbox.height;
		System.out.print(label + "; [" + bbox.width + " x " + bbox.height + "] = " + squares);
		if (squares != sensorRegions.length) {
			System.out.print("; array length is " + sensorRegions.length + "!");
		}
		System.out.println("");

		System.out.print("    ");
		for (int c = 0; c < bbox.width; c++) {
			System.out.print(String.format("  %2d", c));
		}
		System.out.println("");

		System.out.print("    ");
		for (int c = 0; c < bbox.width; c++) {
			System.out.print("  --");
		}
		System.out.println("");

		for (int r = 0; r < bbox.height; r++) {
			System.out.print(String.format(" %2d:", bbox.height - r - 1));
			for (int c = 0; c < bbox.width; c++) {
				System.out.print(String.format("  %2d", sensorRegions[r * bbox.width + c]));
			}
			System.out.println("");
		}

		System.out.print("    ");
		for (int c = 0; c < bbox.width; c++) {
			System.out.print("  --");
		}
		System.out.println("");
	}
}
