package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_EXPLIST extends AST_STMT
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_VAR var;
    public String id;
    public AST_EXPLIST exps;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_STMT_EXPLIST(AST_VAR var, String id, AST_EXPLIST exps)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (var != null) {
			if (exps != null) System.out.print("====================== stmt -> var.id (exp list); \n");
			else System.out.print("====================== stmt -> var.id (); \n");
		}
		if (var == null){
			if (exps != null) System.out.print("====================== stmt -> id (exp list);\n");
			else System.out.print("====================== stmt -> id ();\n");
		}

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
        this.id = id;
        this.exps = exps;
		
	}

	/******************************************************/
	/* The printing message for a statement list AST node */
	/******************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST STATEMENT LIST */
		/**************************************/
		System.out.print("AST NODE STMT EXP LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (var != null) var.PrintMe();
        if (id != null) System.out.format("ID(\"%s\"",id);
        if (exps != null) exps.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("STMT\nEXPLIST\nVAR.(%s)", id));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
        if (exps != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exps.SerialNumber);
		
	}

	public TYPE SemantMe() {
		TYPE t1 = null;
		TYPE tc = null;
		TYPE t2 = null;

		// only if we have var
		if (var != null){
			t1 = var.SemantMe();
			if (t1.isClass() == false)
			{
				System.out.format(">> ERROR [%d:%d] access %s field of a non-class variable\n",6,6,fieldName);
				System.exit(0);
			} else {
				tc = (TYPE_CLASS) t;
			}
			// check that id in class scope
			for (TYPE_LIST it=tc.data_members;it != null;it=it.tail) {
				if (it.head.name == id) {
					t2 = it.head;
				}
			}
			if (t2 == null){
				System.out.format(">> ERROR no %s field on the scope\n",id);
				System.exit(0);
			}

			if (t2 != TYPE_FUNCTION) { //todo: is this how you check the type?
				System.out.format(">> ERROR provided explist although this is not a function");
				System.exit(0);
			}
			if (exps != null){
				exps.SemantMe();
			}

			return null;
		} else { // var is null
			t2 = SYMBOL_TABLE.get_instance().find(id);
			if(t2 != TYPE_FUNCTION){
				System.out.format(">> ERROR provided explist although this is not a function");
				System.exit(0);
			}
			if (exps != null){
				exps.SemantMe();
			}
			return null;
		}
	}
	//TODO add semantMe()
}
