package bike.pow.weather.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import bike.pow.Helper;
import bike.pow.weather.data.WeatherBug_Data;

public class WeatherBug_Handler extends DefaultHandler {

	private boolean in_stationName_tag = false;
	private boolean in_stationCityState_tag = false;
	private boolean in_stationCountry_tag = false;
	private boolean in_stationLatitude_tag = false;
	private boolean in_stationLongitude_tag = false;
	private boolean in_humidity_tag = false;
	private boolean in_pressure_tag = false;
	private boolean in_temperature_tag = false;
	private boolean in_windSpeed_tag = false;
	private boolean in_windDirection_tag = false;
	private boolean in_windDirectionDegrees_tag = false;
	
	private WeatherBug_Data weatherBug_Data = new WeatherBug_Data();
	
	public WeatherBug_Data getParsedData() {
		return this.weatherBug_Data;
	}
	
	@Override
	public void startDocument() throws SAXException {
		this.weatherBug_Data = new WeatherBug_Data();
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
		
		if (localName.equals("station")) {
			this.in_stationName_tag = true;
		}
		
		if (localName.equals("city-state")) {
			this.in_stationCityState_tag = true;
		}
		
		if (localName.equals("country")) {
			this.in_stationCountry_tag = true;
		}
		
		if (localName.equals("latitude")) {
			this.in_stationLatitude_tag = true;
		}
		
		if (localName.equals("longitude")) {
			this.in_stationLongitude_tag = true;
		}
		
		if (localName.equals("humidity")) {
			this.in_humidity_tag = true;
		}
		
		if (localName.equals("pressure")) {
			this.in_pressure_tag = true;
		}

		if (localName.equals("temp")) {
			this.in_temperature_tag = true;
		}
		if (localName.equals("wind-speed")) {
			this.in_windSpeed_tag = true;
		}
		if (localName.equals("wind-direction")) {
			this.in_windDirection_tag = true;
		}
		
		if (localName.equals("wind-direction-degrees")) {
			this.in_windDirectionDegrees_tag = true;
		}
	}
	
	/** Gets be called on closing tags like:
	 * </tag>
	 */
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

		if (localName.equals("station")) {
			this.in_stationName_tag = false;
		}
		
		if (localName.equals("city-state")) {
			this.in_stationCityState_tag = false;
		}
		
		if (localName.equals("country")) {
			this.in_stationCountry_tag = false;
		}
		
		if (localName.equals("latitude")) {
			this.in_stationLatitude_tag = false;
		}
		
		if (localName.equals("longitude")) {
			this.in_stationLongitude_tag = false;
		}
		
		if (localName.equals("humidity")) {
			this.in_humidity_tag = false;
		}
		
		if (localName.equals("pressure")) {
			this.in_pressure_tag = false;
		}
		
		if (localName.equals("temp")) {
			this.in_temperature_tag = false;
		}
		if (localName.equals("wind-speed")) {
			this.in_windSpeed_tag = false;
		}
		if (localName.equals("wind-direction")) {
			this.in_windDirection_tag = false;
		}
		
		if (localName.equals("wind-direction-degrees")) {
			this.in_windDirectionDegrees_tag = false;
		}
	}
	
	/** Gets be called on the following structure:
	 * <tag>characters</tag>
	 */
	public void characters(char ch[], int start, int length) {
		
		String curAttrValue = new String(ch, start, length);
		
		if (this.in_stationName_tag) {
			this.weatherBug_Data.setStationName(curAttrValue);
		}
		
		if (this.in_stationCityState_tag) {
			this.weatherBug_Data.setStationCityState(curAttrValue);
		}
		
		if (this.in_stationCountry_tag) {
			this.weatherBug_Data.setStationCountry(curAttrValue);
		}
		
		if (this.in_stationLatitude_tag) {
			double stationLatitude = Double.parseDouble(curAttrValue);
			this.weatherBug_Data.setStationLatitude(stationLatitude);
		}
		
		if (this.in_stationLongitude_tag) {
			double stationLongitude = Double.parseDouble(curAttrValue);
			this.weatherBug_Data.setStationLongitude(stationLongitude);
		}
		
		if (this.in_humidity_tag) {
			int humidity = Integer.parseInt(curAttrValue);
			this.weatherBug_Data.setHumidity(humidity);
		}
		
		if (this.in_pressure_tag) {
			double pressureDouble = Double.parseDouble(curAttrValue);
			long pressure = Math.round(pressureDouble);
			this.weatherBug_Data.setPressure((int)pressure);
		}
		
		if (this.in_temperature_tag) {
			double tempDouble = Double.parseDouble(curAttrValue);
			long temperature = Math.round(tempDouble);
			this.weatherBug_Data.setTemperature((int)temperature);
		}
		if (this.in_windSpeed_tag) {
			double speed = Double.parseDouble(curAttrValue);
			this.weatherBug_Data.setWindSpeed(Helper.formatDouble(speed));
		}
		if (this.in_windDirection_tag) {
			this.weatherBug_Data.setWindDirection(curAttrValue);
		}
		
		if (this.in_windDirectionDegrees_tag) {
			int windDirectionDegrees = Integer.parseInt(curAttrValue);
			this.weatherBug_Data.setWindDirectionDegrees(windDirectionDegrees);
		}
	}

}
