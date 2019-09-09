package demo.algo.sensor.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.algo.sensor.SensorMonitoring;
import demo.algo.sensor.SensorMonitoring.ExposureSession;
import demo.algo.sensor.SensorMonitoring.Rectangle;

public class TestCountingSort {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNone() {
		List<Rectangle> rectangles = new ArrayList<Rectangle>();

		Rectangle[] output = SensorMonitoring.countingSort(rectangles, SensorMonitoring.XY_UPPER_BOUND);
		Assert.assertNotNull("sort output null", output);
		Assert.assertEquals("length differs", rectangles.size(), output.length);
	}

	@Test
	public void testOnlyOne() {
		ExposureSession es = GenerateDataAndTest.getSessionData(0);
		List<Rectangle> rectangles = es.sessions;

		List<Rectangle> exp = new ArrayList<Rectangle>();
		exp.add(rectangles.get(0));

		Rectangle[] output = SensorMonitoring.countingSort(rectangles, SensorMonitoring.XY_UPPER_BOUND);
		Assert.assertNotNull("sort output null", output);
		Assert.assertEquals("length differs", rectangles.size(), output.length);
		for (int i = 0; i < rectangles.size(); i++) {
			Assert.assertEquals("rectangle[" + i + "] differs", exp.get(i), output[i]);
		}
	}

	@Test
	public void test2() {
		ExposureSession es = GenerateDataAndTest.getSessionData(1);
		List<Rectangle> rectangles = es.sessions;

		List<Rectangle> exp = new ArrayList<Rectangle>();
		exp.add(rectangles.get(1));
		exp.add(rectangles.get(0));

		Rectangle[] output = SensorMonitoring.countingSort(rectangles, SensorMonitoring.XY_UPPER_BOUND);
		Assert.assertNotNull("sort output null", output);
		Assert.assertEquals("length differs", rectangles.size(), output.length);
		for (int i = 0; i < rectangles.size(); i++) {
			Assert.assertEquals("rectangle[" + i + "] differs", exp.get(i), output[i]);
		}
	}

	@Test
	public void test4() {
		ExposureSession es = GenerateDataAndTest.getSessionData(2);
		List<Rectangle> rectangles = es.sessions;

		List<Rectangle> exp = new ArrayList<Rectangle>();
		exp.add(rectangles.get(3));
		exp.add(rectangles.get(2));
		exp.add(rectangles.get(0));
		exp.add(rectangles.get(1));

		Rectangle[] output = SensorMonitoring.countingSort(rectangles, SensorMonitoring.XY_UPPER_BOUND);
		Assert.assertNotNull("sort output null", output);
		Assert.assertEquals("length differs", rectangles.size(), output.length);
		for (int i = 0; i < rectangles.size(); i++) {
			Assert.assertEquals("rectangle[" + i + "] differs", exp.get(i), output[i]);
		}
	}

	@Test
	public void test7() {
		ExposureSession es = GenerateDataAndTest.getSessionData(5);
		List<Rectangle> rectangles = es.sessions;

//		session5.add(new Rectangle(9, 3, 13, 7));
//		session5.add(new Rectangle(1, 2, 3, 8));
//		session5.add(new Rectangle(2, 3, 6, 7));
//		session5.add(new Rectangle(0, 0, 8, 4));
//		session5.add(new Rectangle(12, 6, 15, 8));
//		session5.add(new Rectangle(11, 5, 14, 9));
//		session5.add(new Rectangle(0, 6, 8, 10));

		List<Rectangle> exp = new ArrayList<Rectangle>();
		exp.add(rectangles.get(6));
		exp.add(rectangles.get(3));
		exp.add(rectangles.get(1));
		exp.add(rectangles.get(2));
		exp.add(rectangles.get(0));
		exp.add(rectangles.get(5));
		exp.add(rectangles.get(4));

		Rectangle[] output = SensorMonitoring.countingSort(rectangles, SensorMonitoring.XY_UPPER_BOUND);
		Assert.assertNotNull("sort output null", output);
		Assert.assertEquals("length differs", rectangles.size(), output.length);
		for (int i = 0; i < rectangles.size(); i++) {
			Assert.assertEquals("rectangle[" + i + "] differs", exp.get(i), output[i]);
		}
	}
}
