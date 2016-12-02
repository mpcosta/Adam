package adam.model;

import java.util.HashMap;

public class Country
{
	private static final String EX_CODE_INVALID = "Country code provided was not valid ";
	
	private static HashMap<Code, Country> countries = new HashMap<Code, Country>();
	
	private static WorldBankDataFetcher dataFetcher = new WorldBankDataFetcher();
	private Code code;
	private String name, region, incomeLevel, lendingType, capitalCity;
	private double longitude, latitude;
	private boolean longitudeCached, latitudeCached;
	
	private Country(String c) throws Exception
	{
		code = new Code(c);
		initialise();
	}
	private Country(Code c)
	{
		code = c;
		initialise();
	}
	
	private void initialise()
	{
		name = null;
		region = null;
		incomeLevel = null;
		lendingType = null;
		capitalCity = null;
		longitudeCached = false;
		latitudeCached = false;
	}
	
	public String getCode()
	{
		return code.get();
	}
	
	public String getName()
	{
		if (name == null)
			name = dataFetcher.getNameFromCode(code.get());
		return name;
	}
	
	public String getRegion()
	{
		if (region == null)
			region = dataFetcher.getRegionFromCode(code.get());
		return region;
	}
	
	public String getIncomeLevel()
	{
		if (incomeLevel == null)
			incomeLevel = dataFetcher.getIncomeLevelFromCode(code.get());
		return incomeLevel;
	}
	
	public String getLendingType()
	{
		if (lendingType == null)
			lendingType = dataFetcher.getLendingType(code.get());
		return lendingType;
	}
	
	public String getCapitalCity()
	{
		if (capitalCity == null)
			capitalCity = dataFetcher.getCapitalCity(code.get());
		return capitalCity;
	}
	
	public double getLongitude()
	{
		if (!longitudeCached)
		{
			longitude = dataFetcher.getLongitude(code.get());
			longitudeCached = true;
		}
		return longitude;
	}
	
	public double getLatitude()
	{
		if (!latitudeCached)
		{
			latitude = dataFetcher.getLatitude(code.get());
			latitudeCached = true;
		}
		return latitude;
	}
	
	/**
	 * Tests whether a given Country code is valid to represent a Country. 
	 * @param code	The Country code.
	 * @return	True if it can be used to request a Country object.
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
	
	private static boolean codeIsValid(Code code)
	{
		return countries.containsKey(code) || dataFetcher.countryCodeExists(code.get());
	}
	
	public static Country getCountry(String c) throws Exception
	{
		Code code = new Code(c);
		if (!codeIsValid(code))
			throw new Exception(EX_CODE_INVALID + ": " + c);
		if (!countries.containsKey(code))
			countries.put(code, new Country(code));
		return countries.get(code);
	}
	
	private static class Code
	{
		private static final int CODE_LENGTH = 2;
		
		private String code;
		
		public Code(String c) throws Exception
		{
			if (c.length() != CODE_LENGTH)
				throw new Exception(EX_CODE_INVALID + ": " + c + "; Code length must be " + CODE_LENGTH);
			code = c;
		}
		
		public String get()
		{
			return code;
		}
	}
	
}