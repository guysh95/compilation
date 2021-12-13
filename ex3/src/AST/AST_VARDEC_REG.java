package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VARDEC_REG extends AST_DEC
{
	public AST_TYPE type;
    public String id;
	public int row;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VARDEC_REG(AST_TYPE type, String id, int row)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
        System.out.print("====================== varDec -> type ID;\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
        this.id = id;
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
		System.out.print("AST NODE VAR DEC\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (type != null) type.PrintMe();
        System.out.format("VAR ID(%s)", id);
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("DECLARE\nVAR (%s)\n", id));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
			
	}

	public TYPE SemantMe(String scope) {

		TYPE t = null;

		/****************************/
		/* [1] Check If Type exists */
		/****************************/
		t = type.SemantMe(null);

		/**************************************/
		/* [2] Check That Name does NOT exist */
		/**************************************/
		if (SYMBOL_TABLE.getInstance().findInScope(id) != null)
		{
			System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n",2,2,id);

			throw new lineException(Integer.toString(this.row));
			//System.exit(0);

		}
		if (t == TYPE_VOID.getInstance()){
			System.out.print("void variable isn't defined");
			throw new lineException(Integer.toString(this.row));

		}
		/***************************************************/
		/* [3] Enter the Function Type to the Symbol Table */
		/***************************************************/
		TYPE_CLASS_VAR_DEC t3 = new TYPE_CLASS_VAR_DEC(t, id);
		System.out.println("now we are at vardec reg with: " + id);
		System.out.println("t3 is now: "+ t3.name + " and its type is " + t3.t.name);
		SYMBOL_TABLE.getInstance().enter(id,t);

		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		return t3;
	}
}
