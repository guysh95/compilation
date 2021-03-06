package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;
import ANNOTATE_TABLE.*;

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
		SerialNumber = AST_Node_Serial_Number.getFresh();
		this.var = var;
		this.subscript = subscript;
		this.row = row;
	}

	public void PrintMe()
	{
		System.out.print("AST NODE SUBSCRIPT VAR\n");
		if (var != null) var.PrintMe();
		if (subscript != null) subscript.PrintMe();
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"SUBSCRIPT\nVAR\n...[...]");
		if (var       != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (subscript != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,subscript.SerialNumber);
	}

	public TYPE SemantMe(TYPE scope) {
		TYPE t = null;
		TYPE_ARRAY ta = null;
		TYPE sub_t = null;
		System.out.println("We are Semanting in AST_VAR_SUBSCRIPT BIBI");
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
		}
		else {
			ta = (TYPE_ARRAY) t;
		}
		/***************************************/
		/* [3] Make sure subscript is integral */
		/***************************************/
		if (subscript.SemantMe(scope) != TYPE_INT.getInstance()) {
			System.out.format(">> ERROR [%d:%d] expression inside subscript is not integral\n",2,2);
			throw new lineException(Integer.toString(this.row));
		}
		return ta.member_type;
	}

	public TEMP IRme()
	{
		System.out.println(String.format("IRme in filename: %s and counter is: %d, %s", "AST_VAR_SUBSCRIPT", 1, "start IRme"));
		TEMP dest = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP t1 = var.IRme();
		System.out.println(String.format("IRme in filename: %s and counter is: %d, %s", "AST_VAR_SUBSCRIPT", 2, "finished IR var"));
		TEMP t2 = subscript.IRme();
		System.out.println(String.format("IRme in filename: %s and counter is: %d, %s", "AST_VAR_SUBSCRIPT", 3, "finished IR subscript"));
		IR.getInstance().Add_IRcommand(new IRcommand_Array_Access(dest, t1, t2));
		return dest;
	}

	public TEMP assignIRme(TEMP texp){
		System.out.println(String.format("IRme in filename: %s and counter is: %d, %s", "AST_VAR_SUBSCRIPT", 4, "start assignIRme"));
		TEMP t1 = var.IRme();
		System.out.println(String.format("IRme in filename: %s and counter is: %d, %s", "AST_VAR_SUBSCRIPT", 5, "finished IR var"));
		TEMP t2 = subscript.IRme();
		System.out.println(String.format("IRme in filename: %s and counter is: %d, %s", "AST_VAR_SUBSCRIPT", 6, "finished IR subscript"));

		IR.getInstance().Add_IRcommand(new IRcommand_Array_Set(t1, t2, texp));
		// no need to return anything because we assign it - and finish with store
		return null;
	}

	public TEMP_LIST computeArrayOffsets() {
		TEMP t2 = subscript.IRme();
		TEMP_LIST tlist = var.computeArrayOffsets();
		return new TEMP_LIST(t2, tlist);
	}

	public TEMP storeExp(TEMP_LIST exps, TEMP assigned) {
		if (exps == null) {
			System.out.println("It's an error!");
			System.exit(1);
		}
		TEMP t1 = var.getVarTemp(exps.tail);
		TEMP t2 = exps.head;
		IR.getInstance().Add_IRcommand(new IRcommand_Array_Set(t1, t2, assigned));
		return null;
	}

	public TEMP getVarTemp(TEMP_LIST exps) {
		if (exps == null) {
			System.out.println("It's an error!");
			System.exit(1);
		}
		TEMP dest = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP t1 = var.getVarTemp(exps.tail);
		TEMP t2 = exps.head;
		IR.getInstance().Add_IRcommand(new IRcommand_Array_Access(dest, t1, t2));
		return dest;
	}


}
