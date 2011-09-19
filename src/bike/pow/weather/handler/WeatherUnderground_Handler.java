package bike.pow.weather.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import bike.pow.Helper;
import bike.pow.weather.data.WeatherUnderground_Data;

public class WeatherUnderground_Handler extends DefaultHandler {
	
	private boolean in_observation_location_tag = false;
	private boolean in_station_city_tag = false;
	private boolean in_station_country_tag = false;
	private boolean in_station_latitude_tag = false;
	private boolean in_station_longitude_tag = false;
	private boolean in_temperature_tag = false;
	private boolean in_humidity_tag = false;
	private boolean in_wind_direction_tag = false;
	private boolean in_wind_degrees_tag = false;
	private boolean in_wind_speed_tag = false;
	private boolean in_pressure_tag = false;
	
	private WeatherUnderground_Data weatherUnderground_Data = new WeatherUnderground_Data();
	
	public WeatherUnderground_Data getParsedData() {
		return this.weatherUnderground_Data;
	}
	
	@Override
	public void startDocument() throws SAXException {
		this.weatherUnderground_Data = new WeatherUnderground_Data();
	}
	
	@Override
	public void endDocument() throws SAXException {
		// Nothing to do
	}
	
	/** Gets be called on opening tags like:
	 * <tag>
	 * Can provide attribute(s), when XML was like:
	 * <tag attribute="attributeValue">
	 */
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
		if (localName.equals("observation_location")) {
			this.in_observation_location_tag = true;
			
		} else if (localName.equals("city") && this.in_observation_location_tag) {
			this.in_station_city_tag = true;
			
		} else if (localName.equals("country") && this.in_observation_location_tag) {
			this.in_station_country_tag = true;
			
		} else if (localName.equals("latitude") && this.in_observation_location_tag) {
			this.in_station_latitude_tag = true;
			
		} else if (localName.equals("longitude") && this.in_observation_location_tag) {
			this.in_station_longitude_tag = true;
			
		} else if (localName.equals("temp_c")) {
			this.in_temperature_tag = true;
			
		} else if (localName.equals("relative_humidity")) {
			this.in_humidity_tag = true;
			
		} else if (localName.equals("wind_dir")) {
			this.in_wind_direction_tag = true;
			
		} else if (localName.equals("wind_degrees")) {
			this.in_wind_degrees_tag = true;
			
		} else if (localName.equals("wind_mph")) {
			this.in_wind_speed_tag = true;
			
		} else if (localName.equals("pressure_mb")) {
			this.in_pressure_tag = true;
		}
	}
	
	/** Gets be called on closing tags like:
	 * </tag>
	 */
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		
		if (localName.equals("observation_location")) {
			this.in_observation_location_tag = false;
			
		} else if (localName.equals("city") && this.in_observation_location_tag) {
			this.in_station_city_tag = false;
			
		} else if (localName.equals("country") && this.in_observation_location_tag) {
			this.in_station_country_tag = false;
			
		} else if (localName.equals("latitude") && this.in_observation_location_tag) {
			this.in_station_latitude_tag = false;
			
		} else if (localName.equals("longitude") && this.in_observation_location_tag) {
			this.in_station_longitude_tag = false;
			
		} else if (localName.equals("temp_c")) {
			this.in_temperature_tag = false;
			
		} else if (localName.equals("relative_humidity")) {
			this.in_humidity_tag = false;
			
		} else if (localName.equals("wind_dir")) {
			this.in_wind_direction_tag = false;
			
		} else if (localName.equals("wind_degrees")) {
			this.in_wind_degrees_tag = false;
			
		} else if (localName.equals("wind_mph")) {
			this.in_wind_speed_tag = false;
			
		} else if (localName.equals("pressure_mb")) {
			this.in_pressure_tag = false;
		}
	}
	
	/** Gets be called on the following structure:
	 * <tag>characters</tag>
	 */
	public void characters(char ch[], int start, int length) {
		String curAttrValue = new String(ch, start, length);
		
		if (this.in_station_city_tag) {
			this.weatherUnderground_Data.setStationCity(curAttrValue);
			
		} else if (this.in_station_country_tag) {
			this.weatherUnderground_Data.setStationCountry(curAttrValue);
			
		} else if (this.in_station_latitude_tag) {
			double stationLatitude = Double.parseDouble(curAttrValue);
			this.weatherUnderground_Data.setStationLatitude(stationLatitude);
			
		} else if (this.in_station_longitude_tag) {
			double stationLongitude = Double.parseDouble(curAttrValue);
			this.weatherUnderground_Data.setStationLongitude(stationLongitude);
			
		} else if (this.in_temperature_tag) {
			double temperatureDouble = Double.parseDouble(curAttrValue);
			long temperature = Math.round(temperatureDouble);
			this.weatherUnderground_Data.setTemperature((int)temperature);
			
		} else if (this.in_humidity_tag) {
			String humidityString = Helper.getNumbersFromString(curAttrValue).get(0);
			int humidity = Integer.parseInt(humidityString);
			this.weatherUnderground_Data.setHumidity(humidity);
			
		} else if (this.in_wind_direction_tag) {
			this.weatherUnderground_Data.setWindDirection(curAttrValue);
			
		} else if (this.in_wind_degrees_tag) {
			int windDegrees = Integer.parseInt(curAttrValue);
			this.weatherUnderground_Data.setWindDegrees(windDegrees);
			
		} else if (this.in_wind_speed_tag) {
			double windSpeedKmh = 1.6093 * Double.parseDouble(curAttrValue);	// convert mph to kmh
			this.weatherUnderground_Data.setWindSpeed(Helper.formatDouble(windSpeedKmh));
			
		} else if (this.in_pressure_tag) {
			int pressure = Integer.parseInt(curAttrValue);
			this.weatherUnderground_Data.setPressure(pressure);
		}
	}

}
