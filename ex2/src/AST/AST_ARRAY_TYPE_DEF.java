package AST;

public class AST_ARRAY_TYPE_DEF extends AST_DEC {
    public AST_TYPE type;
    public String arrayName;

    public AST_ARRAY_TYPE_DEF(String name, AST_TYPE type) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.print("====================== arrayTypedef -> ARRAY ID EQ type LBRACK RBRACK SEMICOLON\n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.type = type;
        this.arrayName = name;
    }

    /***********************************************/
    /* The default message for an exp var AST node */
    /***********************************************/
    public void PrintMe()
    {
        /************************************/
        /* AST NODE TYPE = ARRAY TYPEDEF */
        /************************************/
        System.out.print("AST NODE ARRAY TYPEDEF\n");

        /*****************************/
        /* RECURSIVELY PRINT ARRAY NAME, then TYPE */
        /*****************************/
        System.out.format("ARRAY NAME(%s)", arrayName);
        if (type != null) type.PrintMe();


        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("ARRAY\nTYPEDEF\n...->%s", arrayName));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);


    }
}