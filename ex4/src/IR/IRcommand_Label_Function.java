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
import REG_ALLOC.*;

public class IRcommand_Label_Function extends IRcommand
{
    public String label_name;
    // public CFG func_cfg;

    public IRcommand_Label_Function(String label_name)
    {
        this.label_name = label_name;
        // this.func_cfg = new CFG(label_name);
    }

    public Set<Integer> getLiveTemps(){
        return null;
    }

    public Set<Integer> getDeadTemps(){
        return null;
    }


    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        System.out.println(String.format("Debug ---> file is: %s", "IRcommand_Label_Function.java"));
        MIPSGenerator.getInstance().label(label_name);
    }
}
