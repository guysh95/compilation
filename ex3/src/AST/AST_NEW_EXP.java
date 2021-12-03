package AST;

public class AST_NEW_EXP extends AST_Node {

    public AST_TYPE new_name;
    public AST_EXP e;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_NEW_EXP(AST_TYPE new_name, AST_EXP e) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (e != null) System.out.print("====================== newExp -> NEW type LPAREN exp RPRAEN\n");
        else System.out.print("====================== newExp -> NEW type\n");


        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.new_name = new_name;
        this.e = e;
    }

    /***********************************************/
    /* The default message for an new exp AST node */
    /***********************************************/
    public void PrintMe()
    {
        /************************************/
        /* AST NODE TYPE = NEW EXP NODE */
        /************************************/
        System.out.print("AST NODE NEW EXP\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (new_name != null) new_name.PrintMe();
        if (e != null) e.PrintMe();

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "NEW\nTYPE");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (new_name != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,new_name.SerialNumber);
        if (e != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,e.SerialNumber);
    }
}
