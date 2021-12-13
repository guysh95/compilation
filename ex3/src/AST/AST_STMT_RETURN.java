package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_RETURN extends AST_STMT
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_EXP exp;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_STMT_RETURN(AST_EXP exp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (exp != null) System.out.print("====================== stmt -> RETURN exp\n");
		if (exp == null) System.out.print("====================== stmt -> RETURN      \n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.exp = exp;
		
	}

	/******************************************************/
	/* The printing message for a statement list AST node */
	/******************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST STATEMENT LIST */
		/**************************************/
		System.out.print("AST NODE STMT LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (exp != null) exp.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"STMT\nRETURN\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
		
	}

	public TYPE SemantMe(){
		TYPE t = null;

		if (exp == null){
			return TYPE_VOID.getInstance();
		} else {
			t = exp.SemantMe();
			return t;
		}
	}
}
