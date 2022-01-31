package AST;
import TYPES.*;
import TEMP.*; import IR.*; import MIPS.*;


public abstract class AST_Node
{

	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int SerialNumber;
	
	/***********************************************/
	/* The default message for an unknown AST node */
	/***********************************************/
	public void PrintMe()
	{
		System.out.print("AST NODE UNKNOWN\n");
	}
	public TYPE SemantClassMe(TYPE_CLASS scope){return null;}
	public TYPE SemantMe(TYPE scope)
	{
		System.out.print("AST TYPE (semantme) UNKNOWN\n");
		return null;
	}
	public TEMP IRme()
	{
		return null; //not supposed to reach here
	}

	public void AnnotateMe() {return null;}
}
