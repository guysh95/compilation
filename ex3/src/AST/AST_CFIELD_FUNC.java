package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CFIELD_FUNC extends AST_CFIELD
{
	public AST_DEC v;
	public int row;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_CFIELD_FUNC(AST_DEC v, int row)
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

	public TYPE SemantMe(String scope) {
		/* AST_FUNCDEC func = (AST_FUNCDEC) v;
		TYPE_FUNCTION tf;
		if (v != null) tf = (TYPE_FUNCTION) v.semantme(scope);
		*/

		TYPE t1 = null;
		t1 = v.SemantMe(scope);
		System.out.println("Searching " + t1.name + " in " + scope);
		if(SYMBOL_TABLE.getInstance().findInScope(t1.name) != null) {
			System.out.format(">> ERROR function name already exists in scope\n");
			throw new lineException(Integer.toString(this.row));
		}

		return t1;

		//TODO fix all cases of semant me
		//TODO need to consider types of functions
	}
}
