package TYPES;

public class TYPE_FOR_SCOPE_BOUNDARIES extends TYPE
{
	public boolean funcBound;
	public boolean classBound;
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FOR_SCOPE_BOUNDARIES(String name, boolean forClass)
	{
		this.name = name;
		if(name.equals("NONE")) {
			this.funcBound = false;
			this classBound = false;
		}
		else if (forClass) {
			this.funcBound = false;
			this classBound = true;
		}
		else {
			this.funcBound = true;
			this classBound = false;
		}
	}
}
