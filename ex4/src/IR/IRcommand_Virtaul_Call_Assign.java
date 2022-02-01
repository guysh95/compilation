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

public class IRcommand_Virtual_Call_Assign extends IRcommand
{
    TEMP dest;
    TEMP var;
    String methodName;
    TEMP_LIST args = null;

    public IRcommand_Virtual_Call_Assign(TEMP dest, TEMP var, String methodName, TEMP_LIST args)
    {
        this.dest = dest;
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
        Set<Integer> result = new HashSet<Integer>();
        result.add(dest.getSerialNumber());
        return result;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        // todo: create mips command for virtual calls
        // MIPSGenerator.getInstance().print_int(t);
    }
}