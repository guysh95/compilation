package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CFIELD_LIST extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_CFIELD head;
	public AST_CFIELD_LIST tail;
	public TYPE_LIST allTypes = null;
	public int row;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_CFIELD_LIST(AST_CFIELD head,AST_CFIELD_LIST tail, int row)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (tail != null) System.out.print("====================== cfields -> cfield cfields\n");
		if (tail == null) System.out.print("====================== cfields -> cfield\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.head = head;
		this.tail = tail;
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
		System.out.print("AST CFIELD LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (head != null) head.PrintMe();
		if (tail != null) tail.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CFIELD\nLIST\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
		if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
	}

	public TYPE SemantMe(String scope) {
		TYPE t = null;
		if (head == null){
			System.out.print(">> ERROR in CFIELD_LIST semantme");
			throw new lineException(Integer.toString(this.row));
		}
		System.out.println("where are we in class declarations: 0");
		t = head.SemantMe(scope);
		System.out.println("head in cfield is " + t + " and its name is: " + t.name);
		allTypes = new TYPE_LIST(t, null);
		int i = 1;
		for(AST_CFIELD_LIST pointer = tail; pointer != null; pointer = pointer.tail){
			System.out.println("where are we in class declarations: " + i);
			i++;
			t = pointer.head.SemantMe(scope);
			System.out.println("pointer in cfield is " + t + " and its name is: " + t.name);
			allTypes = new TYPE_LIST(t, allTypes);
		}
		return null;
	}

	public TYPE_LIST getTypes(String scope) {
		this.SemantMe(scope);
		return allTypes;
	}

	public TYPE_LIST getTypesClass(TYPE_CLASS scope) {
		TYPE t = null;
		/* if (head == null){
			System.out.print(">> ERROR in CFIELD_LIST semantme");
			throw new lineException(Integer.toString(this.row));
		}
		System.out.println("where are we in class declarations: 0");
		t = head.SemantClassMe(scope);

		System.out.println("head in cfield is " + t + " and its name is: " + t.name);
		allTypes = new TYPE_LIST(t, null); */
		int i = 0;
		for(AST_CFIELD_LIST pointer = this; pointer != null; pointer = pointer.tail){
			System.out.println("where are we in class declarations: " + i);
			i++;
			t = pointer.head.SemantClassMe(scope);
			/*** t is TYPE that is actually TYPE_FUNCTION || t is TYPE that is actually TYPE_CLASS_VAR_DEC ***/
			System.out.println("pointer in cfield is " + t + " and its name is: " + t.name);
			allTypes = new TYPE_LIST(t, allTypes);
		}
		return allTypes;
	}

}
