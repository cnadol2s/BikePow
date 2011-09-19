package bike.pow;

import android.util.Log;

public class Solver {

	// --- fields ---
	
	private double force;					// F
	private double powerOutput;				// P
	
	private double airResistanceLinear;		// A1
	private double airResistanceQuadratic;	// A2
	private double velocity;				// v
	private double headwind;				// h
	private double rollingFriction;			// r
	private double hillGrade;				// g
	private double hillGradeOffset;			// tilted device
	private double weightCyclist;			// wc
	private double weightEquipment;			// wm	
	private double transmissionEfficiency;	// t
	
	private double bearing;					// gamma
	private double windSpeed;
	private double windDegrees;				// angle
	private double alpha;					// angle: bearing to wind direction
	
	
	// --- Getter, Setter ---

	public double getForce() {
		return Helper.formatDouble(force);
	}

	public double getPowerOutput() {
		return Helper.formatDouble(powerOutput);
	}
	
	public double getAirResistanceLinear() {
		return Helper.formatDouble(airResistanceLinear);
	}

	public void setAirResistanceLinear(double airResistanceLinear) {
		this.airResistanceLinear = airResistanceLinear;
	}

	public double getAirResistanceQuadratic() {
		return Helper.formatDouble(airResistanceQuadratic);
	}

	public void setAirResistanceQuadratic(double airResistanceQuadratic) {
		this.airResistanceQuadratic = airResistanceQuadratic;
	}

	public double getVelocity() {
		return Helper.formatDouble(velocity);
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getHeadwind() {
		return Helper.formatDouble(headwind);
	}

	public double getRollingFriction() {
		return Helper.formatDouble(rollingFriction);
	}

	public void setRollingFriction(double rollingFriction) {
		this.rollingFriction = rollingFriction;
	}

	public double getHillGrade() {
		return Helper.formatDouble(hillGrade);
	}

	public void setHillGrade(double hillGrade) {
		this.hillGrade = hillGrade - this.getHillGradeOffset();
	}

	public double getWeightCyclist() {
		return Helper.formatDouble(weightCyclist);
	}

	public void setWeightCyclist(double weightCyclist) {
		this.weightCyclist = weightCyclist;
	}

	public double getWeightEquipment() {
		return Helper.formatDouble(weightEquipment);
	}

	public void setWeightRest(double weightRest) {
		this.weightEquipment = weightRest;
	}

	public void setTransmissionEfficiency(double transmissionEfficiency) {
		this.transmissionEfficiency = transmissionEfficiency;
	}

	public double getTransmissionEfficiency() {
		return Helper.formatDouble(transmissionEfficiency);
	}
	
	public void setBearing(double bearing) {
		this.bearing = bearing;
	}

	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public void setWindDegrees(int windDegrees) {
		this.windDegrees = windDegrees;
	}
	
	public double getHillGradeOffset() {
		return this.hillGradeOffset;
	}
	
	public void setHillGradeOffset(double hillGradeOffset) {
		this.hillGradeOffset = hillGradeOffset;
	}
	
	// --- Constructors ---
	
	public Solver() {
		this.airResistanceLinear = 0.43;		// Crouch (hands on drops, elbows locked)
		this.airResistanceQuadratic = 0.23;		// Crouch (hands on drops, elbows locked)
		this.velocity = 1;						// will be overridden
		this.headwind = 1;						// will be overridden
		this.rollingFriction = 0.0120;			// My Mountainbike
		this.hillGrade = 1;						// will be overridden
		this.hillGradeOffset = 0.0;				// will be overridden
		this.weightCyclist = 70;				// 70 kg by default 
		this.weightEquipment = 15;				// 15 kg by default
		this.transmissionEfficiency = 0.95;		// 95% efficiency
		
		this.bearing = 0.0;
		this.windSpeed = 0.0;
		this.windDegrees = 1.0;
		
		this.alpha = 0.0;
	}

	// --- Methods ---
	
	/*
	 * Wind from front direction = positive resistance output value
	 * Wind from back  direction = negative resistance output value
	 * 
	 * v_w = v_a * cos(alpha)
	 * 
	 * v_w:   wind speed the cyclist feels
	 * v_a:   wind speed
	 * alpha: angle between wind speed and bearing of cyclist
	 */
	public void computeHeadWind() {
		this.alpha = Solver.modTrig(this.windDegrees - this.bearing);
		
		if (this.headwind != Double.NaN) {
			// front and back wind already considered:
			// Cosinus provides positive and negative values
			// based on the current angle
			// Euclidean quadrant I,II: 	positive values
			// Euclidean quadrant III, IV: 	negative values
			this.headwind = this.windSpeed * Math.cos(angle2radian(alpha));

		} else {
			this.headwind = 0.0;
		}
	}
	
	public double computeForce() {
		double res = -1;
		
		/*
		 * Proprietary Code
		 * Removed in GitHub public repository
		 */
		
		return res;
	}

	public double computePowerOutput() {
		double res = -1;
		
		/*
		 * Proprietary Code
		 * Removed in GitHub public repository
		 */
		
		return res;
	}
	
	// --- Helper ---
	
	/*
	 * Extended modulo operator for trigonometric calculations
	 * fixed tailor is 360
	 * maps also negative angles to positive angles
	 * compared to an algebraic cyclic group
	 * Range of input values: -360 to +INF
	 * Example: modTrig(-5) = 355
	 */
	private static double modTrig(double k) {
		if (-360 <= k && k <= -1) {
			return 360 + k;
		} else if (0 <= k) {
			return k % 360;
		} else {
			return -1;
		}
	}
	
	/*
	 * Converts values from angle measure to radian measure
	 */
	private static double angle2radian(double angle) {
		return (Math.PI * (angle / 180));
	}

}
