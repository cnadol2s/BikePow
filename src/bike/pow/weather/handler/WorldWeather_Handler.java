package bike.pow.weather.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import bike.pow.Helper;
import bike.pow.weather.data.WorldWeather_Data;

public class WorldWeather_Handler extends DefaultHandler {

	private boolean in_temperature_tag = false;
	private boolean in_wind_speed_tag = false;
	private boolean in_wind_degrees_tag = false;
	private boolean in_wind_direction_tag = false;
	private boolean in_humidity_tag = false;
	private boolean in_pressure_tag = false;
	
	private WorldWeather_Data worldWeather_Data = new WorldWeather_Data();
	
	public WorldWeather_Data getParsedData() {
		return this.worldWeather_Data;
	}
	
	@Override
	public void startDocument() throws SAXException {
		this.worldWeather_Data = new WorldWeather_Data();
	}
	
	@Override
	public void endDocument() throws SAXException {
		// Nothing to do
	}
	
	/** Gets be called on opening tags like:
	 * <tag>
	 * Can provide attribute(s), when xml was like:
	 * <tag attribute="attributeValue">
	 */
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		
		if (localName.equals("temp_C")) {
			this.in_temperature_tag = true;
			
		} else if (localName.equals("windspeedKmph")) {
			this.in_wind_speed_tag = true;
			
		} else if (localName.equals("winddirDegree")) {
			this.in_wind_degrees_tag = true;
			
		} else if (localName.equals("winddir16Point")) {
			this.in_wind_direction_tag = true;
			
		} else if (localName.equals("humidity")) {
			this.in_humidity_tag = true;
			
		} else if (localName.equals("pressure")) {
			this.in_pressure_tag = true;
		}
	}
	
	/** Gets be called on closing tags like:
	 * </tag>
	 */
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

		if (localName.equals("temp_C")) {
			this.in_temperature_tag = false;
			
		} else if (localName.equals("windspeedKmph")) {
			this.in_wind_speed_tag = false;
			
		} else if (localName.equals("winddirDegree")) {
			this.in_wind_degrees_tag = false;
			
		} else if (localName.equals("winddir16Point")) {
			this.in_wind_direction_tag = false;
			
		} else if (localName.equals("humidity")) {
			this.in_humidity_tag = false;
			
		} else if (localName.equals("pressure")) {
			this.in_pressure_tag = false;
		}
	}
	
	/** Gets be called on the following structure:
	 * <tag>characters</tag>
	 */
	public void characters(char ch[], int start, int length) {
		
		String curAttrValue = new String(ch, start, length);
		
		if (this.in_temperature_tag) {
			int temperature = Integer.parseInt(curAttrValue);
			this.worldWeather_Data.setTemperature(temperature);
			
		} else if (this.in_wind_speed_tag) {
			int windSpeed = Integer.parseInt(curAttrValue);
			this.worldWeather_Data.setWindSpeed(Helper.formatDouble(windSpeed));
			
		} else if (this.in_wind_degrees_tag) {
			int windDegrees = Integer.parseInt(curAttrValue);
			this.worldWeather_Data.setWindDegress(windDegrees);
			
		} else if (this.in_wind_direction_tag) {
			this.worldWeather_Data.setWindDirection(curAttrValue);
			
		} else if (this.in_humidity_tag) {
			int humidity = Integer.parseInt(curAttrValue);
			this.worldWeather_Data.setHumidity(humidity);
			
		} else if (this.in_pressure_tag) {
			int pressure = Integer.parseInt(curAttrValue);
			this.worldWeather_Data.setPressure(pressure);
		}
	}

}
