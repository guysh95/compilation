package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;
import ANNOTATE_TABLE.*;

public class AST_STMT_RETURN extends AST_STMT
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_EXP exp;
	public TYPE expectedReturnType;
	public int row;
	String funcName;
	String methodOwner = null;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_STMT_RETURN(AST_EXP exp, int row)
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
		this.row = row;
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

	public TYPE SemantMe(TYPE scope){
		TYPE t = null;
		if (scope != null && scope.isClass()) {
			TYPE_CLASS tc = (TYPE_CLASS)scope;
			this.methodOwner = tc.name;
		}
		if (exp == null){
			return TYPE_VOID.getInstance();
		} else {
			if (expectedReturnType == TYPE_VOID.getInstance()) {
				System.out.println(">> ERROR void funtion doesn't return argument");
				throw new lineException(Integer.toString(this.row));
			}
			t = exp.SemantMe(scope);
			return t;
		}
	}

	public TYPE SemantReturnMe(TYPE scope, TYPE returnType, String funcName){
		this.expectedReturnType = returnType;
		this.funcName = funcName;
		return this.SemantMe(scope);
	}

	public TEMP IRme(){
		TEMP t1 = null;
		if (exp != null){
			t1 = exp.IRme();
		}
		String returnOwner;
		if(methodOwner == null) {
			returnOwner = funcName;
		}
		else {
			returnOwner = methodOwner + "_" + funcName;
		}
		//TODO: figure function name for return mips command
		IR.getInstance().Add_IRcommand(new IRcommand_Return(t1, returnOwner));
		return t1; // if no exp then t1 = null

	}

	public void AnnotateMe() {
		if (exp != null) exp.AnnotateMe();
	}
}
