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

public class IRcommand_Load extends IRcommand {
	TEMP dst;
	String var_name;
	AnnotAst info;

	public IRcommand_Load(TEMP dst, String var_name, AnnotAst info) {
		this.dst = dst;
		this.var_name = var_name;
		this.info = info;
	}

	public Set<Integer> getLiveTemps() {
		return null;
	}

	public Set<Integer> getDeadTemps() {
		Set<Integer> result = new HashSet<Integer>();
		result.add(dst.getSerialNumber());
		return result;
	}
	/***************/
	/* MIPS me !!! */

	/***************/
	public void MIPSme() {
		System.out.println(String.format("Debug ---> file is: %s", "IRcommand_Load.java"));
		if (info.isGlobal()) {
			System.out.println("Debug =======> loading global variable");
			String varLabel = "global_" + var_name;
			MIPSGenerator.getInstance().loadGlobal(dst, varLabel);
		}
		if (info.isLocal()) {
			System.out.println("Debug =======> loading local variable");
			MIPSGenerator.getInstance().loadLocal(dst, info.getOffset());
		}
		if (info.isParam()) {
			System.out.println("Debug =======> loading param");
			MIPSGenerator.getInstance().loadParam(dst, info.getOffset());
		}
		if (info.isField()) {
			System.out.println("Debug =======> loading field");
			// assuming instance is first argument of method so
			// we access first argument and then load from the right offset
			MIPSGenerator.getInstance().loadFieldMethod(dst, info.getOffset());
		}
		// MIPSGenerator.getInstance().load(dst,var_name);
	}
}
