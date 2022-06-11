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

public class IRcommand_Func_Epilogue extends IRcommand
{
    public IRcommand_Func_Epilogue() {}

    public Set<Integer> getLiveTemps(){ return  null; }

    public Set<Integer> getDeadTemps(){ return null; }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        System.out.println(String.format("Debug ---> file is: %s", "IRcommand_Func_Epilogue.java"));
        MIPSGenerator.getInstance().functionEpilogue();
    }
}