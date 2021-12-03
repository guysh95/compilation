package AST;

public class AST_TYPE_INT extends AST_TYPE {

    public AST_TYPE_INT() {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== type -> TYPE_INT\n");
    }

    public void PrintMe() {
        /*************************************/
        /* AST NODE TYPE = AST TYPE INT */
        /*************************************/
        System.out.print("AST NODE TYPE INT\n");

        /***************************************/
        /* PRINT Node to AST GRAPHVIZ DOT file */
        /***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("TYPE_INT"));
    }

    public TYPE SemantMe() {
        return TYPE_INT.getInstance();
    }
}
