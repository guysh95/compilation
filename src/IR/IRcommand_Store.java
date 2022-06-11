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
import ANNOTATE_TABLE.*;

public class IRcommand_Store extends IRcommand {
	String var_name;
	TEMP src;
	AnnotAst info;

	public IRcommand_Store(String var_name, TEMP src, AnnotAst info) {
		this.src = src;
		this.var_name = var_name;
		this.info = info;
	}

	public Set<Integer> getLiveTemps() {
		Set<Integer> result = new HashSet<Integer>();
		result.add(src.getSerialNumber());
		return result;
	}

	public Set<Integer> getDeadTemps() {
		return null;
	}

	/***************/
	/* MIPS me !!! */

	/***************/
	public void MIPSme() {

		System.out.println(String.format("Debug ---> file is: %s", "IRcommand_Store.java"));
		System.out.println(info);
		if (info.isGlobal()) {
			String varLabel = "global_" + var_name;
			System.out.println(String.format("Debug =======> storing global at %s", varLabel));
			MIPSGenerator.getInstance().storeGlobal(src, varLabel);
		}
		if (info.isLocal()) {
			System.out.println(String.format("Debug =======> storing local"));
			MIPSGenerator.getInstance().storeLocal(src, info.getOffset());
		}
		if (info.isParam()) {
			System.out.println(String.format("Debug =======> storing param"));
			MIPSGenerator.getInstance().storeParam(src, info.getOffset());
		}
		if (info.isField()) {
			// assuming instance is first argument of method so
			// we access first argument and then store in the right offset
			System.out.println(String.format("Debug =======> storing in field"));
			MIPSGenerator.getInstance().storeFieldMethod(src, info.getOffset());
		}
		// MIPSGenerator.getInstance().store(var_name,src);
	}
}
