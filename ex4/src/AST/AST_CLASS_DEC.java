package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;
import ANNOTATE_TABLE.*;

public class AST_CLASS_DEC extends AST_DEC {

    public String className;
    public String extendedClass;
    public AST_CFIELD_LIST list;
    public int row;

    public TYPE_CLASS tclass = null;
    public TYPE_CLASS ext_tclass = null;

    public AST_CLASS_DEC(String className, String extendedClass, AST_CFIELD_LIST list, int row) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (extendedClass != null)
            System.out.print("====================== classDec -> CLASS ID EXTENDS ID { cFieldList }\n");
        else
            System.out.print("====================== classDec -> CLASS ID { cFieldList }\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.className = className;
        this.extendedClass = extendedClass;
        this.list = list;
        this.row = row;
    }

    /***********************************************/
    /* The default message for a classDec Node */

    /***********************************************/
    public void PrintMe() {
        /************************************/
        /* AST NODE TYPE = EXP VAR AST NODE */
        /************************************/
        System.out.print("AST NODE CLASS DEC\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (className != null) System.out.format("class %s", className);
        if (extendedClass != null) System.out.format("extends %s", extendedClass);
        if (list != null) list.PrintMe();


        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("CLASS %s\nDEC\n", className));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (list != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, list.SerialNumber);

    }

    public TYPE SemantMe(TYPE scope)
    {
        System.out.println("###$#$#$#$#$#$ now we semant " + className + " class #$#$#$#$#$#$#$#$#$#$");
        TYPE extended_type = null;
        TYPE_CLASS extended_type_casted = null;

        if (SYMBOL_TABLE.getInstance().isGlobalScope() == false){
            System.out.print("Class not defined in global scope");
            throw new lineException(Integer.toString(this.row));
        }

        if (extendedClass != null){
            extended_type = SYMBOL_TABLE.getInstance().find(extendedClass);
            if (extended_type == null){
                System.out.format("%s Extended class is not defined\n", extendedClass);
                throw new lineException(Integer.toString(this.row));
            }
            if(extended_type.isClass() == false){
                System.out.format("%s Extended class is not of type class\n", extendedClass);
                throw new lineException(Integer.toString(this.row));
            } else {
                extended_type_casted = (TYPE_CLASS) extended_type;
                this.ext_tclass = extended_type_casted;
            }
        }

        if (SYMBOL_TABLE.getInstance().find(className) != null){
            System.out.format("%s Class already defined", className);
            throw new lineException(Integer.toString(this.row));
        }

        /*************************/
        /* [1] Begin Class Scope */
        /*************************/
        SYMBOL_TABLE.getInstance().beginScope(className, true, extended_type_casted);

        /***************************/
        /* [2] Semant Data Members */
        /***************************/
        System.out.println("now we semant the class fields");
        TYPE_CLASS t = new TYPE_CLASS(extended_type_casted,className, null);

        TYPE_LIST dataMembers = list.getTypesClass(t);

        TYPE_CLASS tclass = new TYPE_CLASS(extended_type_casted,className,dataMembers);

        //replace here recursive instances TYPE_ID with new TYPE_CLASS
        /**
         * here we can count class fields and class methods
        * WITHOUT
        * superclass's fields and methods
         */
        int fieldsCount = 0;
        int methodCount = 0;
        System.out.println(tclass.name + " data members are: ");
        for(TYPE_LIST ptr = tclass.data_members; ptr != null; ptr = ptr.tail){
            if (ptr.head.isVar()) {
                if (className.equals(((TYPE_CLASS_VAR_DEC)ptr.head).t.name))
                    ((TYPE_CLASS_VAR_DEC)ptr.head).t = tclass;
                fieldsCount++;
            }
            else if (ptr.head.isFunction()) {
                System.out.println("Hey my name is " + ((TYPE_FUNCTION)ptr.head).name + " My Type is " + ((TYPE_FUNCTION)ptr.head));
                methodCount++;
            }
        }

        /*****************/
        /* [3] End Scope */
        /*****************/
        SYMBOL_TABLE.getInstance().endScope(true);

        tclass.fieldsCount = fieldsCount;
        tclass.methodCount = methodCount;

        /************************************************/
        /* [4] Enter the Class Type to the Symbol Table */
        /************************************************/
        SYMBOL_TABLE.getInstance().enter(className, tclass);

        /*********************************************************/
        /* [5] Return value is irrelevant for class declarations */
        /*********************************************************/


        /** we have class type, method count and father class */

        this.tclass = tclass;

        return tclass;
    }

    public TEMP IRme()
    {
        System.out.println(String.format("IRme in filename: %s and counter is: %d", "AST_CFIELD_LIST", 1));
        IR.getInstance().Add_IRcommand(new IRcommand_Class_Dec_VTable(tclass,ext_tclass));
        System.out.println(String.format("IRme in filename: %s and counter is: %d", "AST_CFIELD_LIST", 2));
        return null;
    }
}