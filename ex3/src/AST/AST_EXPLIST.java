package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXPLIST extends AST_Node {
    /****************/
    /* DATA MEMBERS */
    /****************/
    public AST_EXP head;
    public AST_EXPLIST tail;
    public TYPE_LIST allTypes = null;
    public int row;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXPLIST(AST_EXP head,AST_EXPLIST tail, int row) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (tail != null) System.out.print("====================== expList -> exp expList\n");
        if (tail == null) System.out.print("====================== expList -> exp        \n");

        /*******************************/
        /* COPY INPUT DATA MEMBERS ... */
        /*******************************/
        this.head = head;
        this.tail = tail;
        this.row = row;
    }

    /******************************************************/
    /* The printing message for an expression list AST node */
    /******************************************************/
    public void PrintMe()
    {
        /**************************************/
        /* AST NODE TYPE = AST EXP LIST */
        /**************************************/
        System.out.print("AST NODE EXP LIST\n");

        /*************************************/
        /* RECURSIVELY PRINT HEAD + TAIL ... */
        /*************************************/
        if (head != null) head.PrintMe();
        if (tail != null) tail.PrintMe();

        /**********************************/
        /* PRINT to AST GRAPHVIZ DOT file */
        /**********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                "EXP\nLIST\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
        if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
    }

    public TYPE SemantMe(TYPE scope) {
        TYPE t = null;
        if (head == null){
            System.out.print(">> ERROR in EXPLIST semantme");
            throw new lineException(Integer.toString(this.row));

        }
        System.out.println("peleg is magniv");
        t = head.SemantMe(scope);
        System.out.println("peleg is legend");
        allTypes = new TYPE_LIST(t, null);
        for(AST_EXPLIST pointer = tail; pointer != null; pointer = pointer.tail){
            System.out.println("peleg is cool");
            t = pointer.head.SemantMe(scope);
            allTypes = new TYPE_LIST(t, allTypes);
        }
        System.out.println("peleg is gever");
        return null;
    }

    public TYPE_LIST getTypes(TYPE scope) {
        this.SemantMe(scope);
        System.out.println("peleg peleg peleg");
        return allTypes;
    }

}
