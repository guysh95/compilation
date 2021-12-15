package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_FUNCDEC extends AST_DEC
{
	public AST_TYPE type;
    public String id;
    public AST_FUNCARGS fa;
    public AST_STMT_LIST sl;
	public int row;


	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_FUNCDEC(AST_TYPE type, String id, AST_FUNCARGS fa, AST_STMT_LIST sl, int row)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
        System.out.print("====================== funcDec -> type ID ( funcArgs ) { stmtList }\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
        this.id = id;
        this.fa = fa;
        this.sl = sl;
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
		System.out.print("AST NODE FUNC DEC\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (type != null) type.PrintMe();
        if (id != null) System.out.format("ID(\"%s\"",id);
        if (fa != null) fa.PrintMe();
        if (sl != null) sl.PrintMe();
        
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("%s FUNC\nDEC\n", id));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
        if (fa != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,fa.SerialNumber);
        if (sl != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,sl.SerialNumber);

	}

	public TYPE SemantMe(TYPE scope) {
		TYPE argType;
		TYPE t1 = null;
		TYPE returnType = null;
		TYPE_LIST type_list = null;

		/*******************/
		/* [0] return type */
		/*******************/


		returnType = SYMBOL_TABLE.getInstance().find(type.SemantMe(scope).name); //TODO might need to be "type.semantme(scope).name)" inside find call
		if (returnType == null)
		{
			System.out.format(">> ERROR [%d:%d] non existing return type %s\n",6,6,returnType);
			throw new lineException(Integer.toString(this.row));
			//System.exit(0);

		}

		if (SYMBOL_TABLE.getInstance().isGlobalScope()) {
			if (SYMBOL_TABLE.getInstance().find(id) != null){
				System.out.format(">> ERROR: %s global function name already exists\n", id);
				throw new lineException(Integer.toString(this.row));
				//System.exit(0);
			}
		} else {	// in class scope
			t1 = SYMBOL_TABLE.getInstance().findInScope(id);
			if(t1 != null){ // check if func declared already in class
				System.out.format(">> ERROR: %s function already declared in class\n", id);
				throw new lineException(Integer.toString(this.row));
				//System.exit(0);
			}
		}

		/****************************/
		/* [1] Begin Function Scope */
		/****************************/
		System.out.println("######### Semanting " + id + " ##########");
		SYMBOL_TABLE.getInstance().beginScope();

		/***************************/
		/* [2] Semant Input Params */
		/***************************/
		for (AST_FUNCARGS it = fa; it != null; it = it.fa)
		{
			System.out.println("AAAAAAAaaaaAAAAAAA");
			argType = it.type.SemantMe(scope);
			System.out.println("BBBBBBBBbbbbBBBBBB");
			if (argType == TYPE_VOID.getInstance()){
				System.out.println(">> ERROR: void param isn't defined");
				throw new lineException(Integer.toString(this.row));
			}
			// argument type exist in scope and is not void
			SYMBOL_TABLE.getInstance().enter(it.id, argType);
			type_list = new TYPE_LIST(argType, type_list);
			//TODO remember to check parameter list with the super overridden method
		}

		/*******************/
		/* [3] Semant Body */
		/*******************/
		if (scope != null ) {
			System.out.println("scope of function is " + scope.name);
		}
		TYPE_FUNCTION tfunc = new TYPE_FUNCTION(returnType,id,type_list);
		SYMBOL_TABLE.getInstance().enter(id,tfunc);
		sl.SemantFunctionMe(scope, returnType);

		/*****************/
		/* [4] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope(true);
		System.out.println("we ended the scope for " + id + " in AST_FUNCDEC");
		/***************************************************/
		/* [5] Enter the Function Type to the Symbol Table */
		/***************************************************/
		System.out.println("now we are creating " + id + " type and its arguments are: ");
		System.out.println("returnType: " + returnType + " id: " + id + " type_list: " + type_list);
		// TYPE_FUNCTION tfunc = new TYPE_FUNCTION(returnType,id,type_list);
		System.out.println("we created tfunc and its type is " + tfunc + " in AST_FUNCDEC");
		SYMBOL_TABLE.getInstance().enter(id,tfunc);

		/*********************************************************/
		/* [6] Return value is irrelevant for function declarations */
		/*********************************************************/
		return tfunc;

	}
	//TODO look at funcargs
	// cant be in the same scope
}
