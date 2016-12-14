package adam.model;

public class RequestException extends Exception
{
	private static final long serialVersionUID = -5673649076906702888L; // TODO: check serial

	public final static int NO_CONNECTION = 0, INVALID_RANGE = 1, MAP_INVALID_RANGE = 2, MAP_NO_COUNTRY_DATA_FOUND = 3;
	
	private int type;
	private String info;
	
	public RequestException(int t)
	{
		super();
		type = t;
		info = "";
	}
	public RequestException(int t, String i)
	{
		super();
		type = t;
		info = i;
	}
	
	public int getType()
	{
		return type;
	}
	
	public String getInfo()
	{
		return info;
	}
}
