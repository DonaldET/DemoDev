package demo.algo.sensor;

import java.util.List;

import demo.algo.sensor.SensorMonitoring.Rectangle;

/**
 * An implementation finds the area of exposed sensor regions with the desired
 * number of radiation bursts, each area defined by a rectangular region.
 * 
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public interface ExposureAreaFinder {
	public abstract int findArea(List<? extends Rectangle> exposedRectangle, int k);
}
