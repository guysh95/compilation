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

public class IRcommand_New_Array extends IRcommand
{
    TEMP dest;
    TEMP t1;

    public IRcommand_New_Array(TEMP dest, TEMP t1)
    {
        this.dest     = dest;
        this.t1       = t1;
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
