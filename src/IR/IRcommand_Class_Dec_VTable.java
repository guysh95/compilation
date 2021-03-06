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
        System.out.println(String.format("Debug ---> file is: %s", "IRcommand_Class_Dec_VTable.java"));
        // initialize label for VTable
        String className = currentClass.name;
        MIPSGenerator.getInstance().initVTable(className);

        // TODO: this implementation doesn't support overriding methods
        // insert all classes into a stack

        // Previous implementation

        Stack<TYPE_CLASS> allClasses = new Stack<TYPE_CLASS>();
        TYPE_CLASS p = currentClass;
        while (p != null) {
            allClasses.push(p);
            p = p.father;
        }

        // remove all classes from stack, one by one, and add MIPS method labels to VTable
        Stack<String> methodNames = new Stack<String>();
        List<String[]> methodTuples = new ArrayList<String[]>(); // [ , ]
        String curr_name;
        while (!allClasses.empty()) {
            p = allClasses.pop();
            for(TYPE_LIST ptr = p.data_members; ptr != null; ptr = ptr.tail){
                if (ptr.head.isFunction()) {
                    methodNames.push(ptr.head.name);
                }
            }
            while(!methodNames.empty()) {
                curr_name = methodNames.pop();
                int index = getIndex(methodTuples, curr_name);
                String[] arr = {curr_name, p.name};
                if (index == -1) {

                    methodTuples.add(arr);
                }
                else {
                    methodTuples.set(index, arr);
                }

            }
        }

        for (int i = 0; i < methodTuples.size(); i++) {
            String[] element = methodTuples.get(i);
            String methodLabel = element[1] + "_" + element[0];
            MIPSGenerator.getInstance().enterMethodLabel(methodLabel);
        }



        // new implementation:
        //Stack<String> methodNamesPerClass = new Stack<String>();
        /*
        Stack<String> methodNames = new Stack<String>();
        HashMap<String, String> insertedMethods = new HashMap<String, String>();
        TYPE_CLASS p = currentClass;
        String curr_name;
        while (p != null) {
            for(TYPE_LIST ptr = p.data_members; ptr != null; ptr = ptr.tail){
                if (ptr.head.isFunction()) {
                    if (insertedMethods.get(ptr.head.name) == null)
                    {
                        // methodNamesPerClass.push(p.name+ "_" +ptr.head.name);
                        methodNames.push(p.name+ "_" +ptr.head.name);
                        insertedMethods.put(ptr.head.name, p.name);
                    }
                }
            }
            p = p.father;
        }

        while(!methodNames.empty()) {
            curr_name = methodNames.pop();
            MIPSGenerator.getInstance().enterMethodLabel(curr_name);
        }
        */
        MIPSGenerator.getInstance().endDataSection();

    }

    public int getIndex(List<String[]> methods, String curr_name) {
        for (int i = 0; i < methods.size(); i++) {
            String[] element = methods.get(i);
            if (element[0].equals(curr_name)) {
                return i;
            }
        }
        return -1;
    }
}
