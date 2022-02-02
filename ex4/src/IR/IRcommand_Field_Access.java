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

public class IRcommand_Field_Access extends IRcommand
{
    TEMP dst;
    TEMP var;
    String fieldName;

    public IRcommand_Field_Access(TEMP dst, TEMP var, String fieldName)
    {
        this.dst      = dst;
        this.var = var;
        this.fieldName = fieldName;
    }

    public Set<Integer> getLiveTemps(){
        Set<Integer> result = new HashSet<Integer>();
        result.add(var.getSerialNumber());
        return result;
    }

    public Set<Integer> getDeadTemps(){
        Set<Integer> result = new HashSet<Integer>();
        result.add(dst.getSerialNumber());
        return result;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        // todo: get the correct offset
        int fieldOffset = 4;

        // IR representation: "dst = var.fieldName"
        // MIPS representation: "lw $dst,offset($var)"
        MIPSme();.getInstance().lwByOffset(dst, var, fieldOffset);
    }
}
