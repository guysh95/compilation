package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*; import IR.*; import MIPS.*;
import ANNOTATE_TABLE.*;

public class AST_VAR_FIELD extends AST_VAR
{
	public AST_VAR var;
	public String fieldName;
	public int row;
	TYPE_CLASS fieldOwnerClass;

	public AST_VAR_FIELD(AST_VAR var,String fieldName, int row)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();
		this.var = var;
		this.fieldName = fieldName;
		this.row = row;
	}

	public void PrintMe()
	{
		if (var != null) var.PrintMe();
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FIELD\nVAR\n...->%s",fieldName));
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
	}

	public TYPE SemantMe(TYPE scope)
	{
		TYPE t = null;
		TYPE_CLASS tc = null;
		TYPE_CLASS_VAR_DEC tvar = null;
		TYPE_ARRAY tarr = null;
		System.out.println("We are Semanting in AST_VAR_FIELD BIBI");
		if (var != null) t = var.SemantMe(scope);
		if (t.isClass() == false)
		{
			System.out.format(">> ERROR [%d:%d] access %s field of a non-class variable\n",6,6,fieldName);
			throw new lineException(Integer.toString(this.row));
		}
		System.out.println(t.name + " is a class, we are in var field");
		tc = (TYPE_CLASS) t;
		fieldOwnerClass = tc;

		/* Look for fiedlName inside tc */
		for (TYPE_LIST it=tc.data_members;it != null;it=it.tail)
		{
			if(it.head.isVar()){
				tvar = (TYPE_CLASS_VAR_DEC) it.head;
				if (tvar.name.equals(fieldName))
				{
					return tvar.t;
				}
			}
			if(it.head.isArray()){
				tarr = (TYPE_ARRAY) it.head;
				if (tarr.name.equals(fieldName))
				{
					return tarr.member_type;
				}
			}
		}
		tarr = tc.searchInFathersArr(fieldName, this.row);
		if (tarr != null){
			return tarr.member_type;

		}
		tvar = tc.searchInFathersVar(fieldName, this.row);
		if(tvar != null){
			return tvar.t;
		}

		System.out.format(">> ERROR [%d:%d] field %s does not exist in class\n",6,6,fieldName);
		throw new lineException(Integer.toString(this.row));
	}

	public TEMP IRme()
	{
		System.out.println(String.format("IRme in filename: %s and counter is: %d, %s", "AST_VAR_FIELD", 1, "start IRme"));
		TEMP dest = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP t2 = var.IRme();
		System.out.println(String.format("IRme in filename: %s and counter is: %d, %s", "AST_VAR_FIELD", 2, "finished IR var"));
		int fieldOffset = fieldOwnerClass.getOffsetForVar(fieldName);
		System.out.println(String.format("IRme in filename: %s and fieldOffset is: %d, fieldName is: %s", "AST_VAR_FIELD", fieldOffset, fieldName));
		IR.getInstance().Add_IRcommand(new IRcommand_Field_Access(dest, t2, fieldOffset));
		return dest;
	}

	public TEMP assignIRme(TEMP texp){
		System.out.println(String.format("IRme in filename: %s and counter is: %d, %s", "AST_VAR_FIELD", 3, "start assignIRme"));
		TEMP t2 = var.IRme();
		System.out.println(String.format("IRme in filename: %s and counter is: %d, %s", "AST_VAR_FIELD", 4, "finished IR var"));
		int fieldOffset = fieldOwnerClass.getOffsetForVar(fieldName);
		System.out.println(String.format("assignIRme in filename: %s and fieldOffset is: %d, fieldName is: %s", "AST_VAR_FIELD", fieldOffset, fieldName));
		IR.getInstance().Add_IRcommand(new IRcommand_Field_Set(t2, fieldOffset, texp));
		// I think it does not return anything because we assign it - and finish with store
		return null;
	}

	public TEMP_LIST computeArrayOffsets() {
		TEMP_LIST tlist = var.computeArrayOffsets();
		return tlist;
	}

	public TEMP storeExp(TEMP_LIST exps, TEMP assigned) {
		TEMP t2 = var.getVarTemp(exps);
		int fieldOffset = fieldOwnerClass.getOffsetForVar(fieldName);
		IR.getInstance().Add_IRcommand(new IRcommand_Field_Set(t2, fieldOffset, assigned));
		return null;
	}

	public TEMP getVarTemp(TEMP_LIST exps) {
		TEMP dest = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP t2 = var.getVarTemp(exps);
		int fieldOffset = fieldOwnerClass.getOffsetForVar(fieldName);
		IR.getInstance().Add_IRcommand(new IRcommand_Field_Access(dest, t2, fieldOffset));
		return dest;
	}
}
