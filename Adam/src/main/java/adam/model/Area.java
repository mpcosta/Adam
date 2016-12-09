package adam.model;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Area
{
	protected static final String EX_CODE_INVALID = "Area code provided was not valid";
	
	protected static WorldBankDataFetcher dataFetcher = new WorldBankDataFetcher();
	protected static HashMap<Code, Area> areas = new HashMap<Code, Area>();
	
	private static HashMap<String, String> namesToCodes = new HashMap<String, String>();
	private static HashMap<String, ArrayList<String>> fragmentsToNamesEstimates = new HashMap<String, ArrayList<String>>();
	private static ArrayList<String> allNames = new ArrayList<String>();
	
	protected Code code;
	
	private String name;
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
	
	public double getGDP(int year) throws Exception
	{
		if (!gdp.containsKey(year))
			gdp.put(year, dataFetcher.getGDP(code.get(), year));
		return gdp.get(year);
	}
	
	public double getCPI(int year) throws Exception
	{
		if (!cpi.containsKey(year))
			cpi.put(year, dataFetcher.getCPI(code.get(), year));
		return cpi.get(year);
	}
	
	public double getBOP(int year) throws Exception
	{
		if (!bop.containsKey(year))
			bop.put(year, dataFetcher.getBOP(code.get(), year));
		return bop.get(year);
	}
	
	public double getUnemployment(int year) throws Exception
	{
		if (!unemployment.containsKey(year))
			unemployment.put(year, dataFetcher.getUnemployment(code.get(), year));
		return unemployment.get(year);
	}
	
	public double getInflation(int year) throws Exception
	{
		if (!inflation.containsKey(year))
			inflation.put(year, dataFetcher.getInflation(code.get(), year));
		return inflation.get(year);
	}
	
	public double getGovernmentSpending(int year) throws Exception
	{
		if (!governmentSpending.containsKey(year))
			governmentSpending.put(year, dataFetcher.getGovernmentSpending(code.get(), year));
		return governmentSpending.get(year);
	}
	
	public double getGovernmentConsumption(int year) throws Exception
	{
		if (!governmentConsumption.containsKey(year))
			governmentConsumption.put(year, dataFetcher.getGovernmentConsumption(code.get(), year));
		return governmentConsumption.get(year);
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
	}
}
