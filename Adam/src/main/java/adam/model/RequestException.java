package adam.model;

public class RequestException extends Exception
{
	public final static int NO_CONNECTION = 0, INVALID_RANGE = 1;
	
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
