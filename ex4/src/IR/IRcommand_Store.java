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

public class IRcommand_Store extends IRcommand
{
	String var_name;
	TEMP src;
	AnnotAst info;
	
	public IRcommand_Store(String var_name,TEMP src, AnnotAst info)
	{
		this.src      = src;
		this.var_name = var_name;
		this.info = info;
	}

	public Set<Integer> getLiveTemps(){
		Set<Integer> result = new HashSet<Integer>();
		result.add(src.getSerialNumber());
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
		if(info.type == 0){	// todo: implement global store

		} else if (info.type == 1){ // todo: implement local store

		}
		MIPSGenerator.getInstance().store(var_name,src);
	}
}
