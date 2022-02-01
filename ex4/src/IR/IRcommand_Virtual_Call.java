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

public class IRcommand_Virtual_Call extends IRcommand
{
    TEMP var; // t1
    String methodName; // m2
    TEMP_LIST args = null; // t2 .... tn

    public IRcommand_Virtual_Call(TEMP var, String methodName, TEMP_LIST args)
    {
        this.var = var;
        this.methodName = methodName;
        this.args = args;
    }

    public Set<Integer> getLiveTemps(){
        Set<Integer> result = new HashSet<Integer>();
        for(TEMP_LIST tlist=args; tlist.tail!=null; tlist=tlist.tail){
            TEMP arg = tlist.head;
            result.add(arg.getSerialNumber());
        }
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
        // todo: get method offset here somehow
        int methodOffset = 0;
        MIPSGenerator.getInstance().callMethod(var, methodOffset, args, null);
    }
}
