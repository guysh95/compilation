package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_SIMPLE extends AST_VAR
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	public int row;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SIMPLE(String name, int row)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> ID( %s )\n",name);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
		this.row = row;
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE SIMPLE VAR( %s )\n",name);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("SIMPLE\nVAR\n(%s)",name));
	}

	public TYPE SemantMe(TYPE scope)
	{
		TYPE t = null;
		// search in this scope
		t = SYMBOL_TABLE.getInstance().findInScope(name);
		if (scope != null) System.out.println("scope is " + scope.name + " and its type is " + scope);
		//we are inside scope
		if (t == null && scope != null) {
			// search in class scope and superclasses scope
				System.out.println("scope is " + scope + " and its type is " + scope);
				if (scope.isClass()) {
					System.out.println("BLAH");
					TYPE_CLASS tc = (TYPE_CLASS) scope;
					TYPE myVarType = tc.findInClass(name);
					System.out.println("myVar is " + myVarType);
					if (myVarType == null) {
						TYPE_CLASS_VAR_DEC fatherVar = tc.searchInFathersVar(name, row);
						if (fatherVar == null) {
							t = SYMBOL_TABLE.getInstance().find(name);
							if (t != null) return t;
							System.out.format(">> ERROR ID %s does not exists\n", name);
							throw new lineException(Integer.toString(this.row));
						}
						return fatherVar.t;
					}
					else {
						// found in class scope
						return myVarType;
					}
			}
		}
		if (t == null)	t = SYMBOL_TABLE.getInstance().find(name);
		if (t != null)
			return t;
		// we are in global scope
		System.out.format(">> ERROR ID %s does not exists\n", name);
		throw new lineException(Integer.toString(this.row));
	}
}
