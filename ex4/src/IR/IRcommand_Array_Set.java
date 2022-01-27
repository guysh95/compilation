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

    public Set<Integer> getLiveTemps(){
        Set<Integer> result = new HashSet<Integer>();
        result.add(place.getSerialNumber());
        result.add(exp.getSerialNumber());
        result.add(array.getSerialNumber());
        return result;
    }

    public Set<Integer> getDeadTemps(){
        return null;
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
