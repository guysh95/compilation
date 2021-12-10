package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_FUNCDEC extends AST_DEC
{
	public AST_TYPE type;
    public String id;
    public AST_FUNCARGS fa;
    public AST_STMT_LIST sl;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_FUNCDEC(AST_TYPE type, String id, AST_FUNCARGS fa, AST_STMT_LIST sl)
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
	//TODO add semant me
	//look at funcargs
}
