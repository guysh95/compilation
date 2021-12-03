package AST;

public class AST_CLASS_DEC extends AST_DEC {

    public String className;
    public String extendedClass;
    public AST_CFIELD_LIST list;

    public AST_CLASS_DEC(String className, String extendedClass, AST_CFIELD_LIST list) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (extendedClass != null)
            System.out.print("====================== classDec -> CLASS ID EXTENDS ID { cFieldList }\n");
        else
            System.out.print("====================== classDec -> CLASS ID { cFieldList }\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.className = className;
        this.extendedClass = extendedClass;
        this.list = list;
    }

    /***********************************************/
    /* The default message for a classDec Node */

    /***********************************************/
    public void PrintMe() {
        /************************************/
        /* AST NODE TYPE = EXP VAR AST NODE */
        /************************************/
        System.out.print("AST NODE CLASS DEC\n");

        /*****************************/
        /* RECURSIVELY PRINT var ... */
        /*****************************/
        if (className != null) System.out.format("class %s", className);
        if (extendedClass != null) System.out.format("extends %s", extendedClass);
        if (list != null) list.PrintMe();


        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("CLASS %s\nDEC\n", className));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (list != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, list.SerialNumber);

    }

    public TYPE SemantMe()
    {
        /*************************/
        /* [1] Begin Class Scope */
        /*************************/
        SYMBOL_TABLE.getInstance().beginScope();

        /***************************/
        /* [2] Semant Data Members */
        /***************************/
        TYPE_CLASS t = new TYPE_CLASS(null,name,data_members.SemantMe());

        /*****************/
        /* [3] End Scope */
        /*****************/
        SYMBOL_TABLE.getInstance().endScope();

        /************************************************/
        /* [4] Enter the Class Type to the Symbol Table */
        /************************************************/
        SYMBOL_TABLE.getInstance().enter(name,t);

        /*********************************************************/
        /* [5] Return value is irrelevant for class declarations */
        /*********************************************************/
        return null;
    }
}