package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_ASSIGN_NEW extends AST_STMT
{
	public AST_VAR var;
    public AST_NEW_EXP exp;
	public int row;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_STMT_ASSIGN_NEW(AST_VAR var, AST_NEW_EXP exp, int row)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> exp ASSIGN NEW exp\n");
        

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
        this.exp = exp;
		this.row = row;

	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		System.out.print("AST STMT ASSIGN NEW EXP\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
        if (var != null) var.PrintMe();
        if (exp != null) exp.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"STMT\nNEW EXP");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
        if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
        if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
			
	}

	public TYPE SemantMe(String scope)
	{
		TYPE t1 = null;
		TYPE t2 = null;

		if (var != null) t1 = var.SemantMe(null);
		if (exp != null) t2 = exp.SemantMe(null);

		if (t1 != t2)
		{
			System.out.format(">> ERROR [%d:%d] type mismatch for var := exp\n",6,6);
			throw new lineException(Integer.toString(this.row));
			//System.exit(0);

		}
		return null;
	}
}
