package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_ASSIGN_NEW extends AST_STMT
{
	public AST_VAR var;
    public AST_NEW_EXP exp;
	public int row;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_STMT_ASSIGN_NEW(AST_VAR var, AST_NEW_EXP exp, int row)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> exp ASSIGN NEW exp\n");
        

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
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
		System.out.print("AST STMT ASSIGN NEW EXP\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
        if (var != null) var.PrintMe();
        if (exp != null) exp.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"STMT\nNEW EXP");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
        if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
        if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
			
	}

	public TYPE SemantMe(String scope)
	{
		TYPE t1 = null;
		TYPE t2 = null;

		if (var != null) t1 = var.SemantMe(scope);
		if (exp != null) t2 = exp.SemantMe(scope);
		System.out.println("now checking if " + t1 + " == " + t2 + " in AST_STMT_ASSIGN_NEW");
		System.out.println("t1 is of " + t1.name + " t2 is of " + t2.name);
		if(t1.isArray()){
			TYPE_ARRAY tarr1 = (TYPE_ARRAY) t1;
			TYPE_ARRAY tarr2 = (TYPE_ARRAY) t2;

			if(tarr1.member_type != tarr2.member_type){
				System.out.format(">> ERROR [%d:%d] type mismatch for var := exp\n",6,6);
				throw new lineException(Integer.toString(this.row));
			}
			System.out.println("they are equal peleg");
			return t1;
		}


		if (t1 != t2)
		{
			/* if (t1.isArray() && t2.isArray()) {
				System.out.println("t1 is array of " + ((TYPE_ARRAY)t1).member_type);
				System.out.println("t2 is array of " + ((TYPE_ARRAY)t2).member_type);
				if (t2.name.equals("array") && ((TYPE_ARRAY)t1).member_type == ((TYPE_ARRAY)t2).member_type)
					return t1;
			} */
			if (t1.isClass() && t2.isClass())  {
				System.out.println("we check if son extends father");
				TYPE_CLASS pointer = (TYPE_CLASS) t1;
				System.out.println("father converted");
				TYPE_CLASS instance = (TYPE_CLASS) t2;
				System.out.println("Son converted");
				//check if instance inherited from pointer or vice versa
				System.out.println(instance.father);
				for(TYPE_CLASS dadOfIns = instance.father; dadOfIns != null; dadOfIns = dadOfIns.father){
					System.out.println(dadOfIns);
					if (dadOfIns == pointer) {
						System.out.println("its ok guy");
						return t1;
					}
				}
			}
			System.out.format(">> ERROR [%d:%d] type mismatch for var := exp\n",6,6);
			throw new lineException(Integer.toString(this.row));
			//System.exit(0);

		}
		System.out.println("they are equal ido");
		return t1;
	}
}
