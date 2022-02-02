package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;
import ANNOTATE_TABLE.*;

public class AST_VARDEC_ASSIGN extends AST_DEC
{
	public AST_TYPE type;
    public String name;
    public AST_EXP exp;
	public int row;
	public AnnotAst info;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VARDEC_ASSIGN(AST_TYPE type, String name, AST_EXP exp, int row)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (exp != null) System.out.print("====================== varDec -> type ID ASSIGN exp\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.type = type;
        this.name = name;
        this.exp = exp;
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
		System.out.print("AST NODE DEC ASSIGN EXP\n");

		/*****************************/
		/* RECURSIVELY PRINT id ... */
		/*****************************/
		if (type != null) type.PrintMe();
		System.out.format("VAR ID( %s )\n", name);
        if (exp != null) exp.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("ASSIGN\nID(%s) := right\n", name));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
        if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
			
	}

	public TYPE SemantMe(TYPE scope) {
		TYPE t1 = null;
		TYPE t2 = null;

		/**************************************/
		/* [2] Check That Name does NOT exist */
		/**************************************/
		if (SYMBOL_TABLE.getInstance().findInScope(name) != null) {
			System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n",2,2,name);
			throw new lineException(Integer.toString(this.row));
			//System.exit(0);
		}

		if (type != null) t1 = type.SemantMe(scope);
		if (exp != null) t2 = exp.SemantMe(scope);
		if (scope != null){ // we are in class
			try{
				AST_EXP_INT check1 = (AST_EXP_INT) exp;
			} catch (Exception e1){
				try{
					AST_EXP_STRING check2 = (AST_EXP_STRING) exp;
				} catch (Exception e2) {
					try {
						AST_EXP_NIL check3 = (AST_EXP_NIL) exp;

					} catch (Exception e3) {
						System.out.format(">> ERROR cannot assign to var something else then string, int or nil in class");
						throw new lineException(Integer.toString(this.row));
					}
				}
			}
		}
		if (t1 != t2) {
			if (!((t1.isClass() || t1.isArray()) && t2 == TYPE_NIL.getInstance())) {
				System.out.format(">> ERROR6 [%d:%d] type mismatch for var := exp\n",6,6);
				throw new lineException(Integer.toString(this.row));
				//System.exit(0);
			}
		}
		//Annotations
		this.info = SYMBOL_TABLE.getInstance().setAstAnnotations();

		SYMBOL_TABLE.getInstance().enterVar(name, t1, this.info);
		TYPE_CLASS_VAR_DEC t3 = new TYPE_CLASS_VAR_DEC(t1, name);
		return t3;

	}

	public TEMP IRme(){

		TEMP t = exp.IRme();
		IR.getInstance().Add_IRcommand(new IRcommand_Store(name, t, info));
		// storing result
		return null;
	}

	public void AnnotateMe(){
		//TODO: add check if global scope or class scope
		ANNOTATE_TABLE.getInstance().enter(name);
		exp.AnnotateMe()
	}
}
