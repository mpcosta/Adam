package adam.model;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Area
{
	public static final int GDP = 0, CPI = 1, BOP = 2, UNEMPLOYMENT = 3, INFLATION = 4, GOVERNMENT_SPENDING = 5, GOVERNMENT_CONSUMPTION = 6;
	
	protected static final String EX_CODE_INVALID = "Area code provided was not valid";
	
	protected static WorldBankDataFetcher dataFetcher = new WorldBankDataFetcher();
	protected static HashMap<Code, Area> areas = new HashMap<Code, Area>();
	
	private static HashMap<String, String> namesToCodes = new HashMap<String, String>();
	private static HashMap<String, ArrayList<String>> fragmentsToNamesEstimates = new HashMap<String, ArrayList<String>>();
	private static ArrayList<String> allNames = new ArrayList<String>();
	
	protected Code code;
	
	private String name;
	private HashMap<Integer, HashMap<Integer, Double>> indicators;
	private HashMap<Integer, Integer> indicators_minYear, indicators_maxYear;
	private HashMap<Integer, Double> gdp, cpi, bop, unemployment, inflation, governmentSpending, governmentConsumption;
	
	protected Area(String c) throws Exception
	{
		code = new Code(c);
		initialise();
	}
	protected Area(Code c)
	{
		code = c;
		initialise();
	}
	
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
	
	public HashMap<Integer, Double> getIndicatorData(int indicator, int startYear, int endYear)
	{
		if (!indicators.containsKey(indicator))
			indicators.put(indicator, new HashMap<Integer, Double>());
		HashMap<Integer, Double> cached = indicators.get(indicator);
		if (!indicators_minYear.containsKey(indicator) || !indicators_maxYear.containsKey(indicator) || indicators_minYear.get(indicator) > startYear || indicators_maxYear.get(indicator) < endYear)
		{
			cached.putAll(dataFetcher.getIndicatorData(code.get(), indicator, startYear, endYear));
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
	
	public static String getAreaCodeFromName(String name)
	{
		if (!namesToCodes.containsKey(name))
			namesToCodes.put(name, dataFetcher.getAreaCodeFromName(name));
		return namesToCodes.get(name);
	}
	
	public static ArrayList<String> estimateNamesFromFragment(String fragment)
	{
		if (!fragmentsToNamesEstimates.containsKey(fragment))
			fragmentsToNamesEstimates.put(fragment, dataFetcher.estimateNamesFromFragment(fragment));
		return fragmentsToNamesEstimates.get(fragment);
	}
	
	public static ArrayList<String> getAllNames()
	{
		if (allNames.size() == 0)
			allNames = dataFetcher.getAllNames();
		return new ArrayList<String>(allNames);
	}
	
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
	
	protected static boolean codeIsValid(Code code)
	{
		return areas.containsKey(code) || dataFetcher.areaCodeExists(code.get());
	}
	
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