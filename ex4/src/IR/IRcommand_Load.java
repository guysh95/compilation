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

public class IRcommand_Load extends IRcommand
{
	TEMP dst;
	String var_name;
	
	public IRcommand_Load(TEMP dst,String var_name)
	{
		this.dst      = dst;
		this.var_name = var_name;
	}

	public Set<Integer> getLiveTemps(){
		return null;
	}

	public Set<Integer> getDeadTemps(){
		Set<Integer> result = new HashSet<Integer>();
		result.add(dst.getSerialNumber());
		return result;
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().load(dst,var_name);
	}
}
