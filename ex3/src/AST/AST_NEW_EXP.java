package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_NEW_EXP extends AST_Node {

    public AST_TYPE new_name;
    public AST_EXP e;
    public int row;


    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_NEW_EXP(AST_TYPE new_name, AST_EXP e, int row) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (e != null) System.out.print("====================== newExp -> NEW type LBRACK exp RBRACK\n");
        else System.out.print("====================== newExp -> NEW type\n");


        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.new_name = new_name;
        this.e = e;
        this.row = row;

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

    public TYPE SemantMe(String scope) {
        TYPE t1 = null;
        TYPE t2 = null;

        /****************************************/
        /* Check Type is in symbol table and isn't TYPE_VOID */
        /****************************************/
        if (new_name != null) t1 = new_name.SemantMe(scope);
        System.out.println(t1.name);
        if (t1 == TYPE_VOID.getInstance()){
            System.out.format(">> ERROR [%d:%d] type void is non instanceable\n",7,7);
            throw new lineException(Integer.toString(this.row));
            //System.exit(0);
        }


        if (e != null) {
            /****************************************/
            /* Check e is integral (maybe needs to add checks here) */
            /****************************************/
            System.out.println("type checking for :" + t1);
            if(!t1.isArray() && !t1.isClass() && !t1.name.equals("int") && !t1.name.equals("string")) {
                System.out.format(">> ERROR [%d:%d] type is not suitable for array\n",2,2);
                throw new lineException(Integer.toString(this.row));
                //System.exit(0);
            }
            t2 = e.SemantMe(scope);
            if (t2 != TYPE_INT.getInstance()){
                System.out.format(">> ERROR [%d:%d] expression inside subscript is not integral\n",2,2);
                throw new lineException(Integer.toString(this.row));
                //System.exit(0);
            }
            if (e.getClass().getSimpleName().equals("AST_EXP_INT")){
                AST_EXP_INT num = (AST_EXP_INT) e;
                if (num.value <= 0){
                    System.out.format(">> ERROR [%d:%d] try to init array to size smaller then zero\n",2,2);
                    throw new lineException(Integer.toString(this.row));
                    //System.exit(0);
                }
            }
            return new TYPE_ARRAY("array", t1);
        }
        System.out.println("We check that " + t1.name + " == " + scope + " in AST_NEW_EXP");
        if(t1.isClass() == true || t1.name.equals(scope)) {
            return t1;
        } else { //t1 is not class
            System.out.format(">> ERROR [%d:%d] type is not class\n",2,2);
            throw new lineException(Integer.toString(this.row));
        }

    }
}
