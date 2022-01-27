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

public class IRcommand_New_Class extends IRcommand
{
    TEMP dest;
    String typeName;

    public IRcommand_New_Class(TEMP dest, String typeName)
    {
        this.dest     = dest;
        this.typeName = typeName;
    }

    public Set<Integer> getLiveTemps(){
        return null;
    }

    public Set<Integer> getDeadTemps(){
        Set<Integer> result = new HashSet<Integer>();
        result.add(dest.getSerialNumber());
        return result;
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
