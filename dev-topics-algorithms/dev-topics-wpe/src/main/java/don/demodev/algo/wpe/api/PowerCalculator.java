package don.demodev.algo.wpe.api;

/**
 * A power calculator instance computes the estimate for energy extracted from
 * the wind, the amount of power actually generated from the wind, and the drop
 * in wind speed as a result of extracting energy from the moving cylinder of
 * air.
 * <p>
 * This computational mechanism assumes some number of generators are line up
 * one behind the other. It further assumes that the wind speed of the cylinder
 * of air is reduced by an amount related to the energy &quot;extracted&quit;
 * from the moving air, but is not the same as the energy actually
 * &quot;generated&quot; by the moving cylinder of air. It is, in fact,
 * significantly <em>less</em>.
 * <p>
 * The ration of energy generated to energy extracte is limited by the Betz
 * ration, a value approximately equal to <em>0.59</em>.
 * 
 * @author Donald Trummell
 */
public interface PowerCalculator {
//////// --- Wind Simulation Constructs

	/**
	 * Power generation state of a wind generator
	 * 
	 * @author Donald Trummell
	 */
	public static class PowerPoint {
		public int position; // generator position on grid
		public int generatorType; // enum-like value linking generator characteristics to position in wind farm.
		public long experimentTime; // t (milliseconds)
		public double speed; // m/s
		public double deltaEnergy; // watts (x10^6)
	};

//////// --- Turbine Power Simulation Constructs

	/**
	 * Number of polynomial coefficients in the Cp approximation function
	 */
	public static final int TPFNumCoef = 6;

	/**
	 * Physical factors that describe a wind generator power generation
	 * characteristics
	 * 
	 * @author Donald Trummell
	 */
	public static class TurbinePowerFactors {
		public double l; // Blade length
		public double a; // Blade swept area
		public double coef[]; // Coefficients of a polynomial approximation of Cp as function of wind speed'
		public double cutInSpeed; // Initial power generation wind speed
		public double cutOutSpeed; // Just exceeds greatest operational wind speed
	};

//////// --- Power Generation Constructs

	/**
	 * Wind characteristics, except speed, that affect power generation
	 * 
	 * @author Donald Trummell
	 */
	public static class WindFactors {
		public double rho; // Air density, kg/m^3
	};

	/**
	 * Display a TurbinePowerFactors instance
	 * 
	 * @param tpf the TurbinePowerFactors to display
	 */
	public abstract void display_TPF(TurbinePowerFactors tpf);

	/**
	 * Display a WindFactors instance
	 * 
	 * @param wf the WindFactors to display
	 */
	public abstract void display_WF(WindFactors wf);

	/**
	 * Display a PowerPoint and associated wind speed drop
	 * 
	 * @param powerPoint the PowerPoint instance to display
	 * @param drop       the associated wind speed drop
	 */
	public abstract void display_PPoint(PowerPoint powerPoint, double drop);

	/**
	 * Net power extracted from wind by a generator; computed as total power * Cp.
	 * This ignores the influence of turbulence on power generation (see <a href=
	 * "https://www.wind-watch.org/documents/how-turbulence-can-impact-power-performance/">this</a>
	 * entry.)
	 * 
	 * @param v  double value representing wind speed, units are M/S
	 * @param wf Wind_Factors reference, which defines wind characteristics
	 *           affecting power generation
	 * @param tp urbine_Power_Factors reference, which defines generator
	 *           characteristics affecting power generation
	 * @return the energy extract from the wind (Mega-watts)
	 */
	public abstract double power_extracted(double v, WindFactors wf, TurbinePowerFactors tp);

	/**
	 * Computes the drop in wind speed after passing through a generator, assuming
	 * the extracted power drops the velocity in the same manner (that is, using the
	 * same formula) as the generated power is influenced by wind velocity.
	 *
	 * Note: v = cube_root(2*p/(rho*cp)), but we use Cp == 1; because while we did
	 * not extract all the potential power of the wind, we still impeded the wind
	 * 
	 * @param pextracted a double value, in units of Mega-watts, that quantifies the
	 *                   power extracted from the wind by the previous generator
	 * @param wf         A Wind_Factors reference, which defines wind
	 *                   characteristics affecting power generation
	 * @return the degree of wind speed reduction in the cylinder of air passing
	 *         through a wind generator
	 */
	public abstract double wind_speed_drop(double pextracted, WindFactors wf);

	/**
	 * Calculates the usable energy generated at a grid location with a wind
	 * generator, and the wind speed drop associated with the generated energy. The
	 * calculation accepts an input point and stores the result in an output point,
	 * and returns the wind speed drop as the function value
	 * 
	 * @param input  the state of the wind generator before extracting energy from
	 *               the wind cyclinder
	 * @param wf     the wind factors affecting power generation at this location
	 * @param tp     the turbine power factors affecting power generation at this
	 *               location
	 * @param output the state of the wind generator after extracting energy from
	 *               the wind cyclinder
	 * @return the degree of wind speed reduction in the cylinder of air passing
	 *         through a wind generator
	 */
	public abstract double power_generated(PowerPoint input, WindFactors wf, TurbinePowerFactors tp, PowerPoint output);
}
