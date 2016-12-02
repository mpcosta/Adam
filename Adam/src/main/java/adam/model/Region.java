package adam.model;

public class Region extends Area
{
	private Region(String c) throws Exception
	{
		super(c);
	}
	private Region(Code c)
	{
		super(c);
	}
	
	protected static boolean codeIsValid(Code code)
	{
		return (areas.containsKey(code) && areas.get(code) instanceof Region)
				|| dataFetcher.regionCodeExists(code.get());
	}
	
	/**
	 * Gets the region with the specified code.
	 * @param c	The code that matches to a region.
	 * @return	The Region that has the code given.
	 * @throws Exception	If the code given does not match a region.
	 */
	public static Region getRegion(String c) throws Exception
	{
		Code code = new Code(c);
		if (!codeIsValid(code))
			throw new Exception(Area.EX_CODE_INVALID + ": " + c + ", must be a valid Region code.");
		if (!areas.containsKey(code))
			areas.put(code, new Region(code));
		return (Region)areas.get(code);
	}
}
