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
import MIPS.*;

public class IRcommand_Class_Dec_VTable extends IRcommand
{
    TYPE_CLASS currentClass;
    TYPE_CLASS extended_class;

    public IRcommand_Class_Dec_VTable(TYPE_CLASS currentClass, TYPE_CLASS extended_class)
    {
        this.currentClass = currentClass;
        this.extended_class = extended_class;
    }

    public Set<Integer> getLiveTemps(){ return  null; }

    public Set<Integer> getDeadTemps(){ return null; }

    /***************/
    /* MIPS me !!! */
    /***************/
    public void MIPSme()
    {
        // initialize label for VTable
        String className = currentClass.name;
        MIPSGenerator.getInstance().initVTable(className)

        // TODO: this implementation doesn't support overriding methods
        // insert all classes into a stack
        Stack<TYPE_CLASS> allClasses = new Stack<TYPE_CLASS>();
        TYPE_CLASS p = currentClass;
        while (p != null) {
            allClasses.push(p);
            p = p.father;
        }

        // remove all classes from stack, one by one, and add MIPS method labels to VTable
        while (!allClasses.empty()) {
            p = allClasses.pop();
            for(TYPE_LIST ptr = p.data_members; ptr != null; ptr = ptr.tail){
                if (ptr.head.isFunction()) {
                    MIPSGenerator.getInstance().enterMethodLabel(p.name, ptr.head.name);
                }
            }
        }



        // TODO: add node of new class VTable to vtableMap


    }
}
