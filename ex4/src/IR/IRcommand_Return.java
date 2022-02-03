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
        Set<Integer> result = new HashSet<Integer>();
        result.add(t.getSerialNumber());
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

        MIPSGenerator.getInstance().return1(t, returnOwner);
    }
}
