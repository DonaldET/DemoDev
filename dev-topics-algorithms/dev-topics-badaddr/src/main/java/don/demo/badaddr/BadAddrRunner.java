package don.demo.badaddr;

import java.util.List;

/**
 * Adder checking done with a "runner" using this API.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public interface BadAddrRunner
{
    class Summation
    {
        public final String label;
        public final int n;
        public final double testSum;
        public final long elapsed;

        public Summation(String label, int n, double testSum, final long elapsed) {
            super();
            this.label = label;
            this.n = n;
            this.testSum = testSum;
            this.elapsed = elapsed;
        }

        @Override
        public String toString()
        {
            return "[Summation; 0x" + hashCode() + ";  label: " + label + ",  n: " + n + ",  testSum: " + testSum
                    + ",  elapsed: " + elapsed + "]";
        }
    }

    public abstract BadAddrRunner.Summation doOperation(final String label, final List<Double> testSequence,
            boolean isParallel);
}