package bike.pow.sensor;

public interface ISensor {

	double getHillgradeRaw();
	double computeAverageValue();
	double computeMedianValue();
}
