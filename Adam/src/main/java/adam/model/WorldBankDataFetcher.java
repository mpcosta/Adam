package adam.model;

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

	private static final String BASE_COUNTRY_URL = "http://api.worldbank.org/countries",
			GDP = "NY.GDP.MKTP.CD",
			CPI = "FP.CPI.TOTL",
			BOP = "BN.CAB.XOKA.CD", 
			UNEMPLOYMENT = "SL.UEM.TOTL.ZS",
			INFLATION = "FP.CPI.TOTL.ZG",
			GOVERNMENT_SPENDING = "NE.CON.TETC.ZS",
			GOVERNMENT_CONSUMPTION = "NE.CON.GOVT.ZS";
	private static final int ITEMS_PER_PAGE = 1000;
	
	private static HashMap<String, Document> cachedDocuments;
	
	
	public WorldBankDataFetcher()
	{
		cachedDocuments = new HashMap<String, Document>();
	}
	
	
	
	
	/**
	 * Private method that loads the XML Document from a specific URL 
	 *
	 * @param  url  a string that represents the URL
	 * @return document - a document type with all the data gotten from the HTTP request
	 */
	private static Document loadDocument(String url) {
		
		if (!cachedDocuments.containsKey(url))
		{
			// Parser to get XML Data from URL Given
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			Document document = null;
			
			try { // Put this on separate private method
				DocumentBuilder dBuilder = builderFactory.newDocumentBuilder();
				document = dBuilder.parse(new URL(url).openStream());
				
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
			cachedDocuments.put(url, document);
		}
		return cachedDocuments.get(url);
	}
	
	
	
	/**
	 * Private method that gets the proper indicator code from a given string 
	 *
	 * @param  idName  a string that represents the common indicator name
	 * @return String - the proper Indicator Code for the wanted indicator
	 */
	/*
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
	}*/
	
	
	//TODO: Might Need this - Not Sure Yet
	/**
	 * Private method that gets the proper Country/Region Code from a given string 
	 *
	 * @param  idName  a string that represents the common country/region name
	 * @return String - the proper Id Code for the String Given
	 */
	private String getCountryCode(String countryName) {
		//TODO: For use with getIndicatorDataByYear
		Document document = loadDocument(BASE_COUNTRY_URL + "/all/?per_page=" + ITEMS_PER_PAGE);
		
		
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
	public HashMap<String, HashMap<Integer, String>> getIndicatorDataByYear(String indicatorName, int startYear, int endYear) {
		HashMap<String, HashMap<Integer, String>> indicatorData = new HashMap<String, HashMap<Integer, String>>();
		
		String url = BASE_COUNTRY_URL + "/all/indicators/" + indicatorName + "?per_page=" + ITEMS_PER_PAGE + "&date=" + startYear + ":" + endYear + "&format=xml";
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
		
		Document document = loadDocument(BASE_COUNTRY_URL + "/all/?per_page=" + ITEMS_PER_PAGE);
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
	 * Tests whether an Area code exists.
	 * @param code	The code of the Area.
	 * @return	True if a Area exists with the specified code.
	 */
	public boolean areaCodeExists(String code)
	{
		return !tagExistsInCountryDocument(code, "wb:error");
	}
	
	/**
	 * Tests whether an Area exists, and if that Area is a Country.
	 * @param code	The query code.
	 * @return	True if the specified code matches to a Country.
	 */
	public boolean countryCodeExists(String code)
	{
		return areaCodeExists(code) && !isRegion(code);
	}
	
	/**
	 * Tests whether an Area exists, and if that Area is a Country.
	 * @param code	The query code.
	 * @return	True if the specified code matches to a Region.
	 */
	public boolean regionCodeExists(String code)
	{
		return areaCodeExists(code) && isRegion(code);
	}
	
	/**
	 * Tests whether an area with the specified code is a region. NB: does not check that the code matches to an existing Area. 
	 * @param code	The area code.
	 * @return	True if the specified code matches to a region.
	 */
	public boolean isRegion(String code)
	{
		return !tagExistsInCountryDocument(code, "wb:capitalCity");
	}
	
	/**
	 * Gets the Area name from its code.
	 * @param code	The Area code.
	 * @return	The name of the Area.
	 */
	public String getNameFromCode(String code)
	{
		return getDataFromCode(code, "wb:name");
	}
	
	/**
	 * Gets the region of a Country from its code.
	 * @param code	The Country code.
	 * @return	The name of the region which Country is part of.
	 */
	public String getRegionFromCode(String code)
	{
		return getDataFromCode(code, "wb:region");
	}
	
	/**
	 * Gets the income level of a Country from its code.
	 * @param code	The Country code.
	 * @return	The income level for the specified Country.
	 */
	public String getIncomeLevelFromCode(String code)
	{
		return getDataFromCode(code, "wb:incomeLevel");
	}
	
	/**
	 * Gets the lending type of a Country from its code.
	 * @param code	The Country code.
	 * @return	The lending type for the specified Country.
	 */
	public String getLendingType(String code)
	{
		return getDataFromCode(code, "wb:lendingType");
	}
	
	/**
	 * Gets the capital city of a Country from its code.
	 * @param code	The Country code.
	 * @return	The name of the Countries capital city.
	 */
	public String getCapitalCity(String code)
	{
		return getDataFromCode(code, "wb:capitalCity");
	}
	
	/**
	 * Gets the longitude of a Country from its code.
	 * @param code	The Country code.
	 * @return	The longitude of the Country. 
	 */
	public double getLongitude(String code)
	{
		return Double.parseDouble(getDataFromCode(code, "wb:longitude"));
	}
	
	/**
	 * Gets the latitude of a Country from its code.
	 * @param code	The Country code.
	 * @return	The latitude of the Country.
	 */
	public double getLatitude(String code)
	{
		return Double.parseDouble(getDataFromCode(code, "wb:latitude"));
	}
	
	public double getGDP(String code, int year)
	{
		return Double.parseDouble(getIndicatorData(code, GDP, year));
	}
	
	public double getCPI(String code, int year)
	{
		return Double.parseDouble(getIndicatorData(code, CPI, year));
	}
	
	public double getBOP(String code, int year)
	{
		return Double.parseDouble(getIndicatorData(code, BOP, year));
	}
	
	public double getUnemployment(String code, int year)
	{
		return Double.parseDouble(getIndicatorData(code, UNEMPLOYMENT, year));
	}
	
	public double getInflation(String code, int year)
	{
		return Double.parseDouble(getIndicatorData(code, INFLATION, year));
	}
	
	public double getGovernmentSpending(String code, int year)
	{
		return Double.parseDouble(getIndicatorData(code, GOVERNMENT_SPENDING, year));
	}
	
	public double getGovernmentConsumption(String code, int year)
	{
		return Double.parseDouble(getIndicatorData(code, GOVERNMENT_CONSUMPTION, year));
	}
	
	private boolean tagExistsInCountryDocument(String code, String tagName)
	{
		Document document = getDocumentForArea(code);
		NodeList nodeList = document.getElementsByTagName(tagName);
		return nodeList.getLength() > 0;
	}
	
	private String getDataFromCode(String code, String tagName)
	{
		Document document = getDocumentForArea(code);
		NodeList nodeList = document.getElementsByTagName(tagName);
		return nodeList.item(0).getTextContent();
	}
	
	private Document getDocumentForArea(String code)
	{
		return loadDocument(BASE_COUNTRY_URL + "/" + code);
	}
	
	private String getIndicatorData(String code, String indicator, int year)
	{
		Document document = getIndicatorDocumentForArea(code, indicator, year);
		NodeList nodeList = document.getElementsByTagName("wb:value");
		return nodeList.item(0).getTextContent();
	}
	
	private Document getIndicatorDocumentForArea(String code, String indicator, int year)
	{
		return loadDocument(BASE_COUNTRY_URL + "/" + code + "/indicators/" + indicator + "?date=" + year);
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