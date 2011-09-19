package bike.pow.weather.data;

import bike.pow.Helper;

public class WeatherUnderground_Data {

	private String stationCity = "[stationCity]";
	private String stationCountry = "[stationCountry]";
	private double stationLatitude = -1;
	private double stationLongitude = -1;
	private int temperature = -1;
	private int humidity = -1;
	private String windDirection = "[windDirection]";
	private int windDegrees = -1;
	private double windSpeed = -1.0;
	private int pressure = -1;

	// --- Compact ---
	
	public String toString() {
		return this.getStation() + "\n\n" + this.getLiveWeather();
	}
	
	public String getStationShort() {
		return this.stationCity + "\n"
			+ this.stationCountry;
	}
	
	public String getStation() {
		return "Station: \n"
			+ this.stationCity + "\n"
			+ this.stationCountry + "\n"
			+ "Lat: " + this.stationLatitude + "\n"
			+ "Lng: " + this.stationLongitude;
	}
	
	public String getLiveWeather() {
		return "Temperature: " + this.temperature + "° C" + "\n"
			+ "Humidity: " + this.humidity + "%" + "\n"
			+ "Wind Direction: " + this.windDirection + "\n"
			+ "Wind Degrees: " + this.windDegrees + "°" + "\n"
			+ "Wind Speed: " + this.windSpeed + " m/s" + "\n"
			+ "Pressure: " + this.pressure + " mbar";
	}
	
	// --- Getter / Setter ---
	
	public String getStationCity() {
		return this.stationCity;
	}
	
	public void setStationCity(String stationCity) {
		this.stationCity = stationCity;
	}

	public String getStationCountry() {
		return this.stationCountry;
	}
	
	public void setStationCountry(String stationCountry) {
		this.stationCountry = stationCountry;
	}

	public double getStationLatitude() {
		return this.stationLatitude;
	}
	
	public void setStationLatitude(double stationLatitude) {
		this.stationLatitude = stationLatitude;
	}

	public double getStationLongitude() {
		return this.stationLongitude;
	}
	
	public void setStationLongitude(double stationLongitude) {
		this.stationLongitude = stationLongitude;
	}

	public int getTemperature() {
		return this.temperature;
	}
	
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getHumidity() {
		return this.humidity;
	}
	
	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public String getWindDirection() {
		return this.windDirection;
	}
	
	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	public int getWindDegrees() {
		return this.windDegrees;
	}
	
	public void setWindDegrees(int windDegrees) {
		this.windDegrees = windDegrees;
	}

	public double getWindSpeed() {
		return this.windSpeed / 3.6;			// km/h -> m/s
	}
	
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public int getPressure() {
		return this.pressure;
	}
	
	public void setPressure(int pressure) {
		this.pressure = pressure;
	}
	
	// --- Methods ---
	
	public double distanceToStation(double myLat, double myLng) {
		return Helper.getDistanceBetween(myLat, myLng,
				this.stationLatitude, this.stationLongitude);
	}

}
