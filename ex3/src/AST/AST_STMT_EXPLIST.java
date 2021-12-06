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
	
}
