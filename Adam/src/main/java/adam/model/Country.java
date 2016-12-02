package adam.model;

import java.util.HashMap;

public class Country extends Area
{
	private Code code;
	private String region, incomeLevel, lendingType, capitalCity;
	private double longitude, latitude;
	private boolean longitudeCached, latitudeCached;
	
	private Country(String c) throws Exception
	{
		super(c);
		initialise();
	}
	private Country(Code c)
	{
		super(c);
		initialise();
	}
	
	private void initialise()
	{
		region = null;
		incomeLevel = null;
		lendingType = null;
		capitalCity = null;
		longitudeCached = false;
		latitudeCached = false;
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
	
	protected static boolean codeIsValid(Code code)
	{
		return (areas.containsKey(code) && areas.get(code) instanceof Country) || dataFetcher.countryCodeExists(code.get());
	}
	
	public static Country getCountry(String c) throws Exception
	{
		Code code = new Code(c);
		if (!codeIsValid(code))
			throw new Exception(Area.EX_CODE_INVALID + ": " + c + ", must be a valid Country code.");
		if (!areas.containsKey(code))
			areas.put(code, new Country(code));
		return (Country)areas.get(code);
	}
	
	
	
}