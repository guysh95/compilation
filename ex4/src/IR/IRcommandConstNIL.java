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

public class IRcommandConstNIL extends IRcommand
{
    TEMP t;
    String value;

    public IRcommandConstNIL(TEMP t)
    {
        this.t = t;
        this.value = "NIL";
    }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        // todo: create liNIL that will look different then string
        //MIPSGenerator.getInstance().liString(t,value);
    }
}
