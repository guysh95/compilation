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

public class IRcommand_Array_Set extends IRcommand
{
    TEMP array;
    TEMP place;
    TEMP exp;

    public IRcommand_Array_Set(TEMP array, TEMP place, TEMP exp)
    {
        this.array     = array;
        this.place     = place;
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
