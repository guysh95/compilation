package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;
import ANNOTATE_TABLE.*;

public class AST_FUNCDEC extends AST_DEC
{
	public AST_TYPE type;
    public String id;
    public AST_FUNCARGS fa;
    public AST_STMT_LIST sl;
	public int row;
	public AnnotFunc info;
	TYPE_CLASS methodOwner = null;

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

		returnType = SYMBOL_TABLE.getInstance().find(type.SemantMe(scope).name);
		if (returnType == null)
		{
			System.out.format(">> ERROR [%d:%d] non existing return type %s\n",6,6,returnType);
			throw new lineException(Integer.toString(this.row));

		}

		if (SYMBOL_TABLE.getInstance().isGlobalScope()) {
			if (SYMBOL_TABLE.getInstance().find(id) != null){
				System.out.format(">> ERROR: %s global function name already exists\n", id);
				throw new lineException(Integer.toString(this.row));
			}
		} else {	// in class scope
			t1 = SYMBOL_TABLE.getInstance().findInScope(id);
			if(t1 != null){ // check if func declared already in class
				System.out.format(">> ERROR: %s function already declared in class\n", id);
				throw new lineException(Integer.toString(this.row));
			}
		}

		/****************************/
		/* [1] Begin Function Scope */
		/****************************/
		System.out.println("######### Semanting " + id + " ##########");
		if (scope != null && scope.isClass()) {
			methodOwner = (TYPE_CLASS)scope;
			TYPE_FUNCTION fatherFunc = ((TYPE_CLASS)scope).searchInFathersFunc(id, row);
			//System.out.println(id + " already exist and its returnType is " + fatherFunc);
			if (fatherFunc != null && returnType != fatherFunc.returnType) {
				System.out.println(">> ERROR: cant overload method with diffrent type");
				throw new lineException(Integer.toString(this.row));
			}
			else {
				TYPE_CLASS_VAR_DEC fatherVar = ((TYPE_CLASS)scope).searchInFathersVar(id, row);
				if (fatherVar != null) {
					System.out.println(">> ERROR: super class already has variable named " + id);
					throw new lineException(Integer.toString(this.row));
				}
			}
		}
		SYMBOL_TABLE.getInstance().beginScope(id, false, null);

		/***************************/
		/* [2] Semant Input Params */
		/***************************/
		int paramCount = 0;
		for (AST_FUNCARGS it = fa; it != null; it = it.fa)
		{
			//System.out.println("AAAAAAAaaaaAAAAAAA");
			argType = it.type.SemantMe(scope);
			//System.out.println("BBBBBBBBbbbbBBBBBB");
			if (argType == TYPE_VOID.getInstance()){
				System.out.println(">> ERROR: void param isn't defined");
				throw new lineException(Integer.toString(this.row));
			}
			// argument type exist in scope and is not void
			paramCount++;
			SYMBOL_TABLE.getInstance().enterParam(it.id, argType, paramCount, id);
			type_list = new TYPE_LIST(argType, type_list);
			//TODO remember to check parameter list with the super overridden method
		}
		info.setNumParams(paramCount);

		/*******************/
		/* [3] Semant Body */
		/*******************/
		TYPE_FUNCTION tfunc = new TYPE_FUNCTION(returnType,id,type_list);
		if (scope != null && scope.isClass()) {
			methodOwner = (TYPE_CLASS)scope;
			((TYPE_CLASS)scope).data_members = new TYPE_LIST(tfunc, ((TYPE_CLASS)scope).data_members);
			//System.out.println("scope of function is " + scope.name);
		}
		SYMBOL_TABLE.getInstance().enter(id,tfunc);
		//System.out.println("====> lets semant function body in AST_FUNCDEC");
		int[] localCount = {0};
		sl.SemantFunctionMe(scope, returnType, localCount, id);
		info.setNumLocals(localCount[0]);
		//System.out.println("====> We finished semanting body in AST_FUNCDEC");

		/*****************/
		/* [4] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope(true);
		// System.out.println("we ended the scope for " + id + " in AST_FUNCDEC");
		/***************************************************/
		/* [5] Enter the Function Type to the Symbol Table */
		/***************************************************/
		// System.out.println("now we are creating " + id + " type and its arguments are: ");
		// System.out.println("returnType: " + returnType + " id: " + id + " type_list: " + type_list);
		// System.out.println("we created tfunc and its type is " + tfunc + " in AST_FUNCDEC");
		SYMBOL_TABLE.getInstance().enter(id,tfunc);

		/*********************************************************/
		/* [6] Return value is irrelevant for function declarations */
		/*********************************************************/
		return tfunc;

	}

	public TEMP IRme()
	{
		//todo: consider case of method declaration!
		//todo: get local count to IR
		if (methodOwner == null) {
			IRcommand_Label_Function cmd = new IRcommand_Label(id);
			//CFG.setCFGInstance(cmd.func_cfg);
			IR.getInstance().Add_IRcommand(cmd);

			// no need to IRme func args - because we checked in semant me that
			// the declare and call have the same params - so when we use IRcommand_Call
			// we use the registers that provided there
			if (sl != null) sl.IRme();
			// label:
			IR.getInstance().Add_IRcommand(new IRcommand_Label(String.format("%s_epilogue", id)));
			//TODO: add here IR to print function epiloge for current function
		}
		else {
			String className = methodOwner.name;
			String methodLabel = className + "_" + id; // A_f
			IRcommand_Label_Function cmd = new IRcommand_Label(methodLabel);
			IR.getInstance().Add_IRcommand(cmd);
			if (sl != null) sl.IRme();
			IR.getInstance().Add_IRcommand(new IRcommand_Label(String.format("%s_epilogue", methodLabel)));
			//TODO: add here IR to print function epiloge for current function
		}
		IR.getInstance().Add_IRcommand(new IRcommand_Func_Epilogue());
		return null;
	}

	public void AnnotateMe()
	{
		ANNOTATE_TABLE.getInstance().startScope(id);
		ANNOTATE_TABLE.getInstance().addingParams = true;
		if (fa != null) fa.AnnotateMe();
		ANNOTATE_TABLE.getInstance().addingParams = false;
		if (sl != null) sl.AnnotateMe();
	}
}
