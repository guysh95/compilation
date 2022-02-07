package TYPES;

public class TYPE_CLASS_VAR_DEC extends TYPE
{
	public TYPE t;
	public String name;
	public Integer assignVal = null;
	public String assignString = null;

	public TYPE_CLASS_VAR_DEC(TYPE t,String name, Integer assignVal, String assignString)
	{
		this.t = t;
		this.name = name;
		this.assignVal = assignVal;
		this.assignString = assignString;
	}

	public TYPE_CLASS_VAR_DEC(TYPE t,String name)
	{
		this.t = t;
		this.name = name;
	}

	public boolean isInitialized()
	{
		return (assignVal != null || assignString != null);
	}

	public boolean isVar(){return true;}
}
