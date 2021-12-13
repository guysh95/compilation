package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_TYPE_SIMPLE extends AST_TYPE {

    /************************/
    /* simple variable name */
    /************************/
    public String type;
    public int row;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_TYPE_SIMPLE(String type, int row) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        System.out.format("====================== type -> ID( %s )\n", type);

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.type = type;
        this.row = row;
    }

    /**************************************************/
    /* The printing message for a simple type AST node */
    /**************************************************/
    public void PrintMe()
    {
        /**********************************/
        /* AST NODE TYPE = AST SIMPLE TYPE */
        /**********************************/
        System.out.format("AST NODE SIMPLE TYPE( %s )\n", type);

        /*********************************/
        /* Print to AST GRAPHIZ DOT file */
        /*********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("TYPE\n(%s)", type));
    }

    public TYPE SemantMe() {
        TYPE t;

        /****************************/
        /* [1] Check If Type exists */
        /****************************/
        t = SYMBOL_TABLE.getInstance().find(type);
        if (t == null)
        {
            System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,type);
            throw new lineException(Integer.toString(this.row));
            //System.exit(0);
        }
        if(type.equals("int") || type.equals("string") || type.equals("void") || t.isClass() || t.isArray()) {
            return t;
        }
        System.out.format(">> ERROR [%d:%d] %s non existing type\n",2,2,type);
        System.exit(0);
        return null;
    }
}
