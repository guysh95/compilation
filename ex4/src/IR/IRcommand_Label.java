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

public class IRcommand_Label extends IRcommand
{
	String label_name;
	
	public IRcommand_Label(String label_name)
	{
		this.label_name = label_name;
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
		MIPSGenerator.getInstance().label(label_name);
	}
}
