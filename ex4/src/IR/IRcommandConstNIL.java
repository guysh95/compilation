/***********/
/* PACKAGE */
/***********/
package IR; import MIPS.*; import java.util.*;

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

    public Set<Integer> getLiveTemps(){
        return null;
    }

    public Set<Integer> getDeadTemps(){
        Set<Integer> result = new HashSet<Integer>();
        result.add(t.getSerialNumber());
        return result;
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
