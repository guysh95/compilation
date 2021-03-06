package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;
import ANNOTATE_TABLE.*;

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

	public TYPE SemantMe(TYPE scope) {
		TYPE t1 = null;
		t1 = v.SemantMe(scope);

		return t1;

	}

	public TYPE SemantClassMe(TYPE_CLASS scope) {
		TYPE t1 = null;
		TYPE_FUNCTION tfunc = null;

		t1 = v.SemantMe(scope);
		tfunc = scope.searchInFathersFunc(t1.name, this.row);
		if(tfunc == null){ //no func with same name
			return t1;
		}
		System.out.println("returned from searchInFathersFunc with and checking if same func");

		//need to check same params and returntype
		if(!sameFunc((TYPE_FUNCTION) t1, tfunc)){
			System.out.print(">> ERROR in CFIELD_VAR issue with class scope");
			throw new lineException(Integer.toString(this.row));
		}

		return t1;
	}

	public boolean sameFunc(TYPE_FUNCTION declared, TYPE_FUNCTION returned){
		TYPE t1 = null;
		TYPE t2 = null;
		if(declared.returnType != returned.returnType){
			System.out.print(">> ERROR in samefunc - no same return type");
			return false;
		}
		TYPE_LIST paramsList = declared.params;
		for(TYPE_LIST it= returned.params; it != null; it=it.tail){
			if (paramsList == null) {
				System.out.print(">> ERROR in samefunc - no same amount of params");
				return false;
			}
			t1 = paramsList.head;
			t2 = it.head;
			if(t1 != t2){
				System.out.print(">> ERROR1 in samefunc - no same params types");
				return false;
			}
			paramsList = paramsList.tail;
		}
		if (paramsList != null) { //still more params
			return false;
		}
		//all checks passed
		return true;
	}

	public TEMP IRme(){
		System.out.println(String.format("IRme in filename: %s and counter is: %d", "AST_CFIELD_FUNC", 1));
		v.IRme();
		// stores the results
		return null;
	}

}
