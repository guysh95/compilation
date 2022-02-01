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

    public IRcommand_Return(TEMP t)
    {
        this.t = t;
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
        // todo: create relevant mips command
        // need to get return address here somehow maybe $ra?
        if (t == null) {

        }
        else {
            // t holds return value
        }
        //MIPSGenerator.getInstance().store(var_name,src);
    }
}
