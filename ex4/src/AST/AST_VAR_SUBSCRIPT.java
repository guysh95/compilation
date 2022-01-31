package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;

public class AST_VAR_SUBSCRIPT extends AST_VAR
{
	public AST_VAR var;
	public AST_EXP subscript;
	public int row;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SUBSCRIPT(AST_VAR var,AST_EXP subscript, int row)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== var -> var [ exp ]\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.subscript = subscript;
		this.row = row;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSRIPT ... */
		/****************************************/
		if (var != null) var.PrintMe();
		if (subscript != null) subscript.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"SUBSCRIPT\nVAR\n...[...]");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var       != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (subscript != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,subscript.SerialNumber);
	}

	public TYPE SemantMe(TYPE scope) {
		TYPE t = null;
		TYPE_ARRAY ta = null;
		TYPE sub_t = null;

		/******************************/
		/* [1] Recursively semant var */
		/******************************/
		if (var != null) t = var.SemantMe(scope);

		/*********************************/
		/* [2] Make sure type is a Array */
		/*********************************/
		if (t.isArray() == false) {
			System.out.format(">> ERROR [%d:%d] access subscript of a non-array variable\n",6,6);
			throw new lineException(Integer.toString(this.row));
			//System.exit(0);
		}
		else {
			ta = (TYPE_ARRAY) t;
		}
		/************************************/
		/* [3] Make sure subscript is integral */
		/************************************/
		if (subscript.SemantMe(scope) != TYPE_INT.getInstance()) {
			System.out.format(">> ERROR [%d:%d] expression inside subscript is not integral\n",2,2);
			throw new lineException(Integer.toString(this.row));
			//System.exit(0);
		}

		return ta.member_type;
	}

	public TEMP IRme()
	{
		TEMP dest = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP t1 = var.IRme();
		TEMP t2 = subscript.IRme();
		IR.getInstance().Add_IRcommand(new IRcommand_Array_Access(dest, t1, t2));
		return dest;
	}

	public TEMP assignIRme(TEMP texp){
		TEMP t1 = var.IRme();
		TEMP t2 = subscript.IRme();

		IR.getInstance().Add_IRcommand(new IRcommand_Array_Set(t1, t2, texp));
		// no need to return anything because we assign it - and finish with store
		return null;
	}

	public void AnnotateMe(){
		var.AnnotateMe();
		exp.AnnotateMe();
	}
}
