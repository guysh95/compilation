package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_FUNCARGS extends AST_Node
{
	public AST_TYPE type;
    public String id;
    public AST_FUNCARGS fa;
    

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_FUNCARGS(AST_TYPE type, String id, AST_FUNCARGS fa)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
        if (fa != null) System.out.print("====================== funcArgs -> type ID , funcArgs\n");
        if (fa == null) System.out.print("====================== funcArgs -> type ID\n");
		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
        this.id = id;
        this.fa = fa;
        

	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		System.out.print("AST NODE FUNC ARGS\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (type != null) type.PrintMe();
        if (id != null) System.out.format("ID(\"%s\"",id);
        if (fa != null) fa.PrintMe();
        
        
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FUNC\nARG\n( %s )\n", id));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
        if (fa != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,fa.SerialNumber);

	}
	//TODO add SemantMe()
	//TODO remember to check parameter list with the super overridden method
	//TODO decide if need to check here or in funcdec
	public TYPE SemantMe() {
		TYPE t = null;
		TYPE t2 = null;

		t = type.SemantMe()
		SYMBOL_TABLE.getInstance().enter(name,t);
		if(fa != null){
			t2 = fa.SemantMe();
			return t;
		}

		return null;

	}
}
