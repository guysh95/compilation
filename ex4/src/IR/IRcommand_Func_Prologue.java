/***********/
/* PACKAGE */
/***********/
package IR; import MIPS.*; import java.util.*;
import TYPES.*;
import AST.*;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;

public class IRcommand_Func_Prologue extends IRcommand
{
    int localCount;

    public IRcommand_Func_Prologue(int localCount) {
        this.localCount = localCount;
    }

    public Set<Integer> getLiveTemps(){ return  null; }

    public Set<Integer> getDeadTemps(){ return null; }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        System.out.println(String.format("Debug ---> file is: %s", "IRcommand_Func_Prologue.java"));
        MIPSGenerator.getInstance().functionPrologue(localCount);
    }
}