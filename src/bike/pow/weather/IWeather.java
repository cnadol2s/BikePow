package bike.pow.weather;

import bike.pow.weather.data.GoogleWeather_Data;
import bike.pow.weather.data.WeatherBug_Data;
import bike.pow.weather.data.WeatherUnderground_Data;
import bike.pow.weather.data.WorldWeather_Data;


public interface IWeather {

	WeatherBug_Data getWeatherBugData(double lat, double lng);
	GoogleWeather_Data getGoogleWeatherData(double lat, double lng);
	WeatherUnderground_Data getWeatherUndergroundData(double lat, double lng);
	WorldWeather_Data getWorldWeatherData(double lat, double lng);
	void cancelWeatherTimers();
	void cancelWeatherTasks();
	void initWeatherTasks();
}
