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

public class IRcommand_Field_Access extends IRcommand
{
    TEMP dst;
    TEMP var;
    String fieldName;

    public IRcommand_Field_Access(TEMP dst, TEMP var, String fieldName)
    {
        this.dst      = dst;
        this.var = var;
        this.fieldName = fieldName;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        //MIPSGenerator.getInstance().load(dst,var_name);
        // todo: need to create mips command
    }
}
