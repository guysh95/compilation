package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_TYPE_STRING extends AST_TYPE {

    public AST_TYPE_STRING() {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== type -> TYPE_STRING\n");
    }

    public void PrintMe() {
        /*************************************/
        /* AST NODE TYPE = AST TYPE STRING */
        /*************************************/
        System.out.print("AST NODE TYPE STRING\n");

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("TYPE_STRING"));

    }

    public TYPE SemantMe(TYPE scope) {
        return TYPE_STRING.getInstance();
    }
}
