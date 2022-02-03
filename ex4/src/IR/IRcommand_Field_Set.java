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

public class IRcommand_Field_Set extends IRcommand
{
    TEMP var;
    int fieldOffset;
    TEMP exp;

    public IRcommand_Field_Set(TEMP var, int fieldOffset, TEMP exp)
    {
        this.var       = var;
        this.fieldOffset = fieldOffset;
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
        // IR representation: "var.fieldName = exp"
        // MIPS representation: "sw $exp,offset($var)"
        MIPSGenerator.getInstance().swByOffset(exp, var, fieldOffset);

    }
}
