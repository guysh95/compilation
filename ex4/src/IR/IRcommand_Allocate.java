/***********/
/* PACKAGE */
/***********/
package IR; import MIPS.*; import java.util.*;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;
import MIPS.*;

public class IRcommand_Allocate extends IRcommand
{
	String var_name;
	
	public IRcommand_Allocate(String var_name)
	{
		this.var_name = var_name;
	}

	public Set<Integer> getLiveTemps(){
		return null;
	}
	public Set<Integer> getDeadTemps(){
		return null;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		System.out.println(String.format("Debug ---> file is: %s", "IRcommand_Allocate.java"));
		// MIPSGenerator.getInstance().allocate(var_name);
	}
}
