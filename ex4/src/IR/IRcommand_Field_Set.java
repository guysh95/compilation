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

public class IRcommand_Field_Set extends IRcommand
{
    TEMP var;
    String methodName;
    TEMP exp;

    public IRcommand_Field_Set(TEMP var, String methodName, TEMP exp)
    {
        this.var     = var;
        this.methodName     = methodName;
        this.exp       = exp;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        // todo: create mips command for this
        //MIPSGenerator.getInstance().store(var_name,src);
    }
}
