package adam.model;

import java.util.HashMap;

public abstract class Area
{
	protected static final String EX_CODE_INVALID = "Area code provided was not valid";
	
	protected static WorldBankDataFetcher dataFetcher = new WorldBankDataFetcher();
	protected static HashMap<Code, Area> areas = new HashMap<Code, Area>();
	
	protected Code code;
	
	private String name;
	
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
			code = c;
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
