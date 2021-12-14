package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_EXPLIST extends AST_STMT
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_VAR var;
    public String id;
    public AST_EXPLIST exps;
	public int row;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_STMT_EXPLIST(AST_VAR var, String id, AST_EXPLIST exps, int row)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (var != null) {
			if (exps != null) System.out.print("====================== stmt -> var.id (exp list); \n");
			else System.out.print("====================== stmt -> var.id (); \n");
		}
		if (var == null){
			if (exps != null) System.out.print("====================== stmt -> id (exp list);\n");
			else System.out.print("====================== stmt -> id ();\n");
		}

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
        this.id = id;
        this.exps = exps;
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
		System.out.print("AST NODE STMT EXP LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (var != null) var.PrintMe();
        if (id != null) System.out.format("ID(\"%s\"",id);
        if (exps != null) exps.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("STMT\nEXPLIST\nVAR.(%s)", id));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
        if (exps != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exps.SerialNumber);
		
	}

	public TYPE SemantMe(String scope) {
		TYPE t1 = null;
		TYPE_CLASS tc = null;
		TYPE t2 = null;
		TYPE_LIST texps = null;

		// only if we have var
		// if var is not null this is a method call
		if (var != null){
			t1 = var.SemantMe(scope);
			if (t1.isClass() == false)
			{
				System.out.format(">> ERROR [%d:%d] access %s field of a non-class variable\n",6,6,id);
				throw new lineException(Integer.toString(this.row));
				//System.exit(0);
			}
			tc = (TYPE_CLASS) t1;

			// check that id in class scope
			for (TYPE_LIST it=tc.data_members;it != null;it=it.tail) {
				if (it.head.name.equals(id)) {
					t2 = it.head;
				}
			}
			if (t2 == null){
				System.out.format(">> ERROR no %s field on the scope\n",id);
				throw new lineException(Integer.toString(this.row));
				//System.exit(0);
			}

			if (! t2.getClass().getSimpleName().equals("TYPE_FUNCTION")) {
				System.out.format(">> ERROR provided explist although this is not a function\n");
				throw new lineException(Integer.toString(this.row));
				//System.exit(0);
			}
			TYPE_FUNCTION t3 = (TYPE_FUNCTION) t2;
			if (exps != null){
				texps = exps.getTypes(null);
				for (TYPE_LIST it=t3.params;it != null;it=it.tail) {
					if (texps.head == null){
						System.out.format(">> ERROR missing arguments for function\n");
						throw new lineException(Integer.toString(this.row));
					}
					checkArgument(it.head, texps.head);
					texps = texps.tail;
				}
				if (texps.head != null){
					System.out.format(">> ERROR to many parameters for function\n");
					throw new lineException(Integer.toString(this.row));
				}
			}



			return null;
		} else { // var is null
			// this is a function call
			// System.out.println("We are in stmt calling " + id);
			t2 = SYMBOL_TABLE.getInstance().find(id);
			// System.out.println(id + " is of type " + t2 + " in AST_STMT_EXPLIST");
			if (t2 == null) {
				System.out.format(">> ERROR no function named %s\n", id);
				throw new lineException(Integer.toString(this.row));
			}
			if(! t2.getClass().getSimpleName().equals("TYPE_FUNCTION")){
				System.out.format(">> ERROR provided explist although this is not a function");
				throw new lineException(Integer.toString(this.row));
			}
			System.out.println("We found " + id);
			TYPE_FUNCTION t3 = (TYPE_FUNCTION) t2;
			System.out.println("we converted " + t2 + " to " + t3);
			if (exps != null){
				texps = exps.getTypes(null);
				System.out.println("we got " + id + " arguments typle list");
				for (TYPE_LIST it=t3.params;it != null;it=it.tail) {
					System.out.println("1 argument is: " + texps.head.name);
					System.out.println("2 pointer to rest or arguments is " + texps.tail);
					if (texps.head == null){
						System.out.format(">> ERROR missing arguments for function\n");
						throw new lineException(Integer.toString(this.row));
					}
					checkArgument(it.head, texps.head);
					texps = texps.tail;
				}
				System.out.println("We finished the loop");
				if (texps != null){
					System.out.format(">> ERROR to many parameters for function\n");
					throw new lineException(Integer.toString(this.row));
				}
			}
			return null;
		}
	}

	public void checkArgument(TYPE param, TYPE arg) {
		if (param != arg) {
			boolean isOk = false;
			System.out.println("they are not equal");
			if (param.isArray() && arg == TYPE_NIL.getInstance())
				isOk = true;
			// check if they are class
			System.out.println(param.isClass() + " and param is " + param);
			System.out.println(arg.isClass() + " and arg is " + arg);
			if (param.isClass() && arg.isClass()) {
				System.out.println("We are here");
				if (arg == TYPE_NIL.getInstance()) {
					System.out.println("argument is nill");
					isOk = true;
				}
				else {
					TYPE_CLASS funcExpected = (TYPE_CLASS) param;
					TYPE_CLASS argDeliverd = (TYPE_CLASS) arg;
					for(TYPE_CLASS dadOfIns = argDeliverd.father; dadOfIns != null; dadOfIns = dadOfIns.father){
						if (dadOfIns == funcExpected){
							System.out.println("argument is subclass of paramter");
							isOk = true;
							break;
						}
					}
				}
			}
			System.out.println("isOk is " + isOk);
			if (!isOk) {
				System.out.println("isOk is " + isOk);
				System.out.format(">> ERROR parameters for function are not equal\n");
				throw new lineException(Integer.toString(this.row));
			}
		}
	}
}
