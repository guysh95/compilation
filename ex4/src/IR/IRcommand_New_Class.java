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

public class IRcommand_New_Class extends IRcommand
{
    TEMP dest;
    String typeName;

    public IRcommand_New_Class(TEMP dest, String typeName)
    {
        this.dest     = dest;
        this.typeName = typeName;
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
