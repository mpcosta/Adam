package adam;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class WorldBankDataFetcher {

	
	public WorldBankDataFetcher () {}
	
	
	
	
	/**
	 * Private method that loads the XML Document from a specific URL 
	 *
	 * @param  url  a string that represents the URL
	 * @return document - a document type with all the data gotten from the HTTP request
	 */
	private static Document loadDocument(String url) {
		
		// Parser to get XML Data from URL Given
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		Document document = null;
		
		try { // Put this on separate private method
			DocumentBuilder dBuilder = builderFactory.newDocumentBuilder();
			document = dBuilder.parse(new URL(url).openStream());
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return document;
	}
	
	
	
	/**
	 * Private method that gets the proper indicator code from a given string 
	 *
	 * @param  idName  a string that represents the common indicator name
	 * @return String - the proper Indicator Code for the wanted indicator
	 */
	private String getIndicatorCode(String idName) {
		// TODO: Could also load from the XML indicator if needed - Recommended usage -> if designed for all indicators
		switch (idName) {
			case "GDP": case "gross domestic product ": return "NY.GDP.MKTP.CD";
			case "CPI": case "consumer price index": return "FP.CPI.TOTL";
			case "Unemployment": return "SL.UEM.TOTL.ZS";
			case "BOP": case "balance of payments": return "BN.CAB.XOKA.CD";
			case "inflation": return "FP.CPI.TOTL.ZG";
			// TODO: Investment must be chosen based on the options wanted 
			case "government spending": return "NE.CON.TETC.ZS";
			case "government consumption": return "NE.CON.GOVT.ZS";
			// TODO: Add More IDs as soon as we need them
		}
		return "Invalid Indicator Code";
	}
	
	
	//TODO: Might Need this - Not Sure Yet
	/**
	 * Private method that gets the proper Country/Region Code from a given string 
	 *
	 * @param  idName  a string that represents the common country/region name
	 * @return String - the proper Id Code for the String Given
	 */
	private String getCountryCode(String countryName) {
		//TODO: For use with getIndicatorDataByYear
		Document document = loadDocument("http://api.worldbank.org/countries/all/?per_page=1000");
		
		
		return null;
	}
	
	
	
	/**
	 *  Gets all the data for every counter for the wanted indicator
	 *  If the value for a current year is an EmptyString it means there is no data yet for that year 
	 *
	 * @param  indicatorName  a String with the (NAME) of the the indicator we need data 
	 * @param  startYear an Integer with the start year (inclusive)
	 * @param  endYear an Integer with the end year (inclusive)
	 * @return indicatorData - corresponds to the data desired with it's associated year
	 */
	public HashMap<String, HashMap<Integer, String>> getIndicatorDataByYear (String indicatorName, int startYear, int endYear) {
		HashMap<String, HashMap<Integer, String>> indicatorData = new HashMap<String, HashMap<Integer, String>>();
		
		String url = "http://api.worldbank.org/countries/all/indicators/" + getIndicatorCode(indicatorName) + "?per_page=1000&date=" + startYear + ":" + endYear + "&format=xml";
		Document document = loadDocument(url);
			
		// Get All the Nodes into Node List
		NodeList rootNodes = document.getElementsByTagName("wb:data");
		
		//Create HashMap for <Date and Value>
		HashMap<Integer, String> valueData = new HashMap<Integer, String>();
		
		// Get All the Information Needed from all the Nodes
		for (int i = 0; i < rootNodes.getLength() - 1; i++) { 
			
			// Grab Date and Respective Value
			valueData.put(Integer.parseInt(document.getElementsByTagName("wb:date").item(i).getTextContent()),  document.getElementsByTagName("wb:value").item(i).getTextContent());
			
			//Add necessary data to HashMap <Country, HashMap<Date and Value>>
			indicatorData.put(document.getElementsByTagName("wb:country").item(i).getTextContent(), valueData);	
		}
		// Return HashMap<Country, HashMap<Year, Value>>	
		return indicatorData;
	}
	
	
	
	
	//TODO: Method to get information about all indicators -> Cache Information
	//TODO: Since there is a lot of indicators maybe only get the ones we are going to use 
	//http://api.worldbank.org/indicators/NY.GDP.MKTP.CD?per_page=1000&date=2015:2016&format=xml
	
	
	
	
	

	

	/**
	 * Get all info from all countries in a HashMap
	 * Data represented in order by: 
	 * (region, adminregion, incomeLevel, lendingType, capital City, longitude latitude)
	 *
	 * @return allCInfoData - Return the HashMap with all the data from each country
	 */
	public HashMap<String, String[]> getAllCountriesInfo() {
		// HashMap< CountryName, StringArray with all the info
		HashMap<String, String[]> allCInfoData = new HashMap<String, String[]>();
		
		Document document = loadDocument("http://api.worldbank.org/countries/all/?per_page=1000");
		NodeList rootNodes = document.getElementsByTagName("wb:country");
		
		// Get All the Information Needed from all the Nodes
		for (int i = 0; i < rootNodes.getLength(); i++) { 
			// Add all specific country data in the String[]
			String[] data = new String[7];
			data[0] = document.getElementsByTagName("wb:region").item(i).getTextContent();
			data[1] = document.getElementsByTagName("wb:adminregion").item(i).getTextContent();
			data[2] = document.getElementsByTagName("wb:incomeLevel").item(i).getTextContent();
			data[3] = document.getElementsByTagName("wb:lendingType").item(i).getTextContent();
			data[4] = document.getElementsByTagName("wb:capitalCity").item(i).getTextContent();
			data[5] = document.getElementsByTagName("wb:longitude").item(i).getTextContent();
			data[6] = document.getElementsByTagName("wb:latitude").item(i).getTextContent();
			
			// Grab Data and Put it on HashMap					
			allCInfoData.put(document.getElementsByTagName("wb:name").item(i).getTextContent(), data);
		}
		return allCInfoData;
	}
	
	
	
	
	
	/**
	 *  Private void method to print the whole Document type of variable
	 *  Check if all the data is on the Document
	 *
	 * @param  doc  a Document type of variable
	 * @param  out  an OutputStream type
	 */
	private static void printDocument(Document doc, OutputStream out) {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(new DOMSource(doc), 
			new StreamResult(new OutputStreamWriter(out, "UTF-8")));
						
		} catch (UnsupportedEncodingException | TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	
	
	
	
}
