package demo.amazon.statemachines;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleBinaryOperator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;

/**
 * Illustrate Strategy, Factory, and Decorator design patterns and
 * interface-based programming.
 * <p>
 * <strong>Expected Output</strong>
 * 
 * <pre>
 * <code>
 * Strategy/Factory Example:
 * arithSum[-1, 2, 5, 8, 11]=25.0
 * geoSum[1, 2, 4, 8, 16, 32]=63.0
 * 
 * Bridge Example:
 * IBM arithSum[-1, 2, 5, 8, 11]=25.0
 * IBM geoSum[1, 2, 4, 8, 16, 32]=63.0
 * 
 * Decorator Example:
 * Arith_1:  [-1.0, 2.0, 5.0, 8.0, 11.0] SUM: 25.0 MIN: -1.0; MAX: 14.0
 * Geo_1  :  SUM: 63.0 MIN: 1.0; MAX: 32.0 [1.0, 2.0, 4.0, 8.0, 16.0, 32.0]
 * 
 * *****
 * Junit Examples:
 * JUnit version 4.12
 * ......
 * Time: 0.005
 * 
 * OK (6 tests)
 * </code>
 * </pre>
 */
public class Solution {
	public static enum SeriesType {
		ARITHMETIC, GEOMETRIC
	}

	//
	// STRATEGY Pattern: Multiple implementations for series summation

	public static interface Adder {
		public abstract double summationOf(double commonValue, double a0, int nterms);

		//
		// Factory Pattern: Hide details of implementation, allows runtime
		// implementation selection

		public static class Factory {
			public static Adder getArith() {
				return new ArithSum();
			}

			public static Adder getGeo() {
				return new GeoSum();
			}
		}
	}

	private static class ArithSum implements Adder {
		@Override
		public double summationOf(double commonIncrement, double a0, int nterms) {
			int incrSum = nterms % 2 == 0 ? (nterms / 2) * (nterms - 1) : nterms * ((nterms - 1) / 2);
			return nterms * a0 + incrSum * commonIncrement;
		}
	}

	private static class GeoSum implements Adder {
		@Override
		public double summationOf(double commonRatio, double a0, int nterms) {
			return a0 * ((1.0 - Math.pow(commonRatio, nterms)) / (1.0 - commonRatio));
		}
	}

	//
	// BRIDGE Pattern: Implement summation using Adder private classes. Note: The
	// main difference between an adaptor and a bridge pattern, is that a bridge
	// pattern serves to decouple an abstraction class from its implementation, and
	// an adaptor pattern converts the interface between classes with less
	// inheritance.
	//

	public static interface Summation {
		public abstract double seriesSum(double a0, int nterms, double factor, SeriesType type);

		public static class IBMOperator implements Summation {

			private static class IBMArith implements Summation {
				private static Adder sumImp = Adder.Factory.getArith();

				@Override
				public double seriesSum(double a0, int nterms, double factor, SeriesType type) {
					assert type == SeriesType.ARITHMETIC;
					return sumImp.summationOf(factor, a0, nterms);
				}
			}

			private static class IBMGeo implements Summation {
				private static Adder sumImp = Adder.Factory.getGeo();

				@Override
				public double seriesSum(double a0, int nterms, double factor, SeriesType type) {
					assert type == SeriesType.GEOMETRIC;
					return sumImp.summationOf(factor, a0, nterms);
				}
			}

			//
			// Loading strategies by series type

			private static final Map<SeriesType, Summation> impl = new HashMap<SeriesType, Summation>();
			static {
				impl.put(SeriesType.ARITHMETIC, new IBMArith());
				impl.put(SeriesType.GEOMETRIC, new IBMGeo());
			}

			@Override
			public double seriesSum(double a0, int nterms, double factor, SeriesType type) {
				return impl.get(type).seriesSum(a0, nterms, factor, type);
			}
		}
	}

	//
	// DECORATOR Pattern: Nest optional sequence manipulations in different orders

	public interface SequenceDescription {
		public abstract String describeSequence(double a0, int nterms, double factor, SeriesType type);
	}

	public static abstract class SequenceDecorator implements SequenceDescription {
		protected final SequenceDescription sequence;

		protected SequenceDecorator(SequenceDescription sequence) {
			super();
			this.sequence = sequence;
		}

		protected SequenceDecorator() {
			this(null);
		}

		public String describeSequence(double a0, int nterms, double factor, SeriesType type) {
			return "";
		}
	}

	public static class GenerateSequence extends SequenceDecorator {
		public GenerateSequence(SequenceDescription sequence) {
			super(sequence);
		}

		public GenerateSequence() {
			this(null);
		}

		@Override
		public String describeSequence(double a0, int nterms, double factor, SeriesType type) {
			String leader = sequence == null ? "" : sequence.describeSequence(a0, nterms, factor, type);
			DoubleBinaryOperator op = type == SeriesType.ARITHMETIC ? (x, y) -> x + y : (x, y) -> x * y;
			return leader + createSeq(a0, nterms, factor, op);
		}

		private String createSeq(double a0, int nterms, double factor, DoubleBinaryOperator action) {
			StringBuilder sb = new StringBuilder();
			double t = a0;
			sb.append(" [");
			sb.append(t);
			for (int i = 1; i < nterms; i++) {
				sb.append(", ");
				t = action.applyAsDouble(t, factor);
				sb.append(t);
			}
			sb.append("]");
			return sb.toString();
		}
	}

	public static class SumSequence extends SequenceDecorator {
		public SumSequence(SequenceDescription sequence) {
			super(sequence);
		}

		public SumSequence() {
			this(null);
		}

		@Override
		public String describeSequence(double a0, int nterms, double factor, SeriesType type) {
			String leader = sequence == null ? "" : sequence.describeSequence(a0, nterms, factor, type);
			Summation.IBMOperator s = new Summation.IBMOperator();
			return leader + " SUM: " + Double.toString(s.seriesSum(a0, nterms, factor, type));
		}
	}

	public static class RangeSequence extends SequenceDecorator {
		public RangeSequence(SequenceDescription sequence) {
			super(sequence);
		}

		public RangeSequence() {
			this(null);
		}

		@Override
		public String describeSequence(double a0, int nterms, double factor, SeriesType type) {
			double[] r = seqMinMax(a0, nterms, factor, type);
			String leader = sequence == null ? "" : sequence.describeSequence(a0, nterms, factor, type);
			return leader + " MIN: " + Double.toString(r[0]) + "; MAX: " + Double.toString(r[1]);
		}

		private double[] seqMinMax(double a0, int nterms, double factor, SeriesType type) {
			double aN = type == SeriesType.ARITHMETIC ? a0 + nterms * factor : a0 * Math.pow(factor, nterms - 1);
			return new double[] { Math.min(a0, aN), Math.max(a0, aN) };
		}
	}

	/////////////////
	// JUnit tests //
	/////////////////

	@Test
	public void testArith() {
		Assert.assertEquals("Arith differs", 25.0, Adder.Factory.getArith().summationOf(3.0, -1.0, 5), 1.E-16);
	}

	@Test
	public void testGeo() {
		Assert.assertEquals("Geo differs", 63.0, Adder.Factory.getGeo().summationOf(2.0, 1.0, 6), 1.E-16);
	}

	@Test
	public void testIBMArith() {
		Summation a = new Summation.IBMOperator();
		Assert.assertEquals("IBM Arith differs", 25.0, a.seriesSum(-1.0, 5, 3.0, SeriesType.ARITHMETIC), 1.E-16);
	}

	@Test
	public void testIBMGeo() {
		Summation a = new Summation.IBMOperator();
		Assert.assertEquals("IBM Geo differs", 63.0, a.seriesSum(1.0, 6, 2.0, SeriesType.GEOMETRIC), 1.E-16);
	}

	@Test
	public void testArith1Decorator() {
		SequenceDecorator sd = new RangeSequence(new SumSequence(new GenerateSequence()));
		Assert.assertEquals("Arith1 Decorator Differs", " [-1.0, 2.0, 5.0, 8.0, 11.0] SUM: 25.0 MIN: -1.0; MAX: 14.0",
				sd.describeSequence(-1.0, 5, 3.0, SeriesType.ARITHMETIC));
	}

	@Test
	public void testGeo1Decorator() {
		SequenceDecorator sd = new GenerateSequence(new RangeSequence(new SumSequence()));
		Assert.assertEquals("Geo1 Decorator Differs", " SUM: 63.0 MIN: 1.0; MAX: 32.0 [1.0, 2.0, 4.0, 8.0, 16.0, 32.0]",
				sd.describeSequence(1.0, 6, 2.0, SeriesType.GEOMETRIC));
	}

	//
	// Test driver
	public static void main(String[] args) {
		System.out.println("Strategy/Factory Example:");
		System.out.println("arithSum[-1, 2, 5, 8, 11]=" + Adder.Factory.getArith().summationOf(3.0, -1.0, 5));
		System.out.println("geoSum[1, 2, 4, 8, 16, 32]=" + Adder.Factory.getGeo().summationOf(2.0, 1.0, 6));

		System.out.println("\nBridge Example:");
		Summation opr = new Summation.IBMOperator();
		System.out.println("IBM arithSum[-1, 2, 5, 8, 11]=" + opr.seriesSum(-1.0, 5, 3.0, SeriesType.ARITHMETIC));
		System.out.println("IBM geoSum[1, 2, 4, 8, 16, 32]=" + opr.seriesSum(1.0, 6, 2.0, SeriesType.GEOMETRIC));

		System.out.println("\nDecorator Example:");
		SequenceDecorator sd = new RangeSequence(new SumSequence(new GenerateSequence()));
		System.out.println("Arith_1: " + sd.describeSequence(-1.0, 5, 3.0, SeriesType.ARITHMETIC));
		sd = new GenerateSequence(new RangeSequence(new SumSequence()));
		System.out.println("Geo_1  : " + sd.describeSequence(1.0, 6, 2.0, SeriesType.GEOMETRIC));

		System.out.println("\n*****\nJunit Examples:");
		JUnitCore.main("demo.wework.patterns.Solution");
	}
}
