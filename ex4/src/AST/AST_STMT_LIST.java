package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;

public class AST_STMT_LIST extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_STMT head;
	public AST_STMT_LIST tail;
	public TYPE expectedReturnType;
	public int row;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_STMT_LIST(AST_STMT head,AST_STMT_LIST tail, int row)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (tail != null) System.out.print("====================== stmts -> stmt stmts\n");
		if (tail == null) System.out.print("====================== stmts -> stmt      \n");

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
		System.out.print("AST NODE STMT LIST\n");

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
			"STMT\nLIST\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
		if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tail.SerialNumber);
	}

	public TYPE SemantMe(TYPE scope)
	{
		if (head == null){
			System.out.println(">> ERROR function can't be empty ");
			throw new lineException(Integer.toString(this.row));
		}
		AST_STMT current;
		TYPE stmtType;
		// System.out.println("Semanting AST_STMT_LIST");
		// System.out.println("Expected return type is " + expectedReturnType);
		for(AST_STMT_LIST pointer = this; pointer != null; pointer = pointer.tail) {
			current = pointer.head;
			// check if current statement is a return statment
			// System.out.println("looking at statment of type " + current);
			if (current.getClass().getSimpleName().equals("AST_STMT_IF")) {
				((AST_STMT_IF)current).SemantBodyMe(scope, expectedReturnType);
				continue;
			}
			if (current.getClass().getSimpleName().equals("AST_STMT_WHILE")) {
				((AST_STMT_WHILE)current).SemantBodyMe(scope, expectedReturnType);
				continue;
			}
			if (current.getClass().getSimpleName().equals("AST_STMT_RETURN")) {
				stmtType = ((AST_STMT_RETURN)current).SemantReturnMe(scope, expectedReturnType);
				if (stmtType != expectedReturnType) {
					if (expectedReturnType.isArray() && (stmtType == TYPE_NIL.getInstance())){
						continue;
					}
					if ((stmtType.isClass()) && (expectedReturnType.isClass())) {
						// check if stmtType is nil
						if (stmtType == TYPE_NIL.getInstance())
							continue;
						// check if stmtType is a class that extends expectedReturnType
						TYPE_CLASS pointerClass = (TYPE_CLASS) expectedReturnType;
						TYPE_CLASS instanceClass = (TYPE_CLASS) stmtType;
						boolean isLegal = false;
						for(TYPE_CLASS dadOfIns = instanceClass.father; dadOfIns != null; dadOfIns = dadOfIns.father){
							if (dadOfIns == pointerClass){
								isLegal = true;
								break;
							}
						}
						if (isLegal)
							continue;
					}
					System.out.println(">> ERROR return type " + stmtType.name + " is not " + expectedReturnType.name);
					throw new lineException(Integer.toString(((AST_STMT_RETURN)current).row));
				}
			}
			// current statment is not a return statment
			current.SemantMe(scope);
		}
		return null;
	}

	public void SemantFunctionMe(TYPE scope, TYPE returnType) {
		this.expectedReturnType = returnType;
		this.SemantMe(scope);
	}

	public TEMP IRme()
	{
		return null; //need to return temp list from here
	}

	public TEMP_LIST listIRme(){
		TEMP t1 = head.IRme();
		TEMP_LIST tlist = new TEMP_LIST(t1, null);

		for(AST_STMT_LIST curr = tail; curr != null; curr = curr.tail){
			t1 = curr.head.IRme();
			tlist = new TEMP_LIST(t1, tlist);
		}
		return tlist;
	}

	public void AnnotateMe() {
		head.AnnotateMe();
		if (tail != null) tail.AnnotateMe();
	}
	
}
