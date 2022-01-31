package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;

public class AST_ARRAY_TYPE_DEF extends AST_DEC {
    public AST_TYPE type;
    public String arrayName;
    public int row;

    public AST_ARRAY_TYPE_DEF(String name, AST_TYPE type, int row) {
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
        this.row = row;
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

    public TYPE SemantMe(TYPE scope) {
        /************************************/
        /* [0] Check we are in global scope */
        /************************************/
        if(SYMBOL_TABLE.getInstance().isGlobalScope() == false) {
            System.out.print("array definition isn't in global scope");
            throw new lineException(Integer.toString(this.row));


        }
        /*****************************************/
        /* [1] Check if array is already defined */
        /*****************************************/
        if(SYMBOL_TABLE.getInstance().find(arrayName) != null) {
            System.out.print("array is already defined");
            throw new lineException(Integer.toString(this.row));

        }
        /***************************/
        /* [2] Semant members type */
        /***************************/
        TYPE t = type.SemantMe(scope);
        if (t == TYPE_VOID.getInstance()){
            System.out.print("void array isn't defined");
            throw new lineException(Integer.toString(this.row));

        }
        TYPE_ARRAY ta = new TYPE_ARRAY(arrayName, t);
        /************************************************/
        /* [3] Enter the Array Type to the Symbol Table */
        /************************************************/
        SYMBOL_TABLE.getInstance().enter(arrayName, ta);
        return ta;
    }

    public TEMP IRme()
    {
        return null; //not supposed to reach here
    }

    public void AnnotateMe()
    {
        //TODO: Maybe need to do something here
    }
}
