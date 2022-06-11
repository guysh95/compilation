package IR; import MIPS.*; import java.util.*;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;
import ANNOTATE_TABLE.*;

public class IRcommand_New_Global_Int extends IRcommand {
    String var_name;
    int val;

    public IRcommand_New_Global_Int(String var_name, int val) {
        this.val = val;
        this.var_name = var_name;
    }

    public Set<Integer> getLiveTemps() {
        return null;
    }

    public Set<Integer> getDeadTemps() {
        return null;
    }

    /***************/
    /* MIPS me !!! */

    /***************/
    public void MIPSme() {
        String varLabel = var_name;
        System.out.println(String.format("Debug =======> storing global at %s", varLabel));
        MIPSGenerator.getInstance().allocateGlobalInt(varLabel, val);
    }
}