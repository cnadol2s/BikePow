package bike.pow;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.util.Log;

public class Helper {

	private static DecimalFormat df = new DecimalFormat("#.###");
	
	public static double formatDouble(double raw) {
		String res = df.format(raw).replaceAll(",", ".");
		return Double.parseDouble(res);
	}
	
	public static List<String> getNumbersFromString(String msg) {
		String[] raw = msg.split("\\D");
		List<String> res = new ArrayList<String>();
		for (int i=0; i < raw.length; i++) {
			if (! raw[i].equals("")) {
				res.add(raw[i]);
			}
		}
		return res;
	}
	
	public static double getDistanceBetween(double startLat, double startLng,
			double endLat, double endLng) {
		float[] results = new float[5];
		double dist_km = -1;
		String dist_km_string;
		try {
			Location.distanceBetween(startLat, startLng,
					endLat, endLng, results);
			dist_km_string = df.format(results[0] / 1000.0).replaceAll(",", ".");
			dist_km = Double.parseDouble(dist_km_string);
		} catch (IllegalArgumentException e) {
			Log.e("Helper", e.getMessage());
		}
		return dist_km;
	}
	
	/*
	 * New in this version: Method moved from SensorService to Helper
	 * Output example: 0.12 which means 12% hill grade
	 * Note: In portrait mode: positive hill grade = negative roll value
	 * Update: Portrait mode deactivated in manifest
	 */
	public static double angle2HillGrade(float angle_rad) {
		double angle_deg = -angle_rad * Math.PI / 180;
		return Math.tan(angle_deg);	
	}
}
