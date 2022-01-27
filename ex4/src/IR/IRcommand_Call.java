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

public class IRcommand_Call extends IRcommand
{
    String funcName;
    TEMP_LIST args = null;

    public IRcommand_Call(String funcName, TEMP_LIST args)
    {
        this.funcName = funcName;
        this.args = args;
    }

    public Set<Integer> getLiveTemps(){
        Set<Integer> result = new HashSet<Integer>();
        for(TEMP_LIST tlist=args; tlist.tail!=null; tlist=tlist.tail){
            TEMP arg = tlist.head;
            result.add(arg.getSerialNumber());
        }
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
        // todo: create mips command for function calls
        // MIPSGenerator.getInstance().print_int(t);
        MIPSGenerator.getInstance().callFunc(funcName, args, null);
    }
}
