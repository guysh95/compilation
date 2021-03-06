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
        System.out.println(String.format("Debug ---> file is: %s 1", "IRcommand_Array_Access"));
        MIPSGenerator.getInstance().getArrayValue(var,exp,dst);
        System.out.println(String.format("Debug ---> file is: %s 2", "IRcommand_Array_Access"));
    }


}
