package bike.pow.weather.data;

public class GoogleWeather_Data {

	private int temperature = -1;
	private String humidity = "[humidity]";
	private String windCondition = "[windCondition]";
	private String windDirection = "[windDirection]";
	private double windSpeed = -1.0;

	// --- Compact ---
	
	public String toString() {
		return "Temperature: " + this.temperature + "° C" + "\n"
			+ "Humidity: " + this.humidity + "%" + "\n"
			+ "Wind Speed: " + this.windSpeed + " m/s" + "\n"
			+ "Wind Direction: " + this.windDirection;
	}
	
	// --- Getter / Setter ---
	
	public int getTemperature() {
		return this.temperature;
	}
	
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	
	public String getHumidity() {
		return this.humidity;
	}
	
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getWindCondition() {
		return this.windCondition;
	}
	
	public void setWindCondition(String windCondition) {
		this.windCondition = windCondition;
	}
	
	public String getWindDirection() {
		return this.windDirection;
	}
	
	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}
	
	public double getWindSpeed() {
		return this.windSpeed / 3.6;			// km/h -> m/s
	}
	
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

}
