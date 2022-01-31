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

public class IRcommand_Array_Access extends IRcommand
{
    TEMP dst;
    TEMP var;
    TEMP exp;

    public IRcommand_Array_Access(TEMP dst, TEMP t1, TEMP t2)
    {
        this.dst      = dst;
        this.var    = t1;
        this.exp    = t2;
    }


    public Set<Integer> getLiveTemps(){
        Set<Integer> result = new HashSet<Integer>();
        result.add(var.getSerialNumber());
        result.add(exp.getSerialNumber());
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
        // todo: need to create relevant mips command
        MIPSGenerator.getInstance().getArrayValue(t1,t2,dst);



    }


}
