package demo.algo.sensor.test;

import demo.algo.sensor.MonitorExposureByArea;
import demo.algo.sensor.SensorMonitoring.BoundingBox;

public class DumpSensorData {

	private static final MonitorExposureByArea mapper = new MonitorExposureByArea();

	public static void main(String[] args) {
		System.out.println("\nDump of Test Data Sensor Regions");
		for (int id = 0; id < GenerateDataAndTest.testCases; id++) {
			System.out.println("\n-----------------------");
			GenerateDataAndTest.testData(id, mapper, false);
			printSensor("\nData[" + id + "]", mapper.getSensorRegions(), mapper.getBbox());
		}
	}

	/**
	 * Display Sensor region count
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
