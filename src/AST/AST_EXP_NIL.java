package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;
import ANNOTATE_TABLE.*;

public class AST_EXP_NIL extends AST_EXP {


    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXP_NIL() {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();


        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== exp -> NIL\n");
    }
    /************************************************/
    /* The printing message for an NIL EXP AST node */
    /************************************************/
    public void PrintMe()
    {
        /*******************************/
        /* AST NODE TYPE = AST INT EXP */
        /*******************************/
        System.out.format("AST NODE NIL\n");

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "NIL");
    }

    public TYPE SemantMe(TYPE scope) {
        return TYPE_NIL.getInstance();
    }

    public TEMP IRme()
    {
        System.out.println(String.format("IRme in filename: %s and counter is: %d", "AST_EXP_NIL", 1));
        TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommandConstNIL(t));
        return t;
    }


}
