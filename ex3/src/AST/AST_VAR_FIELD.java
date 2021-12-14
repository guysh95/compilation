package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_FIELD extends AST_VAR
{
	public AST_VAR var;
	public String fieldName;
	public int row;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_FIELD(AST_VAR var,String fieldName, int row)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> var DOT ID( %s )\n",fieldName);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.fieldName = fieldName;
		this.row = row;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.print("AST NODE FIELD VAR\n");

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (var != null) var.PrintMe();
		System.out.format("FIELD NAME( %s )\n",fieldName);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FIELD\nVAR\n...->%s",fieldName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
	}

	public TYPE SemantMe(String scope)
	{
		TYPE t = null;
		TYPE_CLASS tc = null;
		TYPE_CLASS_VAR_DEC tvar = null;


		/******************************/
		/* [1] Recursively semant var */
		/******************************/
		if (var != null) t = var.SemantMe(scope);

		/*********************************/
		/* [2] Make sure type is a class */
		/*********************************/
		if (t.isClass() == false)
		{
			System.out.format(">> ERROR [%d:%d] access %s field of a non-class variable\n",6,6,fieldName);
			throw new lineException(Integer.toString(this.row));

		}
		else
		{
			System.out.println(t.name + " is a class, we are in var field");
			tc = (TYPE_CLASS) t;
		}

		/************************************/
		/* [3] Look for fiedlName inside tc */
		/************************************/
		for (TYPE_LIST it=tc.data_members;it != null;it=it.tail)
		{

			if(it.head.isVar()){
				tvar = (TYPE_CLASS_VAR_DEC) it.head;
				System.out.println("casted class var");
				System.out.println("checking the following data member from class:" +tvar.name);
				System.out.println("fieldname is: "+ fieldName);
				if (tvar.name.equals(fieldName))
				{
					System.out.println("found field in class " +tc.name);
					return tvar.t;
				}
			}
		}

		/*********************************************/
		/* [4] fieldName does not exist in class var */
		/*********************************************/
		System.out.format(">> ERROR [%d:%d] field %s does not exist in class\n",6,6,fieldName);
		throw new lineException(Integer.toString(this.row));
		//System.exit(0);
		//return null;
	}
}
