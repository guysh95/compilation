package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;

public class AST_PROGRAM extends AST_Node{

    public AST_DEC head;
    public AST_PROGRAM tail;
    public int row;

    public AST_PROGRAM(AST_DEC head, AST_PROGRAM tail, int row) {
        /******************************/
        /* SET A UNIQUE SERIAL NUMBER */
        /******************************/
        SerialNumber = AST_Node_Serial_Number.getFresh();

        /***************************************/
        /* PRINT CORRESPONDING DERIVATION RULE */
        /***************************************/
        if (tail != null) System.out.print("====================== Program -> dec Program\n");
        if (tail == null) System.out.print("====================== Program -> dec\n");

        /*******************************/
        /* COPY INPUT DATA NENBERS ... */
        /*******************************/
        this.head = head;
        this.tail = tail;
        this.row = row;
    }

    public void PrintMe() {
        /**************************************/
        /* AST NODE TYPE = AST PROGRAM */
        /**************************************/
        System.out.print("AST NODE PROGRAM\n");

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
                "PROGRAM\n");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
        if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
    }

    public TYPE SemantMe(TYPE scope)
    {
        /*************************************/
        /* RECURSIVELY PRINT HEAD + TAIL ... */
        /*************************************/
        if (head != null) head.SemantMe(scope);
        if (tail != null) tail.SemantMe(scope);
        System.out.println("now we semnat program");

        return null;
    }

    public TEMP IRme(){
        // no need to return anything - all stored, need only to IR all decs
        head.IRme();
        for(AST_PROGRAM curr = tail; curr != null; curr = curr.tail){
            curr.head.IRme();
            //tlist = new TEMP_LIST(t1, tlist);
        }
        return null;
    }

    public void AnnotateMe()
    {
        head.AnnotateMe();
        for(AST_PROGRAM curr = tail; curr != null; curr = curr.tail){
            curr.head.AnnotateMe();
            //tlist = new TEMP_LIST(t1, tlist);
        }
        return null;
    }
}
