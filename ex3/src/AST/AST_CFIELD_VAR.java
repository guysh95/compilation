package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CFIELD_VAR extends AST_CFIELD
{
	public AST_DEC v;
	public int row;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_CFIELD_VAR(AST_DEC v, int row)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== cfield -> varDec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.v = v;
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
		System.out.print("AST NODE CFIELD VAR DEC\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (v != null) v.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CLASS\nVAR");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,v.SerialNumber);
			
	}

	public TYPE SemantMe(String scope) {
		TYPE_CLASS_VAR_DEC t1 = null;

		t1 = (TYPE_CLASS_VAR_DEC) v.SemantMe(null);
		System.out.println("Adding class var " + t1.name);
		/* if(SYMBOL_TABLE.getInstance().findInScope(t1.name) != null) {
			System.out.format(">> ERROR variable already exists in scope\n");
			throw new lineException(Integer.toString(this.row));
			//System.exit(0);
		} */
		return t1;

		//TODO need to check if var already exists in super class
	}
}
