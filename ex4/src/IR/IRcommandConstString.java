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

public class IRcommandConstString extends IRcommand
{
    TEMP t;
    String value;

    public IRcommandConstString(TEMP t,String value)
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
