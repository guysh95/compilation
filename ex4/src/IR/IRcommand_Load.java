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
		if (info.isGlobal()) {
			String varLabel = "global_" + var_name;
			MIPSGenerator.getInstance().loadGlobal(dst, varLabel);
			if (info.isLocal()) {
				MIPSGenerator.getInstance().loadLocal(dst, info.getOffset());
			}
			if (info.isParam()) {
				MIPSGenerator.getInstance().loadParam(dst, info.getOffset());
			}
			if (info.isField()) {
				// assuming instance is first argument of method so
				// we access first argument and then load from the right offset
				if (info.isField()) {
					MIPSGenerator.getInstance().loadFieldMethod(dst, info.getOffset());
				}
			}
			// MIPSGenerator.getInstance().load(dst,var_name);
		}
	}
}
