package AST;

public class AST_EXP_STRING extends AST_EXP {

    public String str_val;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXP_STRING(String str_val)
    {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== exp -> STRING( %s )\n", str_val);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.str_val = str_val;
    }


    /************************************************/
    /* The printing message for an STRING EXP AST node */
    /************************************************/
    public void PrintMe()
    {
        /*******************************/
        /* AST NODE TYPE = AST STRING EXP */
        /*******************************/
        System.out.format("AST NODE STRING( %s )\n",str_val);

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("STRING(%s)",str_val.replace('"','\'')));
    }
}
