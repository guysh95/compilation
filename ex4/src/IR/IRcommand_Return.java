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

public class IRcommand_Return extends IRcommand
{
    TEMP t;
    String returnOwner;
    public IRcommand_Return(TEMP t, String returnOwner)
    {
        this.t = t;
        this.returnOwner = returnOwner;
    }

    public Set<Integer> getLiveTemps(){
        if(t != null){
            Set<Integer> result = new HashSet<Integer>();
            result.add(t.getSerialNumber());
            return result;
        }
        else {
            return null;
        }
    }

    public Set<Integer> getDeadTemps(){
        return null;
    }
    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        System.out.println(String.format("Debug ---> file is: %s 1", "IRcommand_Return.java"));
        System.out.println("We want to return to " + returnOwner);
        MIPSGenerator.getInstance().return1(t, returnOwner);
        System.out.println(String.format("Debug ---> file is: %s 2", "IRcommand_Return.java"));
    }
}
