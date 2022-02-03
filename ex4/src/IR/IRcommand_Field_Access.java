/***********/
/* PACKAGE */
/***********/
package IR; import java.util.*;

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
    int fieldOffset;

    public IRcommand_Field_Access(TEMP dst, TEMP var, int fieldOffset)
    {
        this.dst      = dst;
        this.var = var;
        this.fieldOffset = fieldOffset;
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
        // IR representation: "dst = var.fieldName"
        // MIPS representation: "lw $dst,offset($var)"
        MIPSGenerator.getInstance().lwByOffset(dst, var, fieldOffset);
    }
}
