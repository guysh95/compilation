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

public class IRcommand_Binop_Add_Strings extends IRcommand
{
    public TEMP t1;
    public TEMP t2;
    public TEMP dst;

    public IRcommand_Binop_Add_Strings(TEMP dst,TEMP t1,TEMP t2)
    {
        this.dst = dst;
        this.t1 = t1;
        this.t2 = t2;
    }

    public Set<Integer> getLiveTemps(){
        Set<Integer> result = new HashSet<Integer>();
        result.add(t1.getSerialNumber());
        result.add(t2.getSerialNumber());
        result.add(dst.getSerialNumber());
        return result;
    }

    public Set<Integer> getDeadTemps(){
        //Set<Integer> result = new HashSet<Integer>();
        return null;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        System.out.println(String.format("Debug ---> file is: %s, dst: %d, s1: %d, s2: %d",
                "IRcommand_Binop_Add_Strings.java", dst.getSerialNumber(), t1.getSerialNumber(), t2.getSerialNumber()));
        MIPSGenerator.getInstance().addStrings(dst,t1,t2);
    }
}
