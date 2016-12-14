package adam.model;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Area
{
	/**
	 * Seven macroeconomic topics the user can search 
	 */
	public static final int GDP = 0, CPI = 1, BOP = 2, UNEMPLOYMENT = 3, INFLATION = 4, GOVERNMENT_SPENDING = 5, GOVERNMENT_CONSUMPTION = 6;
	
	protected static final String EX_CODE_INVALID = "Area code provided was not valid";
	/**
	 * Calling the static data Fetcher from the World Bank 
	 * Calling the codes and names for a specific area 
	 */
	protected static WorldBankDataFetcher dataFetcher = new WorldBankDataFetcher();
	protected static HashMap<Code, Area> areas = new HashMap<Code, Area>();
	
	private static HashMap<String, String> namesToCodes = new HashMap<String, String>();
	private static HashMap<String, ArrayList<String>> fragmentsToNamesEstimates = new HashMap<String, ArrayList<String>>();
	private static ArrayList<String> allNames = new ArrayList<String>();
	
	protected Code code;
	/**
	 * Private hash maps for the indicators and macroeconomic topics 
	 */
	private String name;
	private HashMap<Integer, HashMap<Integer, Double>> indicators;
	private HashMap<Integer, Integer> indicators_minYear, indicators_maxYear;
	private HashMap<Integer, Double> gdp, cpi, bop, unemployment, inflation, governmentSpending, governmentConsumption;
	/**
	 * Initialises the code for the specified area 
	 * @param c The code that matches to a country 
	 * @throws Exception if the area of the code does not exist 
	 */
	protected Area(String c) throws Exception
	{
		code = new Code(c);
		initialise();
	}
	/**
	 * Initialises the code for the area 
	 * @param c the code for the country 
	 */
	protected Area(Code c)
	{
		code = c;
		initialise();
	}
	/**
	 * Initialises all data needed for a chart/graph to appear 
	 */
	private void initialise()
	{
		name = null;
		indicators = new HashMap<Integer, HashMap<Integer, Double>>();
		indicators_minYear = new HashMap<Integer, Integer>();
		indicators_maxYear = new HashMap<Integer, Integer>();
		
		gdp = new HashMap<Integer, Double>();
		cpi = new HashMap<Integer, Double>();
		bop = new HashMap<Integer, Double>();
		unemployment = new HashMap<Integer, Double>();
		inflation = new HashMap<Integer, Double>();
		governmentSpending = new HashMap<Integer, Double>();
		governmentConsumption = new HashMap<Integer, Double>();
	}
	
	/**
	 * Gets the code for the Area.
	 * @return	The Area code.
	 */
	public String getCode()
	{
		return code.get();
	}
	
	/**
	 * Gets the name for the Area.
	 * @return	The Area name.
	 */
	public String getName()
	{
		if (name == null)
			name = dataFetcher.getNameFromCode(code.get());
		return name;
	}
	/**
	 * Gets the data for the Area 
	 * @param indicator
	 * @param startYear
	 * @param endYear
	 * @return the data for the area 
	 */
	public HashMap<Integer, Double> getIndicatorData(int indicator, int startYear, int endYear) throws RequestException
	{
		if (endYear < startYear)
			throw new RequestException(RequestException.INVALID_RANGE);
		if (!indicators.containsKey(indicator))
			indicators.put(indicator, new HashMap<Integer, Double>());
		HashMap<Integer, Double> cached = indicators.get(indicator);
		if (!indicators_minYear.containsKey(indicator) || !indicators_maxYear.containsKey(indicator) || indicators_minYear.get(indicator) > startYear || indicators_maxYear.get(indicator) < endYear)
		{
			HashMap<Integer, Double> toCache = dataFetcher.getIndicatorData(code.get(), indicator, startYear, endYear);
			cached.putAll(toCache);
			indicators_minYear.put(indicator, startYear);
			indicators_maxYear.put(indicator, endYear);
		}
		HashMap<Integer, Double> data = new HashMap<Integer, Double>();
		for (int year = startYear; year <= endYear; year++)
		{
			if (cached.containsKey(year))
				data.put(year, cached.get(year));
		}
		return data;
	}
	/**
	 * Gets the area from the code 
	 * @param c the code for the country 
	 * @return the code of the area 
	 * @throws Exception if the area code does not exist 
	 */
	public static Area getAreaFromCode(String c) throws Exception
	{
		Code code = new Code(c);
		if (!areas.containsKey(code))
		{
			if (dataFetcher.isRegion(c))
				areas.put(code, Region.getRegion(c));
			else
				areas.put(code, Country.getCountry(c));
		}
		return areas.get(code);
	}
	/**
	 * Gets the name of the area code 
	 * @param name
	 * @return the name of the area code 
	 */
	public static String getAreaCodeFromName(String name)
	{
		if (!namesToCodes.containsKey(name))
			namesToCodes.put(name, dataFetcher.getAreaCodeFromName(name));
		return namesToCodes.get(name);
	}
	/**
	 * Estimates the possible names from the fragment inputed 
	 * @param fragment of the possible options 
	 * @return possible names 
	 */
	public static ArrayList<String> estimateNamesFromFragment(String fragment)
	{
		if (!fragmentsToNamesEstimates.containsKey(fragment))
			fragmentsToNamesEstimates.put(fragment, dataFetcher.estimateNamesFromFragment(fragment));
		return fragmentsToNamesEstimates.get(fragment);
	}
	/**
	 * A getter for all the names in the data fetcher 
	 * @return an array list of all the names
	 */
	public static ArrayList<String> getAllNames()
	{
		if (allNames.size() == 0)
			allNames = dataFetcher.getAllNames();
		return new ArrayList<String>(allNames);
	}
	/**
	 * A method to store the names that will be used 
	 */
	public static void cacheAllNames()
	{
		dataFetcher.cacheAllNames();
	}
	
	/**
	 * Tests whether the given code matches to an Area.
	 * @param code	The query code.
	 * @return	True if an Area exists with the specified code.
	 */
	public static boolean codeIsValid(String code)
	{
		try
		{
			return codeIsValid(new Code(code));
		}
		catch (Exception e)
		{
			return false;
		}
	}
	/**
	 * Boolean to check whether the code exists or not 
	 * @param code for the country 
	 * @return the code if it exists or not 
	 */
	protected static boolean codeIsValid(Code code)
	{
		return areas.containsKey(code) || dataFetcher.areaCodeExists(code.get());
	}
	/**
	 * A protected method for the codes length to check whether its valid or not 
	 * @author hagerabdo
	 * Containing a boolean to see whether the codes are equal 
	 */
	protected static class Code
	{
		private static final int CODE_LENGTH = 2;
		
		private String code;
		
		public Code(String c) throws Exception
		{
			if (c.length() != CODE_LENGTH)
				throw new Exception(EX_CODE_INVALID + ": " + c + "; Code length must be " + CODE_LENGTH);
			code = c.toLowerCase();
		}
		
		/**
		 * Gets the string representation of the code.
		 * @return	The code.
		 */
		public String get()
		{
			return code;
		}
		
		public int hashCode()
		{
			return code.hashCode();
		}
		
		public boolean equals(Object o)
		{
			if (!(o instanceof Code))
				return super.equals(o);
			return code.equals(((Code)o).code);
		}
	}
}
