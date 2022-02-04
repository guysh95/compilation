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

public class IRcommandConstInt extends IRcommand
{
	TEMP t;
	int value;
	
	public IRcommandConstInt(TEMP t,int value)
	{
		this.t = t;
		this.value = value;
	}

	public Set<Integer> getLiveTemps(){
		return null;
	}

	public Set<Integer> getDeadTemps(){
		Set<Integer> result = new HashSet<Integer>();
		result.add(t.getSerialNumber());
		return result;
	}

	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		System.out.println(String.format("Debug ---> file is: %s", "IRcommandConstInt.java"));
		MIPSGenerator.getInstance().liInt(t,value);
	}
}
