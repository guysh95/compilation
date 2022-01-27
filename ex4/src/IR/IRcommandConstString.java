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

public class IRcommandConstString extends IRcommand
{
    TEMP t;
    String value;

    public IRcommandConstString(TEMP t,String value)
    {
        this.t = t;
        this.value = value;
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
        MIPSGenerator.getInstance().liString(t,value);
    }
}
