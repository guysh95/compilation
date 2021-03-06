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
        result.add(var.getSerialNumber());
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
        // IR representation: "var.fieldName = exp"
        // MIPS representation: "sw $exp,offset($var)"
        System.out.println(String.format("Debug ---> file is: %s", "IRcommand_Class_Field_Set.java"));
        System.out.println(String.format("Debug ---------> Class_Field offset is: %d A", fieldOffset));
        MIPSGenerator.getInstance().pointerDereference(var);
        System.out.println(String.format("Debug ---> file is: %s B", "IRcommand_Class_Field_Set.java"));
        MIPSGenerator.getInstance().swByOffset(exp, var, fieldOffset);
    }
}
