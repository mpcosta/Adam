package adam.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



public class WorldBankDataFetcher {
/**
	 * The indicators specified from the world bank for each macroeconomic topic 
	 */
	private static final String BASE_COUNTRY_URL = "http://api.worldbank.org/countries",
			BASE_INDICATOR_URL = "http://api.worldbank.org/indicators",
			GDP = "NY.GDP.MKTP.CD",
			CPI = "FP.CPI.TOTL",
			BOP = "BN.CAB.XOKA.CD", 
			UNEMPLOYMENT = "SL.UEM.TOTL.ZS",
			INFLATION = "FP.CPI.TOTL.ZG",
			GOVERNMENT_SPENDING = "NE.CON.TETC.ZS",
			GOVERNMENT_CONSUMPTION = "NE.CON.GOVT.ZS",
			
			EX_INDICATOR_DATA_NOT_FOUND = "Indicator data could not be retrived specified year",
			
			OFFLINE_CACHING_PATH = "res/offline-data/";
	private static final int ITEMS_PER_PAGE = 1000;
	
	private static final Pattern INDICATOR_RANGE_QUERY = Pattern.compile("api.worldbank.org_countries_(..)_indicators_(.*)_date=(\\d\\d\\d\\d)_(\\d\\d\\d\\d).*");
	
	private static HashMap<String, Document> cachedDocuments = new HashMap<String, Document>();
	private static HashMap<String, Integer> areaCodeToDocumentIndex = new HashMap<String, Integer>(); 
	
	public WorldBankDataFetcher()
	{
		
	}
	
	private static String urlToFilename(String url)
	{
		url = url.replace("http://", "");
		url = url.replace('/', '_');
		url = url.replace(':', '_');
		url = url.replace('?', '_');
		return OFFLINE_CACHING_PATH + url;
	}
	
	/**
	 * Private method that loads the XML Document from a specific URL 
	 *
	 * @param  url  a string that represents the URL
	 * @return document - a document type with all the data gotten from the HTTP request
	 */
	private static Document loadDocument(String url) throws IOException, ParserConfigurationException, SAXException
	{
		if (!cachedDocuments.containsKey(url))
		{
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			Document document = null;
			DocumentBuilder dBuilder = builderFactory.newDocumentBuilder();
			try
			{
				InputStream stream = new URL(url).openStream();
				document = dBuilder.parse(stream);
				String filename = urlToFilename(url);
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
				writer.write(documentAsString(document));
				writer.close();
			}
			catch (IOException e)
			{
				document = loadOfflineDocument(dBuilder, url);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			cachedDocuments.put(url, document);
		} else {
			// Grab the XML Document we need
			// URL Could be 2 types - BASE_COUNTRY_URL + "/all?per_page=" + ITEMS_PER_PAGE OR BASE_INDICATOR_URL + "/" + indicator
		}
		return cachedDocuments.get(url);
	}
	
	private static Document loadOfflineDocument(DocumentBuilder dBuilder, String url) throws IOException, SAXException
	{
		File file = new File(urlToFilename(url));
		return dBuilder.parse(file);
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
	
	
	
	/**
	 *  Gets all the data for every counter for the wanted indicator
	 *  If the value for a current year is an EmptyString it means there is no data yet for that year 
	 *
	 * @param  indicatorName  a String with the (NAME) of the the indicator we need data 
	 * @param  startYear an Integer with the start year (inclusive)
	 * @param  endYear an Integer with the end year (inclusive)
	 * @return indicatorData - corresponds to the data desired with it's associated year
	 */
	/*
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
	*/
	
	
	
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
	/*
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
	*/
	/**
	 * A getter method to get the area code from the name from the world bank data 
	 * @param name
	 * @return the base country or null 
	 */
	public String getAreaCodeFromName(String name)
	{
		Document document = null;
		try
		{
			document = loadDocument(BASE_COUNTRY_URL + "/all?per_page=" + ITEMS_PER_PAGE);
		}
		catch (Exception e)
		{
			return "";
		}
		NodeList names = document.getElementsByTagName("wb:name"),
				codes = document.getElementsByTagName("wb:iso2Code");
		for (int i = 0; i < names.getLength(); i++)
		{
			Node item = names.item(i);
			if (item.getTextContent().equals(name))
				return codes.item(i).getTextContent();
		}
		return null;
	}
	/**
	 * Estimates the name from the fragments inputed by the user from the world bank
	 * @param fragment
	 * @return
	 */
	public ArrayList<String> estimateNamesFromFragment(String fragment)
	{
		Document document = null;
		try
		{
			document = loadDocument(BASE_COUNTRY_URL + "/all?per_page=" + ITEMS_PER_PAGE);
		}
		catch (Exception e)
		{
			return new ArrayList<String>();
		}
		NodeList namesNodeList = document.getElementsByTagName("wb:name"),
				codeNodeList = document.getElementsByTagName("wb:iso2Code");
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0; i < namesNodeList.getLength(); i++)
		{
			String name = namesNodeList.item(i).getTextContent(),
					code = codeNodeList.item(i).getTextContent().toUpperCase();
			if (name.startsWith(fragment) || code.startsWith(fragment) || fragment.startsWith(code))
				names.add(name);
		}
		names.sort((s1, s2) -> s1.length() - s2.length());
		return names;
	}
	/**
	 * Gets all the names in a node list from the world bank
	 * @return the names
	 */
	public ArrayList<String> getAllNames()
	{
		Document document;
		try
		{
			document = loadDocument(BASE_COUNTRY_URL + "/all?per_page=" + ITEMS_PER_PAGE);
		}
		catch (Exception e)
		{
			return new ArrayList<String>();
		}
		NodeList namesNodeList = document.getElementsByTagName("wb:name");
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0; i < namesNodeList.getLength(); i++)
		{
			names.add(namesNodeList.item(i).getTextContent());
		}
		return names;
	}
	/**
	 * The caches of all the names being retrieved from the world bank 
	 */
	public void cacheAllNames()
	{
		Document document = null;
		try
		{
			document = loadDocument(BASE_COUNTRY_URL + "/all?per_page=" + ITEMS_PER_PAGE);
		}
		catch (Exception e)
		{
			return;
		}
		NodeList codes = document.getElementsByTagName("wb:iso2Code");
		for (int i = 0; i < codes.getLength(); i++)
		{
			areaCodeToDocumentIndex.put(codes.item(i).getTextContent(), i);
		}
	}
	
	/**
	 * Tests whether an Area code exists.
	 * @param code	The code of the Area.
	 * @return	True if a Area exists with the specified code.
	 */
	public boolean areaCodeExists(String code)
	{
		code = code.toUpperCase();
		Document document = getDocumentForAllAreas();
		NodeList codes = document.getElementsByTagName("wb:iso2Code");
		for (int i = 0; i < codes.getLength(); i++)
		{
			if (code.equals(codes.item(i).getTextContent()))
				return true;
		}
		return false;
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
		return getDataFromCode(code, "wb:capitalCity").equals("");
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
	
	/**
	 * Gets the Indicator Description.
	 * @param code	The Indicator code.
	 * @return	The Indicator Description.
	 */
	private String getIndicatorInfo(String indicator)
	{
		return getIndicatorDescription(indicator); 
	}
	/**
	 * Gets the indicators 
	 * @param indicatorID
	 * @return the indicators 
	 */
	private String getIndicatorCodeFromID(int indicatorID)
	{
		switch (indicatorID)
		{
			case Area.GDP:
				return GDP;
			case Area.CPI:
				return CPI;
			case Area.BOP:
				return BOP;
			case Area.GOVERNMENT_CONSUMPTION:
				return GOVERNMENT_CONSUMPTION;
			case Area.GOVERNMENT_SPENDING:
				return GOVERNMENT_SPENDING;
			case Area.INFLATION:
				return INFLATION;
			case Area.UNEMPLOYMENT:
				return UNEMPLOYMENT;
		}
		return null;
	}
	/**
	 * Gets the data of every indicator 
	 * @param code
	 * @param indicator
	 * @param startYear
	 * @param endYear
	 * @return the indicator data 
	 */
	public HashMap<Integer, Double> getIndicatorData(String code, int indicator, int startYear, int endYear) throws RequestException
	{
		HashMap<Integer, Double> data = new HashMap<Integer, Double>();
		String indicatorCode = getIndicatorCodeFromID(indicator);
		Document document = null;
		try
		{
			document = loadDocument(BASE_COUNTRY_URL + "/" + code + "/indicators/" + indicatorCode + "?date=" + startYear + ":" + endYear + "&per_page=" + ITEMS_PER_PAGE);
		}
		catch (Exception e)
		{
			File folder = new File(OFFLINE_CACHING_PATH);
			File[] files = folder.listFiles();
			int minStart = 0, maxEnd = 0;
			for (File file : files)
			{
				Matcher matcher = INDICATOR_RANGE_QUERY.matcher(file.getName());
				if (matcher.matches() && matcher.group(1).equals(code) && matcher.group(2).equals(indicatorCode))
				{
					int start = Integer.parseInt(matcher.group(3)),
							end = Integer.parseInt(matcher.group(4));
					if (start <= startYear && end >= endYear)
						return getIndicatorData(code, indicator, start, end);
					if (end - start > maxEnd - minStart)
					{
						minStart = start;
						maxEnd = end;
					}
				}
			}
			throw new RequestException(RequestException.NO_CONNECTION, "Cached data exists for the years " + minStart + " to " + maxEnd + ".");
		}
		NodeList dates = document.getElementsByTagName("wb:date"), values = document.getElementsByTagName("wb:value");
		for (int i = 0; i < values.getLength(); i++)
		{
			String textContent = values.item(i).getTextContent();
			if (!textContent.equals(""))
				data.put(Integer.parseInt(dates.item(i).getTextContent()), Double.parseDouble(textContent));
		}
		return data;
	}
	
	/**
	 * Gets a specific data from a Code
	 * @param code	The Area code.
	 * @param tagName the tagName of the data we want.
	 * @return	A String with the data we wanted.
	 */
	private String getDataFromCode(String code, String tagName)
	{
		code = code.toUpperCase();
		Document document = getDocumentForAllAreas();
		NodeList nodeList = document.getElementsByTagName(tagName);
		if (!areaCodeToDocumentIndex.containsKey(code))
			cacheAllNames();
		int index = areaCodeToDocumentIndex.get(code);
		return nodeList.item(index).getTextContent();
	}
	
	
	/**
	 * Gets the Document of an Area from its code.
	 * @param code	The Area code.
	 * @return	The Document for that Area
	 */
	private Document getDocumentForArea(String code)
	{
		try
		{
			return loadDocument(BASE_COUNTRY_URL + "/" + code);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Gets the Indicator Information.
	 * @param indicator The Indicator Code
	 * @return	The Indicator Brief Description.
	 */
	private String getIndicatorDescription(String indicator) {
		Document document = getIndicatorDocumentForInfo(indicator);
		NodeList nodeList = document.getElementsByTagName("wb:sourceNote");
		return nodeList.item(0).getTextContent();
	}
	
	/**
	 * Gets the Indicator Document based on a set of arguments
	 * @param indicator	The Indicator Code.
	 * @return	The Document with the Indicator Info.
	 */
	private Document getIndicatorDocumentForInfo(String indicator)
	{
		try
		{
			return loadDocument(BASE_INDICATOR_URL + "/" + indicator);
		}
		catch (Exception e)
		{
			return null;
		}
		
	}
	
	private Document getDocumentForAllAreas()
	{
		try
		{
			return loadDocument(BASE_COUNTRY_URL + "/all?per_page=" + ITEMS_PER_PAGE);
		}
		catch (Exception e)
		{
			return null;
		}
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
	
	private static String documentAsString(Document document) throws Exception
	{
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.MEDIA_TYPE, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		StringWriter stringWriter = new StringWriter();
		transformer.transform(new DOMSource(document),  new StreamResult(stringWriter));
		return stringWriter.toString();
	}
}
