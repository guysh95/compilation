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

public class IRcommand_Jump_If_Not_Eq_To_Zero extends IRcommand
{
    TEMP t;
    String label_name;

    public IRcommand_Jump_If_Not_Eq_To_Zero(TEMP t, String label_name)
    {
        this.t          = t;
        this.label_name = label_name;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        // todo: add mips command
        
    }
}
