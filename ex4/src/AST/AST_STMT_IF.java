package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;
import REG_ALLOC.*;

public class AST_STMT_IF extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;
	public TYPE expReturnType;
	public int row;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_IF(AST_EXP cond,AST_STMT_LIST body, int row)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> IF (EXP) {stmtList}\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.cond = cond;
		this.body = body;
		this.row = row;
	}

	/**************************************************/
	/* The printing message for a if AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST IF STMT NODE */
		/**********************************/
		System.out.print("AST NODE WHILE STMT");

		/*************************************/
		/* RECURSIVELY PRINT COND + BODY ... */
		/*************************************/
		if (cond != null) cond.PrintMe();
		if (body != null) body.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"IF\nSTMT");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (cond != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cond.SerialNumber);
		if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
	}

	public TYPE SemantMe(TYPE scope)
	{
		/****************************/
		/* [0] Semant the Condition */
		/****************************/
		if (cond.SemantMe(scope) != TYPE_INT.getInstance())
		{
			System.out.format(">> ERROR [%d:%d] condition inside IF is not integral\n",2,2);
			throw new lineException(Integer.toString(this.row));
			//System.exit(0);


		}

		/*************************/
		/* [1] Begin Class Scope */
		/*************************/
		SYMBOL_TABLE.getInstance().beginScope();

		/***************************/
		/* [2] Semant Data Members */
		/***************************/
		body.SemantFunctionMe(scope, expReturnType);

		/*****************/
		/* [3] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope(false);

		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;
	}

	public void SemantBodyMe(TYPE scope, TYPE returnType) {
		this.expReturnType = returnType;
		this.SemantMe(scope);
	}

	public TEMP IRme()
	{
		/*******************************/
		/* [1] Allocate fresh label */
		/*******************************/
		String label_end   = IRcommand.getFreshLabel("end_if");

		/********************/
		/* [3] cond.IRme(); */
		/********************/
		TEMP cond_temp = cond.IRme();

		/******************************************/
		/* [4] Jump conditionally to the loop end */
		/******************************************/
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Not_Eq_To_Zero(cond_temp,label_end));

		CFG_node node1 = CFG.getInstance().getCFGTail(); //supposed to get the node for the jump command

		/*******************/
		/* [5] body.IRme() */
		/*******************/
		body.listIRme();

		/**********************/
		/* [7] Loop end label */
		/**********************/
		IR.getInstance().Add_IRcommand(new IRcommand_Label(label_end));

		CFG_node node2 = CFG.getInstance().getCFGTail(); //supposed to get the node for the label command
		// add to the nodes the jump values
		node1.jumpTo = node2;
		node2.jumpFrom = node1;
		/*******************/
		/* [8] return null */
		/*******************/
		return null;
	}

	public void AnnotateMe()
	{
		cond.AnnotateMe();
		body.AnnotateMe();
	}
}