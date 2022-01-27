package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;
import REG_ALLOC.*;

public class AST_STMT_WHILE extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;
	public TYPE expReturnType;
	public int row;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_WHILE(AST_EXP cond,AST_STMT_LIST body, int row)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> WHILE (EXP) {stmtList}\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.cond = cond;
		this.body = body;
		this.row = row;
	}

	/**************************************************/
	/* The printing message for a simple type AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST WHILE STMT NODE */
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
				"WHILE\nSTMT");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (cond != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cond.SerialNumber);
		if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
	}

	public TYPE SemantMe(TYPE scope) {
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
		/* [1] Allocate 2 fresh labels */
		/*******************************/
		String label_end   = IRcommand.getFreshLabel("end_while");
		String label_start = IRcommand.getFreshLabel("start_while");

		/*********************************/
		/* [2] entry label for the while */
		/*********************************/
		IR.getInstance().Add_IRcommand(new IRcommand_Label(label_start));

		CFG_node node_start_label = CFG.getInstance().getCFGTail(); //supposed to get the node for the jump command

		/********************/
		/* [3] cond.IRme(); */
		/********************/
		TEMP cond_temp = cond.IRme();

		/******************************************/
		/* [4] Jump conditionally to the loop end */
		/******************************************/
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(cond_temp,label_end));

		CFG_node node_jump_end = CFG.getInstance().getCFGTail(); //supposed to get the node for the jump command

		/*******************/
		/* [5] body.IRme() */
		/*******************/
		body.listIRme();

		/******************************/
		/* [6] Jump to the loop entry */
		/******************************/
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_Label(label_start));

		CFG_node node_jump_start = CFG.getInstance().getCFGTail(); //supposed to get the node for the jump command

		node_start_label.jumpFrom = node_jump_start;
		/**********************/
		/* [7] Loop end label */
		/**********************/
		IR.getInstance().Add_IRcommand(new IRcommand_Label(label_end));

		CFG_node node_end_label = CFG.getInstance().getCFGTail(); //supposed to get the node for the jump command
		node_jump_end.jumpTo = node_end_label;
		node_end_label.prev = node_jump_end; // fix prev of end label to be from to conditional jump

		node_jump_start.next = node_start_label; //fix next from jump to be to start label




		/*******************/
		/* [8] return null */
		/*******************/
		return null;
	}

}