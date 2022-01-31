package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;

public class AST_EXP_PAREN extends AST_EXP {

    public AST_EXP child;
    public int row;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXP_PAREN(AST_EXP child, int row)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== exp -> LPAREN exp RPAREN\n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.child = child;
        this.row = row;
    }

    /************************************************/
    /* The printing message for an NESTED EXP AST node */
    /************************************************/
    public void PrintMe(String scope)
    {
        /*******************************/
        /* AST NODE TYPE = AST NESTED EXP */
        /*******************************/
        System.out.format("AST NODE ( EXP )\n");

        /**************************************/
        /* RECURSIVELY PRINT child */
        /**************************************/
        if (child != null) child.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "AST NODE ( EXP )");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (child != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,child.SerialNumber);
    }

    public TYPE SemantMe(TYPE scope) {
        if (child != null) return child.SemantMe(scope);
        return null;
    }

    public TEMP IRme()
    {
        if (child != null) return child.IRme();
        return null;
    }

    public void AnnotateMe() {
        if (child != null) child.AnnotateMe();
    }
}
