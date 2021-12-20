/***********/
/* PACKAGE */
/***********/
package IR;

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
    TEMP dest;
    TEMP var;
    String methodName;
    TEMP_LIST args = null;

    public IRcommand_Virtual_Call(TEMP dest, TEMP var, String methodName, TEMP_LIST args)
    {
        this.dest = dest;
        this.var = var;
        this.methodName = methodName;
        this.args = args;
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
