package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;

public class AST_FUNCARGS extends AST_Node
{
	public AST_TYPE type;
    public String id;
    public AST_FUNCARGS fa;
	public int row;
	public TYPE_LIST allTypes;


	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_FUNCARGS(AST_TYPE type, String id, AST_FUNCARGS fa, int row)
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
	//TODO add semantme(scope)
	public TYPE SemantMe(TYPE scope) {
		TYPE t = null;
		TYPE t2 = null;
		System.out.println("AAA");
		t = type.SemantMe(scope);
		System.out.println("semanted type in funcargs" + t.name);
		if (t == TYPE_VOID.getInstance()){
			System.out.print("void param isn't defined");
			throw new lineException(Integer.toString(this.row));

		}
		SYMBOL_TABLE.getInstance().enter(id,t);

		allTypes = new TYPE_LIST(t, null);
		int i = 0;
		for(AST_FUNCARGS pointer = fa; pointer != null; pointer = pointer.fa){
			System.out.println("where are we in func arguments: " + i);
			i++;
			t = pointer.type.SemantMe(scope);
			if (t == TYPE_VOID.getInstance()){
				System.out.print("void param isn't defined");
				throw new lineException(Integer.toString(this.row));

			}
			allTypes = new TYPE_LIST(t, allTypes);
		}
		System.out.println("finished inspecting function arguments of: " + scope);
		return null;

	}

	public TYPE_LIST getTypes(TYPE scope) {
		this.SemantMe(scope);
		return allTypes;
	}

	public TEMP IRme()
	{
		return null; //not supposed to reach here
	}

}
