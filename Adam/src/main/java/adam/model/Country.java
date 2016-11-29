package adam.model;

public class Country
{
	private DataFetcher dataFetcher;
	private String code, name;
	
	
	public Country(DataFetcher fetcher, String c)
	{
		dataFetcher = fetcher;
		code = c;
		name = null;
	}
	
	public String getCode()
	{
		return code;
	}
	
	public String getName()
	{
		return name;
	}
	
	public double getGDP(int year)
	{
		return 0;
	}
}