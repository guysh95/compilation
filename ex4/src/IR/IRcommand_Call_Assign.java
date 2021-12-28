/***********/
/* PACKAGE */
/***********/
package IR; import MIPS.*;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;
import MIPS.*;

public class IRcommand_Call_Assign extends IRcommand
{
    TEMP dest;
    String funcName;
    TEMP_LIST args = null;

    public IRcommand_Call_Assign(TEMP dest, String funcName, TEMP_LIST args)
    {
        this.dest = dest;
        this.funcName = funcName;
        this.args = args;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        // todo: create mips command for function calls
        // MIPSGenerator.getInstance().print_int(t);
        MIPSGenerator.getInstance().callFunc(funcName, args, dest);

    }
}
