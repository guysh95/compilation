package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;
import ANNOTATE_TABLE.*;

public class AST_VAR_SIMPLE extends AST_VAR
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	public int row;
	public int offset;
	// positive offset - local
	// negative offset - param
	public AnnotAst nodeInfo;

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
		// t is null ====> name is not a defined in scope
		// t is not null ====> name is a local var in scope
		if (scope != null) System.out.println("scope is " + scope.name + " and its type is " + scope);
		if (t == null && scope != null) {
			// name is not a defined in current scope
			// search in class scope and superclasses scope
				System.out.println("scope is " + scope + " and its type is " + scope);
				if (scope.isClass()) {
					// we are in a method call
					// name is not defined in current boundary scope
					System.out.println("BLAH");
					TYPE_CLASS tc = (TYPE_CLASS) scope;
					TYPE myVarType = tc.findInClass(name);
					System.out.println("myVar is " + myVarType);
					if (myVarType == null) {
						// name is not current class field and not current boundary scope var
						// we are in a method call
						TYPE_CLASS_VAR_DEC fatherVar = tc.searchInFathersVar(name, row);
						if (fatherVar == null) {
							// name is not a class field and not current boundary scope var
							t = SYMBOL_TABLE.getInstance().find(name);
							if (t != null) {
								//name is global var or function local var or param
								nodeInfo = SYMBOL_TABLE.getInstance().getVarAnnotations(name, null);
								return t;
							}
							System.out.format(">> ERROR ID %s does not exists\n", name);
							throw new lineException(Integer.toString(this.row));
						}
						// name is superclass field
						nodeInfo = SYMBOL_TABLE.getInstance().getVarAnnotations(name, tc);
						return fatherVar.t;
					}
					else {
						// name is class field
						nodeInfo = SYMBOL_TABLE.getInstance().getVarAnnotations(name, null);
						return myVarType;
					}
			}
		}
		// name is or global or local func
		// we are not in a method
		if (t == null)	t = SYMBOL_TABLE.getInstance().find(name);
		if (t != null) {
			nodeInfo = SYMBOL_TABLE.getInstance().getVarAnnotations(name, null);
			return t;
		}
		// we are in global scope
		System.out.format(">> ERROR ID %s does not exists\n", name);
		throw new lineException(Integer.toString(this.row));
	}

	public TEMP IRme()
	{
		TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_Load(t,name));
		return t;
	}

	public TEMP assignIRme(TEMP texp){
		IR.getInstance().Add_IRcommand(new IRcommand_Store(name, texp));
		// I think it does not return anything because we assign it - and finish with store
		return null;
	}

	public void AnnotateMe(){
		this.offset = ANNOTATE_TABLE.getInstance().find(name);
		//TODO: check if offset is 0 -> maybe id is global\class field
	}
}
