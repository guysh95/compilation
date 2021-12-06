package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_VARDEC extends AST_STMT {

    public AST_DEC_VAR vd;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_STMT_VARDEC(AST_DEC_VAR vd)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (vd != null) System.out.print("====================== stmt -> varDec\n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.vd = vd;
    }

    /******************************************************/
    /* The printing message for a statement list AST node */
    /******************************************************/
    public void PrintMe()
    {
        /**************************************/
        /* AST NODE TYPE = AST STATEMENT LIST */
        /**************************************/
        System.out.print("AST STMT TO VARDEC\n");

        /*************************************/
        /* RECURSIVELY PRINT HEAD + TAIL ... */
        /*************************************/
        if (vd != null) vd.PrintMe();

        /**********************************/
        /* PRINT to AST GRAPHVIZ DOT file */
        /**********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "STMT\nTO\nVARDEC\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (vd != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,vd.SerialNumber);
    }

}
