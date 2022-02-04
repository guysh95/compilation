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
import TYPES.*;

public class IRcommand_New_Class extends IRcommand
{
    TEMP dest;
    String typeName;
    TYPE_CLASS type;

    public IRcommand_New_Class(TEMP dest, TYPE_CLASS classType)
    {
        this.dest     = dest;
        this.typeName = classType.name;
        this.type = classType;
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
        System.out.println(String.format("Debug ---> file is: %s", "IRcommand_New_Class.java"));
        // need to allocate (counter + 1) * 4 bytes for vtable and all fields
        int space = (countFieldsInFathers() + 1) * 4;
        System.out.println(String.format("Debug ======> Creating new instance of: %s  ; size of is %d", typeName, space));
        MIPSGenerator.getInstance().mallocSpace(dest, space);

        String vtableLabel = "vt_" + typeName;
        // assuming we have an address for the class vtable as "vtableAddress"
        System.out.println(String.format("Debug ======> loading vt of %s at: %s", typeName, vtableLabel));
        MIPSGenerator.getInstance().loadAddressToDest(vtableLabel, dest);
    }

    public int countFieldsInFathers()
    {
        TYPE_CLASS p = this.type;
        int counter = 0;
        while (p != null) {
            counter += p.fieldsCount;
            p = p.father;
        }
        return counter;
    }
}
