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
	AnnotAst info;
	
	public IRcommand_Load(TEMP dst,String var_name, AnnotAst info)
	{
		this.dst      = dst;
		this.var_name = var_name;
		this.info = info;
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
		if(info.type == 0){	// todo: implement global load

		} else if (info.type == 1){ // todo: implement local load

		}
		MIPSGenerator.getInstance().load(dst,var_name);
	}
}
