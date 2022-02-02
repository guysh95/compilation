package TYPES;

public class TYPE_FOR_SCOPE_BOUNDARIES extends TYPE
{

	public boolean classBound = false;
	public boolean funcBound = false;
	public TYPE_CLASS father = null;
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FOR_SCOPE_BOUNDARIES(String name, boolean isClass, TYPE_CLASS father)
	{
		this.name = name;
		if (isClass) {
			this.classBound = true;
			this.father = tc;
		}
		else if (!name.equals("NONE"))
			this.funcBound = true;
	}
}
