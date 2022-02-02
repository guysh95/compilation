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

public class IRcommand_Field_Set extends IRcommand
{
    TEMP var;
    String methodName;
    TEMP exp;

    public IRcommand_Field_Set(TEMP var, String methodName, TEMP exp)
    {
        this.var     = var;
        this.methodName     = methodName;
        this.exp       = exp;
    }

    public Set<Integer> getLiveTemps(){
        Set<Integer> result = new HashSet<Integer>();
        result.add(exp.getSerialNumber());
        return result;
    }

    public Set<Integer> getDeadTemps(){
        Set<Integer> result = new HashSet<Integer>();
        result.add(var.getSerialNumber());
        return result;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        // todo: create mips command for this assuming we have correct offset
        int fieldOffset = 4;
        //MIPSGenerator.getInstance().store(var_name,src);
    }
}
