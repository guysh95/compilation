package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CFIELD_FUNC extends AST_CFIELD
{
	public AST_DEC v;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_CFIELD_FUNC(AST_DEC v)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== cfield -> funcDec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.v = v;
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		System.out.print("AST NODE CFIELD FUNC DEC\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (v != null) v.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CLASS\nFUNC");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,v.SerialNumber);
	}

	public TYPE SemantMe() {
		AST_FUNCDEC func = (AST_FUNCDEC) v;
		TYPE_FUNCTION tf;
		if (v != null) tf = (TYPE_FUNCTION) v.SemantMe();
		//TODO fix all cases of semant me
		//TODO need to check if function already exists in super class
		//TODO need to consider types of functions
	}
}
