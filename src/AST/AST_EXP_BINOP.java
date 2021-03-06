package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;
import ANNOTATE_TABLE.*;

public class AST_EXP_BINOP extends AST_EXP
{
	// public AST_BINOP bOP;
	int bOP;
	public AST_EXP left;
	public AST_EXP right;
	public int row;
	public TYPE leftType = null;
	public TYPE rightType = null;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_BINOP(AST_EXP left,AST_EXP right,int bOP, int row)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();


		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> exp BINOP exp\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.left = left;
		this.right = right;
		this.bOP = bOP;
		this.row = row;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		String sOP = "";

		/*********************************/
		/* CONVERT OP to a printable sOP */
		/*********************************/
		if (bOP == 0) {sOP = "+";}
		if (bOP == 1) {sOP = "-";}
		if (bOP == 2) {sOP = "*";}
		if (bOP == 3) {sOP = "/";}
		if (bOP == 4) {sOP = "<";}
		if (bOP == 5) {sOP = ">";}
		if (bOP == 6) {sOP = "=";}
		
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.format("AST NODE BINOP(%s) EXP\n", sOP);

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (left != null) left.PrintMe();
		if (right != null) right.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("BINOP(%s)", sOP));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (left  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,left.SerialNumber);
		if (right != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,right.SerialNumber);
	}

	public TYPE SemantMe(TYPE scope) {
		TYPE t1 = null;
		TYPE t2 = null;

		if (left  != null) {
			t1 = left.SemantMe(scope);
			leftType = t1;
		}
		if (right != null) {
			t2 = right.SemantMe(scope);
			rightType = t2;
		}

		if ((t1 == TYPE_INT.getInstance()) && (t2 == TYPE_INT.getInstance()))
		{
			if (bOP == 3){

				if (right.getClass().getSimpleName().equals("AST_EXP_INT")){
					AST_EXP_INT num = (AST_EXP_INT) right;
					if (num.value == 0) {
						System.out.print("error division by zero\n");
						throw new lineException(Integer.toString(this.row));
						//System.exit(0);
					}

				}

			}
			return TYPE_INT.getInstance();
		}

		if ((bOP == 0) && (t1 == TYPE_STRING.getInstance()) && (t2 == TYPE_STRING.getInstance())) {

			return TYPE_STRING.getInstance();
		}
		if (bOP == 6) {
			if ((t1 == TYPE_VOID.getInstance()) && (t2 == TYPE_VOID.getInstance())) {
				if(right.getClass().getSimpleName().equals("AST_EXP_FCALL") && left.getClass().getSimpleName().equals("AST_EXP_FCALL"))
					return TYPE_INT.getInstance();
			}
			if ((t1 == TYPE_STRING.getInstance()) && (t2 == TYPE_STRING.getInstance())) {
				return TYPE_INT.getInstance();	// cond if string equals to another
			}
			if (t1.isClass() && t2.isClass()) {
				if (t1 == TYPE_NIL.getInstance() || t2 == TYPE_NIL.getInstance()) {
					return TYPE_INT.getInstance();
				}
				if (t1 == t2) {
					return TYPE_INT.getInstance();
				}
				TYPE_CLASS leftClass = (TYPE_CLASS) t1;
				TYPE_CLASS rightClass = (TYPE_CLASS) t2;
				//check if left inherited from right or vice versa
				for(TYPE_CLASS dadOfIns = leftClass.father; dadOfIns != null; dadOfIns = dadOfIns.father){
					if (dadOfIns == rightClass)
						return TYPE_INT.getInstance();
				}
				for(TYPE_CLASS dadOfIns = rightClass.father; dadOfIns != null; dadOfIns = dadOfIns.father){
					if (dadOfIns == leftClass)
						return TYPE_INT.getInstance();
				}
			}
			if (t1.isArray() && t2.isArray()) {
				if (t1 == TYPE_NIL.getInstance() || t2 == TYPE_NIL.getInstance()) {
					return TYPE_INT.getInstance();
				}
				if (t1 == t2) {
					return TYPE_INT.getInstance();
				}
			}
		}
		System.out.print("unmatching types or undeclared string operation\n");
		throw new lineException(Integer.toString(this.row));
	}

	public TEMP IRme()
	{
		System.out.println(String.format("IRme in filename: %s and counter is: %d", "AST_EXP_BINOP", 1));
		// we use semant me to get the type
		TYPE s1 = this.leftType;
		TYPE s2 = this.rightType;

		//if (left  != null) s1 = left.SemantMe(null);
		//if (right != null) s2 = right.SemantMe(null);

		TEMP t1 = null;
		TEMP t2 = null;
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();

		System.out.println(String.format("IRme in filename: %s and counter is: %d", "AST_EXP_BINOP", 2));
		if (left  != null) t1 = left.IRme();
		System.out.println(String.format("IRme in filename: %s and counter is: %d", "AST_EXP_BINOP", 3));
		if (right != null) t2 = right.IRme();
		System.out.println(String.format("IRme in filename: %s and counter is: %d and binop is: %d", "AST_EXP_BINOP", 4, bOP));


		if (bOP == 0) //added option for string
		{
			if((s1 == TYPE_INT.getInstance()) && (s2 == TYPE_INT.getInstance())){
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Integers(dst,t1,t2));
			} else {
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Strings(dst,t1,t2));
			}
		}
		if (bOP == 1)
		{
			IR.getInstance().Add_IRcommand(new IRcommand_Binop_Sub_Integers(dst,t1,t2));
		}
		if (bOP == 2)
		{
			IR.getInstance().Add_IRcommand(new IRcommand_Binop_Mul_Integers(dst,t1,t2));
		}
		if (bOP == 3)
		{
			IR.getInstance().Add_IRcommand(new IRcommand_Binop_Divide_Integers(dst,t1,t2));
		}
		if (bOP == 4)
		{
			IR.getInstance().Add_IRcommand(new IRcommand_Binop_LT_Integers(dst,t1,t2));
		}
		if (bOP == 5)
		{
			IR.getInstance().Add_IRcommand(new IRcommand_Binop_GT_Integers(dst,t1,t2));
		}
		if (bOP == 6)
		{
			if((s1 == TYPE_STRING.getInstance()) && (s2 == TYPE_STRING.getInstance())){
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_EQ_Strings(dst,t1,t2));
			} else {
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_EQ_Integers(dst,t1,t2));
			}
		}
		System.out.println(String.format("IRme in filename: %s and counter is: %d", "AST_EXP_BINOP", 5));
		return dst;
	}

}
