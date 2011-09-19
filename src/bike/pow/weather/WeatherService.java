/*
 * Local Service with Callback
 */

package bike.pow.weather;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import bike.pow.weather.data.GoogleWeather_Data;
import bike.pow.weather.data.WeatherBug_Data;
import bike.pow.weather.data.WeatherUnderground_Data;
import bike.pow.weather.data.WorldWeather_Data;
import bike.pow.weather.handler.GoogleWeather_Handler;
import bike.pow.weather.handler.WeatherBug_Handler;
import bike.pow.weather.handler.WeatherUnderground_Handler;
import bike.pow.weather.handler.WorldWeather_Handler;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class WeatherService extends Service {
	
	private final static String API_CODE_WEATHERBUG = "Removed in GitHub";
	private final static String API_CODE_WORLDWEATHER = "Removed in GitHub";
	private final static long TIMER_PERIOD = 180000L;	// 3 min
	private Timer googleWeatherTimer;
	private Timer weatherBugTimer;
	private Timer weatherUndergroundTimer;
	private Timer worldWeatherTimer;
	protected TimerTask googleWeatherTask;
	protected TimerTask weatherBugTask;
	protected TimerTask weatherUndergroundTask;
	protected TimerTask worldWeatherTask;
	private double lat;
	private double lng;
	private GoogleWeather_Data mGoogleWeather_Data;
	private WeatherBug_Data mWeatherBug_Data;
	private WeatherUnderground_Data mWeatherUnderground_Data;
	private WorldWeather_Data mWorldWeather_Data;
	private IBinder binder = new WeatherServiceBinder();
	
	public class WeatherServiceBinder extends Binder implements IWeather {

		@Override
		public GoogleWeather_Data getGoogleWeatherData(double lat, double lng) {
			WeatherService.this.lat = lat;
			WeatherService.this.lng = lng;
			
			if (WeatherService.this.googleWeatherTimer == null) {
				WeatherService.this.googleWeatherTimer = new Timer("GoogleWeatherTimer", false);
				WeatherService.this.googleWeatherTimer.scheduleAtFixedRate(googleWeatherTask, TIMER_PERIOD, TIMER_PERIOD);
				return WeatherService.this.getGoogleWeatherData(lat, lng);
			
			} else {
				return WeatherService.this.mGoogleWeather_Data;
			}
		}

		@Override
		public WeatherBug_Data getWeatherBugData(double lat, double lng) {
			WeatherService.this.lat = lat;
			WeatherService.this.lng = lng;
			
			if (WeatherService.this.weatherBugTimer == null) {
				WeatherService.this.weatherBugTimer = new Timer("WeatherBugTimer", false);
				WeatherService.this.weatherBugTimer.scheduleAtFixedRate(weatherBugTask, TIMER_PERIOD, TIMER_PERIOD);
				return WeatherService.this.getWeatherBugData(lat, lng);
			
			} else {
				return WeatherService.this.mWeatherBug_Data;
			}
		}

		@Override
		public WeatherUnderground_Data getWeatherUndergroundData(double lat, double lng) {
			WeatherService.this.lat = lat;
			WeatherService.this.lng = lng;
			
			if (WeatherService.this.weatherUndergroundTimer == null) {
				WeatherService.this.weatherUndergroundTimer = new Timer("WeatherUndergroundTimer", false);
				WeatherService.this.weatherUndergroundTimer.scheduleAtFixedRate(weatherUndergroundTask, TIMER_PERIOD, TIMER_PERIOD);
				return WeatherService.this.getWeatherUndergroundData(lat, lng);
			
			} else {
				return WeatherService.this.mWeatherUnderground_Data;
			}
		}

		@Override
		public WorldWeather_Data getWorldWeatherData(double lat, double lng) {
			WeatherService.this.lat = lat;
			WeatherService.this.lng = lng;
			
			if (WeatherService.this.worldWeatherTimer == null) {
				WeatherService.this.worldWeatherTimer = new Timer("WorldWeatherTimer", false);
				WeatherService.this.worldWeatherTimer.scheduleAtFixedRate(worldWeatherTask, TIMER_PERIOD, TIMER_PERIOD);
				return WeatherService.this.getWorldWeatherData(lat, lng);
			
			} else {
				return WeatherService.this.mWorldWeather_Data;
			}
		}

		@Override
		public void initWeatherTasks() {
			WeatherService.this.initWeatherTasks();
		}
		
		@Override
		public void cancelWeatherTasks() {
			WeatherService.this.cancelWeatherTasks();
		}

		@Override
		public void cancelWeatherTimers() {
			WeatherService.this.cancelWeatherTimers();
		}
		
	}
	
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		this.initWeatherTasks();
		return WeatherService.this.binder;
	}
	
	public void initWeatherTasks() {
		this.googleWeatherTask = new TimerTask() {
			@Override
			public void run() {
				WeatherService.this.getGoogleWeatherData(WeatherService.this.lat, WeatherService.this.lng);
			}
		};
		this.weatherBugTask = new TimerTask() {
			@Override
			public void run() {
				WeatherService.this.getWeatherBugData(WeatherService.this.lat, WeatherService.this.lng);
			}
		};
		this.weatherUndergroundTask = new TimerTask() {
			@Override
			public void run() {
				WeatherService.this.getWeatherUndergroundData(WeatherService.this.lat, WeatherService.this.lng);
			}
		};
		this.worldWeatherTask = new TimerTask() {
			@Override
			public void run() {
				WeatherService.this.getWorldWeatherData(WeatherService.this.lat, WeatherService.this.lng);
			}
		};
	}
	
	public void cancelWeatherTimers() {
		this.googleWeatherTimer.cancel();
		this.googleWeatherTimer = null;
		
		this.weatherBugTimer.cancel();
		this.weatherBugTimer = null;
		
		this.weatherUndergroundTimer.cancel();
		this.weatherUndergroundTimer = null;
		
		this.worldWeatherTimer.cancel();
		this.worldWeatherTimer = null;
	}
	
	public void cancelWeatherTasks() {
		try {
			this.googleWeatherTask.cancel();
			this.googleWeatherTask = null;
			
			this.weatherBugTask.cancel();
			this.weatherBugTask = null;
			
			this.weatherUndergroundTask.cancel();
			this.weatherUndergroundTask = null;
			
			this.worldWeatherTask.cancel();
			this.worldWeatherTask = null;
		
		} catch (RuntimeException e) {
			Log.e("MyException", "WeatherService: cancelWeatherTasks");
		}
		
	}
	

	public GoogleWeather_Data getGoogleWeatherData(double lat, double lng) {
		int lat_e6 = (int) (lat * 1000000);
    	int lng_e6 = (int) (lng * 1000000);
    	
    	try {
        	URL url = new URL("http://www.google.com/ig/api?weather=,,," + lat_e6 + "," + lng_e6 + "&hl=en");
        	SAXParserFactory spf = SAXParserFactory.newInstance();
        	SAXParser sp = spf.newSAXParser();
        	XMLReader xr = sp.getXMLReader();
        	GoogleWeather_Handler googleWeather_Handler = new GoogleWeather_Handler();
        	xr.setContentHandler(googleWeather_Handler);
        	xr.parse(new InputSource(url.openStream()));
        	this.mGoogleWeather_Data = googleWeather_Handler.getParsedData();
        
    	} catch (Exception e) {
        	e.printStackTrace();
        }
    	
    	return this.mGoogleWeather_Data;
	}

	public WeatherBug_Data getWeatherBugData(double lat, double lng) {
		
		try {
        	URL url = new URL("http://" + API_CODE_WEATHERBUG +
        			".api.wxbug.net/getLiveWeatherRSS.aspx?ACode=" + API_CODE_WEATHERBUG +
        			"&lat=" + lat + 
        			"&long=" + lng +
        			"&UnitType=1&OutputType=1");
        	SAXParserFactory spf = SAXParserFactory.newInstance();
        	SAXParser sp = spf.newSAXParser();
        	XMLReader xr = sp.getXMLReader();
        	WeatherBug_Handler weatherBug_Handler = new WeatherBug_Handler();
        	xr.setContentHandler(weatherBug_Handler);
        	xr.parse(new InputSource(url.openStream()));
        	this.mWeatherBug_Data = weatherBug_Handler.getParsedData();
        	
        } catch (Exception e) {
        	Log.e("WeatherService", "WeatherBug: " + e.getMessage());
        	e.printStackTrace();
        }
        
    	return this.mWeatherBug_Data;
	}

	public WeatherUnderground_Data getWeatherUndergroundData(double lat, double lng) {
		
		try {
        	URL url = new URL("http://api.wunderground.com/auto/wui/geo/WXCurrentObXML/index.xml?query=" + lat + "," + lng);
        	SAXParserFactory spf = SAXParserFactory.newInstance();
        	SAXParser sp = spf.newSAXParser();
        	XMLReader xr = sp.getXMLReader();
        	WeatherUnderground_Handler weatherUnderground_Handler = new WeatherUnderground_Handler();
        	xr.setContentHandler(weatherUnderground_Handler);
        	xr.parse(new InputSource(url.openStream()));
        	this.mWeatherUnderground_Data = weatherUnderground_Handler.getParsedData();
        	
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
    	return this.mWeatherUnderground_Data;
	}

	public WorldWeather_Data getWorldWeatherData(double lat, double lng) {
		
		try {
        	URL url = new URL("http://www.worldweatheronline.com/feed/weather.ashx?q="
        			+ lat + "," + lng
        			+ "&format=xml&num_of_days=1&key="
        			+ API_CODE_WORLDWEATHER);
        	SAXParserFactory spf = SAXParserFactory.newInstance();
        	SAXParser sp = spf.newSAXParser();
        	XMLReader xr = sp.getXMLReader();
        	WorldWeather_Handler worldWeather_Handler = new WorldWeather_Handler();
        	xr.setContentHandler(worldWeather_Handler);
        	xr.parse(new InputSource(url.openStream()));
        	this.mWorldWeather_Data = worldWeather_Handler.getParsedData();
        
		} catch (Exception e) {
        	Log.e("WeatherService", "WorldWeather: " + e.getMessage());
        	e.printStackTrace();
        }
		
    	return this.mWorldWeather_Data;
	}

}
