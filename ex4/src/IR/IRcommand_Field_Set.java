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
    String fieldName;
    TEMP exp;

    public IRcommand_Field_Set(TEMP var, String fieldName, TEMP exp)
    {
        this.var       = var;
        this.fieldName = fieldName;
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
        // todo: get the correct offset
        int fieldOffset = 4;

        // IR representation: "var.fieldName = exp"
        // MIPS representation: "sw $exp,offset($var)"
        MIPSme();.getInstance().swByOffset(exp, var, fieldOffset);

    }
}
