package bike.pow.weather.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import bike.pow.Helper;
import bike.pow.weather.data.GoogleWeather_Data;

public class GoogleWeather_Handler extends DefaultHandler {
	
	private GoogleWeather_Data googleWeather_Data = new GoogleWeather_Data();
	
	public GoogleWeather_Data getParsedData() {
		return this.googleWeather_Data;
	}
	
	@Override
	public void startDocument() throws SAXException {
		this.googleWeather_Data = new GoogleWeather_Data();
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
		
		if (localName.equals("temp_c")) {
			String temp_c = atts.getValue("data");
			int temperature = Integer.parseInt(temp_c);
			this.googleWeather_Data.setTemperature(temperature);
		
		} else if (localName.equals("humidity")) {
			String humidity_long = atts.getValue("data");
			String humidity = Helper.getNumbersFromString(humidity_long).get(0);
			this.googleWeather_Data.setHumidity(humidity);
		
		} else if (localName.equals("wind_condition")) {
			String windCondition = atts.getValue("data");
			String windDirection = windCondition.split("\\s")[1];
			String windSpeedStringMph = Helper.getNumbersFromString(windCondition).get(0);
			double windSpeedKmh = 1.6093 * Double.parseDouble(windSpeedStringMph);
			this.googleWeather_Data.setWindDirection(windDirection);
			this.googleWeather_Data.setWindSpeed(Helper.formatDouble(windSpeedKmh));
			this.googleWeather_Data.setWindCondition(windCondition);
		}
	}
	
	/** Gets be called on closing tags like:
	 * </tag>
	 */
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		// Nothing to do
	}
	
	/** Gets be called on the following structure:
	 * <tag>characters</tag>
	 */
	public void characters(char ch[], int start, int length) {
		// Nothing to do
	}

}
