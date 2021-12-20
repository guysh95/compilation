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

public class IRcommandConstString extends IRcommand
{
    TEMP t;
    String value;

    public IRcommandConstInt(TEMP t,String value)
    {
        this.t = t;
        this.value = value;
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        MIPSGenerator.getInstance().liString(t,value);
    }
}
