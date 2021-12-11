package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_FCALL extends AST_EXP {
    /****************/
    /* DATA MEMBERS */
    /****************/
    public AST_VAR caller;
    public String fieldName;
    public AST_EXPLIST explist;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXP_FCALL(AST_VAR caller, String fieldName, AST_EXPLIST explist) {
            /******************************/
            /* SET A UNIQUE SERIAL NUMBER */
            /******************************/
            SerialNumber = AST_Node_Serial_Number.getFresh();

            /***************************************/
            /* PRINT CORRESPONDING DERIVATION RULE */
            /***************************************/
            if (explist != null) {
                if (caller != null) System.out.format("====================== exp -> var . ID(%s) ( expList )\n", fieldName);
                else System.out.format("====================== exp -> ID(%s) ( expList )\n", fieldName);
            }
            else {
                if (caller != null) System.out.format("====================== exp -> var . ID(%s) ()\n", fieldName);
                else System.out.format("====================== exp -> ID(%s) ()\n", fieldName);
            }

            /*******************************/
            /* COPY INPUT DATA NENBERS ... */
            /*******************************/
            this.caller = caller;
            this.fieldName = fieldName;
            this.explist = explist;
    }

    /******************************************************/
    /* The printing message for a function call AST node */
    /******************************************************/
    public void PrintMe()
    {
        /**************************************/
        /* AST NODE TYPE = AST EXP FUNCTION CALL */
        /**************************************/
        System.out.print("AST EXP FUNC CALL\n");

        /*************************************/
        /* RECURSIVELY PRINT CALLER + expList ... */
        /*************************************/
        if (caller != null) {
            caller.PrintMe();
            if (fieldName!= null) System.out.format(".ID( %s )", fieldName);
        }
        else System.out.format("ID( %s )", fieldName);
        if (explist != null) explist.PrintMe();

        /**********************************/
        /* PRINT to AST GRAPHVIZ DOT file */
        /**********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("FUNC\nCALL\nID(%s)\n", fieldName));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (caller != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,caller.SerialNumber);
        if (explist != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,explist.SerialNumber);
    }

    /* public TYPE SemantMe() {
        TYPE t = null;
        TYPE tc = null;
        TYPE tfunc = null;



        // when there is var - check that the var is class type and that the function within class scope
        if (caller != null){
            t = caller.SemantMe();


            if (t.isClass() == false)
            {
                System.out.format(">> ERROR [%d:%d] access %s field of a non-class variable\n",6,6,fieldName);
                System.exit(0);
            }
            else
            {
                tc = (TYPE_CLASS) t;
            }


            for (TYPE_LIST it=tc.data_members;it != null;it=it.tail)
            {
                if (it.head.name == fieldName)
                {
                    tfunc = it.head;
                }
            }
        }

        // when there is no var - check that the function exists


    } */
    public TYPE SemantMe() {
        TYPE t1 = null;
        TYPE_CLASS tc = null;
        TYPE t2 = null;

        // only if we have caller
        if (caller != null){
            t1 = caller.SemantMe();
            if (t1.isClass() == false)
            {
                System.out.format(">> ERROR [%d:%d] access %s field of a non-class variable\n",6,6,fieldName);
                System.exit(0);
            } else {
                tc = (TYPE_CLASS) t1;
            }
            // check that fieldName in class scope
            for (TYPE_LIST it=tc.data_members;it != null;it=it.tail) {
                if (it.head.name == fieldName) {
                    t2 = it.head;
                }
            }
            if (t2 == null){
                System.out.format(">> ERROR no %s field on the scope\n",fieldName);
                System.exit(0);
            }

            if (t2 != TYPE_FUNCTION) { //todo: is this how you check the type?
                System.out.format(">> ERROR provided explist although this is not a function");
                System.exit(0);
            }
            if (exps != null){
                exps.SemantMe();
            }

            return null;
        } else { // caller is null
            t2 = SYMBOL_TABLE.get_instance().find(fieldName);
            if(t2 != TYPE_FUNCTION){
                System.out.format(">> ERROR provided explist although this is not a function");
                System.exit(0);
            }
            if (exps != null){
                exps.SemantMe();
            }
            return null;
        }
    }

    //TODO add SemantMe()
    //consider functions scope and function arguments etc.
}
