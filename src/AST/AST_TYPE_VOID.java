package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;
import ANNOTATE_TABLE.*;

public class AST_TYPE_VOID extends AST_TYPE {

    public AST_TYPE_VOID() {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== type -> TYPE_VOID\n");
    }

    public void PrintMe() {
        /*************************************/
        /* AST NODE TYPE = AST TYPE VOID */
        /*************************************/
        System.out.print("AST NODE TYPE VOID\n");

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("TYPE_VOID"));

    }

    public TYPE SemantMe(TYPE scope) {
        return TYPE_VOID.getInstance();
    }

    public TEMP IRme()
    {
        return null; //not supposed to reach here
    }
}