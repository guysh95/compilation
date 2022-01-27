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
        //MIPSGenerator.getInstance().load(dst,var_name);
        // todo: need to create mips command
    }
}
