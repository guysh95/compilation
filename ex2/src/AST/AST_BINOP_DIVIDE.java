package AST;

public class AST_BINOP_DIVIDE extends AST_BINOP {

    public AST_BINOP_DIVIDE() {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== BINOP -> DIVIDE\n");
    }

    public void PrintMe() {

        /*************************************/
        /* AST NODE TYPE = AST BINOP EXP */
        /*************************************/
        System.out.print("AST NODE BINOP DIVIDE\n");

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("/"));

    }
}