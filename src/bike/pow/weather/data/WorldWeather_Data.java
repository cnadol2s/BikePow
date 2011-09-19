package bike.pow.weather.data;

public class WorldWeather_Data {
	
	private int temperature = -1;
	private double windSpeed = -1.0;
	private int windDegrees = -1;
	private String windDirection = "[windDirection]";
	private int humidity = -1;
	private int pressure = -1;
	
	// --- Compact ---

	public String toString() {
		return "Temperature: " + this.temperature + "° C" + "\n"
			+ "Wind Speed: " + this.windSpeed + " m/s" + "\n"
			+ "Wind Degrees: " + this.windDegrees + "°" + "\n"
			+ "Wind Direction: " + this.windDirection + "\n"
			+ "Humidity: " + this.humidity + "%" + "\n"
			+ "Pressure: " + this.pressure + " mbar";
	}
	
	// --- Getter / Setter ---
	
	public int getTemperature() {
		return this.temperature;
	}
	
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public double getWindSpeed() {
		return this.windSpeed;
	}
	
	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed / 3.6;			// km/h -> m/s
	}
	
	public int getWindDegrees() {
		return this.windDegrees;
	}

	public void setWindDegress(int windDegrees) {
		this.windDegrees = windDegrees;
	}

	public String getWindDirection() {
		return this.windDirection;
	}
	
	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
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

}
