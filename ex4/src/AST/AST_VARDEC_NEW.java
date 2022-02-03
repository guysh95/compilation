package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;
import ANNOTATE_TABLE.*;

public class AST_VARDEC_NEW extends AST_DEC
{
	public AST_TYPE type;
    public String id;
    public AST_NEW_EXP exp;
	public int row;
	public AnnotAst info;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VARDEC_NEW(AST_TYPE type, String id, AST_NEW_EXP exp, int row)
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

	public TYPE SemantMe(TYPE scope) {
		TYPE t1 = null;
		TYPE t2 = null;
		System.out.println("%%%%%%%% semanting in AST_VARDEC_NEW");
		/**************************************/
		/* [2] Check That id does NOT exist */
		/**************************************/
		if(scope != null) { //not allowed to declare var in class with assignning something else then constant
			System.out.format(">> ERROR can assign only constant to var dec in class, " +id);
			throw new lineException(Integer.toString(this.row));
		}
		if (SYMBOL_TABLE.getInstance().findInScope(id) != null) {
			System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n",2,2,id);
			throw new lineException(Integer.toString(this.row));
			//System.exit(0);
		}

		if (type != null) t1 = type.SemantMe(scope);
		if (exp != null) t2 = exp.SemantMe(scope);
		System.out.println("%%%%%%%% finished semanting in AST_VARDEC_NEW");
		System.out.println("t1 is: " + t1 + ", t2 is: " + t2);
		try {
			for(TYPE_CLASS texp = (TYPE_CLASS) t2; texp != null; texp = texp.father){
				if (texp == t1) {
					SYMBOL_TABLE.getInstance().enter(id, t1);
					return null;
				}
			}
			System.out.format(">> ERROR no heritage for var decleration\n",6,6);
			throw new lineException(Integer.toString(this.row));
			//System.exit(0);
		} catch (Exception e){
			if (t1.isArray() && t2.isArray()){
				System.out.println("we are trying to assign array");
				TYPE t1_member = ((TYPE_ARRAY)t1).member_type;
				System.out.println(t1_member);
				TYPE t2_member = ((TYPE_ARRAY)t2).member_type;
				System.out.println(t2_member);
				if(t1_member != t2_member) {
					// Check if the two arrays hold class type (*)
					if (t1_member.isClass() && t2_member.isClass()) {
						// if (*) holds, check if t2 array's class type
						TYPE_CLASS pointerClass = (TYPE_CLASS) t1_member;
						TYPE_CLASS instanceClass = (TYPE_CLASS) t2_member;
						for (TYPE_CLASS dadOfIns = instanceClass.father; dadOfIns != null; dadOfIns = dadOfIns.father) {
							System.out.println("in dadofins");
							if (dadOfIns == pointerClass) {
								SYMBOL_TABLE.getInstance().enter(id, ((TYPE_ARRAY) t1));
								return null;
							}
						}
					}
					System.out.format(">> ERROR35 [%d:%d] type mismatch for var := exp\n", 6, 6);
					throw new lineException(Integer.toString(this.row));
				} /* else {
					TYPE_CLASS_VAR_DEC t3 = new TYPE_CLASS_VAR_DEC(t2, id);

					SYMBOL_TABLE.getInstance().enter(id, t2);
					return t3;
				}*/
			}
			else if (t1 != t2) {
				System.out.format(">> ERROR23 [%d:%d] type mismatch for var := exp\n",6,6);
				throw new lineException(Integer.toString(this.row));
			}
		}
		TYPE_CLASS_VAR_DEC t3 = new TYPE_CLASS_VAR_DEC(t1, id);

		this.info = SYMBOL_TABLE.getInstance().setAstAnnotations();

		SYMBOL_TABLE.getInstance().enterVar(id, t1, this.info);
		return t3;
	}

	public TEMP IRme(){

		TEMP t = exp.newIRme();
		IR.getInstance().Add_IRcommand(new IRcommand_Store(id, t, info));
		// storing result
		return null;
	}
}
