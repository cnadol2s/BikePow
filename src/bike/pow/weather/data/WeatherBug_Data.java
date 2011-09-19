package bike.pow.weather.data;

import bike.pow.Helper;

public class WeatherBug_Data {

	private String stationName = "[stationName]";
	private String stationCityState = "[stationCityState]";
	private String stationCountry = "[stationCountry]";
	private double stationLatitude = -1;
	private double stationLongitude = -1;
	private int humidity = -1;
	private int pressure = -1;
	private int temperature = -1;
	private double windSpeed = -1.0;
	private String windDirection = "[windDirection]";
	private int windDirectionDegrees = -1;


	// --- Compact ---
	
	public String getStationShort() {
		return this.stationName + "\n"
			+ this.stationCityState + "\n"
			+ this.stationCountry;
	}
	
	public String getStation() {
		return "Weather Station: " + "\n"
			+ this.stationName + "\n"
			+ this.stationCityState + "\n"
			+ this.stationCountry + "\n"
			+ "Lat: " + this.stationLatitude + "\n"
			+ "Lng: " + this.stationLongitude;
	}
	
	public String getLiveWeather() {
		return "Temperature: " + this.temperature + "° C" + "\n"
			+ "Humidity: " + this.humidity + "%" + "\n"
			+ "Pressure: " + this.pressure + " mbar" + "\n"
			+ "Wind Speed: " + this.windSpeed + " m/s" + "\n"
			+ "Wind Direction: " + this.windDirection + "\n"
			+ "Wind Degrees: " + this.windDirectionDegrees + "°";
	}
	
	public String toString() {
		return this.getStation()
		+ "\n\n"
		+ this.getLiveWeather();
	}
	
	// --- Getter / Setter ---
	
	public String getStationName() {
		return this.stationName;
	}
	
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	
	public String getStationCityState() {
		return this.stationCityState;
	}
	
	public void setStationCityState(String stationCityState) {
		this.stationCityState = stationCityState;
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
	
	public int getHumidity() {
		return this.humidity;
	}
	
	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}
	
	public int getPressure() {
		return this.pressure;
	}
	
	public void setPressure(int pressure) {
		this.pressure = pressure;
	}
	
	public int getTemperature() {
		return this.temperature;
	}
	
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	
	public double getWindSpeed() {
		return this.windSpeed / 3.6;			// km/h -> m/s
	}

	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}
	
	public String getWindDirection() {
		return this.windDirection;
	}
	
	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}
	
	public int getWindDirectionDegrees() {
		return this.windDirectionDegrees;
	}

	public void setWindDirectionDegrees(int windDirectionDegrees) {
		this.windDirectionDegrees = windDirectionDegrees;
	}
	
	// --- Methods ---
	
	public double distanceToStation(double myLat, double myLng) {
		return Helper.getDistanceBetween(myLat, myLng,
				this.stationLatitude, this.stationLongitude);
	}

}
