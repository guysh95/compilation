package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;
import ANNOTATE_TABLE.*;

public class AST_STMT_ASSIGN extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_VAR var;
	public AST_EXP exp;
	public int row;
	private AST_VAR_SUBSCRIPT sub_var;

	public AST_STMT_ASSIGN(AST_VAR var,AST_EXP exp, int row)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();
		this.var = var;
		this.exp = exp;
		this.row = row;
	}

	public void PrintMe()
	{
		if (var != null) var.PrintMe();
		if (exp != null) exp.PrintMe();
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ASSIGN\nleft := right\n");
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

	public TYPE SemantMe(TYPE scope)
	{
		TYPE t1 = null;
		TYPE t2 = null;
		TYPE_CLASS tc1 = null;
		TYPE_CLASS tc2 = null;
		System.out.println("We are Semanting in AST_STMT_ASSIGN");
		if (var != null) t1 = var.SemantMe(scope);
		System.out.println("finished t1 in STMT_ASSIGN, result " + t1);
		if (exp != null) t2 = exp.SemantMe(scope);
		System.out.println("finished t2 in STMT_ASSIGN, result " + t2);

		if (t1 != t2) {
			if ((t1.isClass() || t1.isArray()) && t2 == TYPE_NIL.getInstance()) {
				System.out.println("Assigning nill to " + t1.name);
				return t2;
			}
			if(t1.isClass() && t2.isClass()){
				tc1 = (TYPE_CLASS) t1;
				tc2 = (TYPE_CLASS) t2;
				if(tc1.name.equals(tc2.name)){
					return t1;
				}
				for(TYPE_CLASS currFather = tc2.father; currFather != null; currFather = currFather.father){
					if (currFather.name.equals(tc1.name)){
						return t2;
					}
				}
			}
			System.out.format(">> ERROR19 [%d:%d] type mismatch for var := exp\n",6,6);
			throw new lineException(Integer.toString(this.row));
		}
		return t1;
	}

	public TEMP IRme(){
		System.out.println(String.format("IRme in filename: %s and counter is: %d", "AST_STMT_ASSIGN", 1));
		TEMP_LIST arrayOffsets = var.computeArrayOffsets();
		System.out.println(String.format("IRme in filename: %s and counter is: %d", "AST_STMT_ASSIGN", 2));
		TEMP t1 = exp.IRme();
		System.out.println(String.format("IRme in filename: %s and counter is: %d", "AST_STMT_ASSIGN", 3));
		var.storeExp(arrayOffsets, t1);
		return null;
	}

	public TEMP IRme_ver2(){
		System.out.println(String.format("IRme in filename: %s and counter is: %d", "AST_STMT_ASSIGN", 1));
		TEMP t1 = exp.IRme();
		System.out.println(String.format("IRme in filename: %s and counter is: %d", "AST_STMT_ASSIGN", 2));
		TEMP t2 = var.assignIRme(t1);
		System.out.println(String.format("IRme in filename: %s and counter is: %d", "AST_STMT_ASSIGN", 3));
		// nothing to return because we store the result (in assignIRme we set the result
		// to field, array or store it for reg var
		return null;
	}

}

