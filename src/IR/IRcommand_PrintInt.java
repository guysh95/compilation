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

public class IRcommand_PrintInt extends IRcommand
{
	TEMP t;
	
	public IRcommand_PrintInt(TEMP t)
	{
		this.t = t;
	}

	public Set<Integer> getLiveTemps(){
		Set<Integer> result = new HashSet<Integer>();
		result.add(t.getSerialNumber());
		return result;
	}

	public Set<Integer> getDeadTemps(){
		return null;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		System.out.println(String.format("Debug ---> file is: %s", "IRcommand_PrintInt.java"));
		MIPSGenerator.getInstance().print_int(t);
	}
}
