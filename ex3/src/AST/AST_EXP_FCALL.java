package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_FCALL extends AST_EXP {
    /****************/
    /* DATA MEMBERS */
    /****************/
    public AST_VAR caller;
    public String name;
    public AST_EXPLIST explist;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXP_FCALL(AST_VAR caller, String name, AST_EXPLIST explist) {
            /******************************/
            /* SET A UNIQUE SERIAL NUMBER */
            /******************************/
            SerialNumber = AST_Node_Serial_Number.getFresh();

            /***************************************/
            /* PRINT CORRESPONDING DERIVATION RULE */
            /***************************************/
            if (explist != null) {
                if (caller != null) System.out.format("====================== exp -> var . ID(%s) ( expList )\n", name);
                else System.out.format("====================== exp -> ID(%s) ( expList )\n", name);
            }
            else {
                if (caller != null) System.out.format("====================== exp -> var . ID(%s) ()\n", name);
                else System.out.format("====================== exp -> ID(%s) ()\n", name);
            }

            /*******************************/
            /* COPY INPUT DATA NENBERS ... */
            /*******************************/
            this.caller = caller;
            this.name = name;
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
            if (name!= null) System.out.format(".ID( %s )", name);
        }
        else System.out.format("ID( %s )", name);
        if (explist != null) explist.PrintMe();

        /**********************************/
        /* PRINT to AST GRAPHVIZ DOT file */
        /**********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("FUNC\nCALL\nID(%s)\n", name));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (caller != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,caller.SerialNumber);
        if (explist != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,explist.SerialNumber);
    }

    //TODO add SemantMe()
    //consider functions scope and function arguments etc.
}
