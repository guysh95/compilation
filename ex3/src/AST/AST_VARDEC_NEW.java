package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VARDEC_NEW extends AST_DEC
{
	public AST_TYPE type;
    public String id;
    public AST_NEW_EXP exp;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VARDEC_NEW(AST_TYPE type, String id, AST_NEW_EXP exp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== varDec -> type ID ASSIGN NEW exp\n");
        // father peleg := NEW son;

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
        this.id = id;
        this.exp = exp;

	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		System.out.print("AST NODE DEC NEW EXP\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (type != null) type.PrintMe();
        if (id != null) System.out.format("ID(\"%s\")",id);
        if (exp != null) exp.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("DEC\nNEW EXP (%s)", id));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
        if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

	public TYPE SemantMe() {

		TYPE t1 = null;
		TYPE t2 = null;

		/**************************************/
		/* [2] Check That id does NOT exist */
		/**************************************/
		if (SYMBOL_TABLE.getInstance().find(id) != null) {
			System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n",2,2,id);
			System.exit(0);
		}

		if (type != null) t1 = type.SemantMe();
		if (exp != null) t2 = exp.SemantMe();
		try {
			TYPE_CLASS texp = (TYPE_CLASS) t2;
			for(texp; texp != null; texp = texp.father){
				if (texp == t1) {
					SYMBOL_TABLE.getInstance().enter(id, t2)
					return null;
				}
			}
			System.out.format(">> ERROR no heritage for var decleration\n",6,6);
			System.exit(0);
		} catch {
			if (t1 != t2) {
				System.out.format(">> ERROR [%d:%d] type mismatch for var := exp\n",6,6);
				System.exit(0);
			}
		}

		SYMBOL_TABLE.getInstance().enter(id, t2)
		return null;
	}
}
