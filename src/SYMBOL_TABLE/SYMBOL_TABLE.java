/***********/
/* PACKAGE */
/***********/
package SYMBOL_TABLE;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TYPES.*;
import ANNOTATE_TABLE.*;

/****************/
/* SYMBOL TABLE */
/****************/
public class SYMBOL_TABLE
{
	private int hashArraySize = 13;
	
	/**********************************************/
	/* The actual symbol table data structure ... */
	/**********************************************/
	private SYMBOL_TABLE_ENTRY[] table = new SYMBOL_TABLE_ENTRY[hashArraySize];
	private SYMBOL_TABLE_ENTRY top;
	private int top_index = 0;
	private int scopeLayer = 0;
	public boolean inFuncScope = false;
	public boolean inClassScope = false;
	
	/**************************************************************/
	/* A very primitive hash function for exposition purposes ... */
	/**************************************************************/
	private int hash(String s)
	{
		if (s.charAt(0) == 'l') {return 1;}
		if (s.charAt(0) == 'm') {return 1;}
		if (s.charAt(0) == 'r') {return 3;}
		if (s.charAt(0) == 'i') {return 6;}
		if (s.charAt(0) == 'd') {return 6;}
		if (s.charAt(0) == 'k') {return 6;}
		if (s.charAt(0) == 'f') {return 6;}
		if (s.charAt(0) == 'S') {return 6;}
		return 12;
	}

	/****************************************************************************/
	/* Enter a variable, function, class type or array type to the symbol table */
	/****************************************************************************/
	public void enter(String name,TYPE t)
	{
		/*************************************************/
		/* [1] Compute the hash value for this new entry */
		/*************************************************/
		int hashValue = hash(name);

		/******************************************************************************/
		/* [2] Extract what will eventually be the next entry in the hashed position  */
		/*     NOTE: this entry can very well be null, but the behaviour is identical */
		/******************************************************************************/
		SYMBOL_TABLE_ENTRY next = table[hashValue];
	
		/**************************************************************************/
		/* [3] Prepare a new symbol table entry with name, type, next and prevtop */
		/**************************************************************************/
		SYMBOL_TABLE_ENTRY e = new SYMBOL_TABLE_ENTRY(name,t,hashValue,next,top,null,top_index++);
		/**********************************************/
		/* [4] Update the top of the symbol table ... */
		/**********************************************/
		top = e;
		
		/****************************************/
		/* [5] Enter the new entry to the table */
		/****************************************/
		table[hashValue] = e;
		
		/**************************/
		/* [6] Print Symbol Table */
		/**************************/
		PrintMe();
	}

	public void enterParam(String name, TYPE t, int paramIndex, String funcName){
		this.enter(name, t);
		this.top.paramIndex = new Integer(paramIndex);
		this.top.info = new AnnotAst(paramIndex, funcName, null);
		this.top.info.setParam();
		// paramIndex is null -> entry is not param
		// paramIndex is not null -> entry is param and it offset in the function is paramIndex
	}

	public void enterVar(String name, TYPE t, AnnotAst info)
	{
		this.enter(name, t);
		this.top.info = info;
	}

	public boolean isGlobalScope() {
		return (scopeLayer == 0);
	}

	/***********************************************/
	/* Find the inner-most scope element with name */
	/***********************************************/
	public TYPE find(String name)
	{
		SYMBOL_TABLE_ENTRY e;
				
		for (e = table[hash(name)]; e != null; e = e.next)
		{

			if (name.equals(e.name))
			{
				System.out.println("found!");
				return e.type;
			}
		}
		System.out.println("leaving found");
		return null;
	}

	public TYPE findInScope(String name)
	{
		SYMBOL_TABLE_ENTRY e;
		System.out.println("need to find: " + name);
		for (e = top; e != null; e = e.prevtop)
		{
			System.out.println("currently looking at: " + e.name);
			if(e.name.equals("SCOPE-BOUNDARY")){
				System.out.println("we return null");
				return null;
			}
			if (name.equals(e.name))
			{
				System.out.println("We found " + e.name + " in scope");
				return e.type;
			}
		}
		System.out.println("we return null");
		return null;
	}



	/***************************************************************************/
	/* begine scope = Enter the <SCOPE-BOUNDARY> element to the data structure */
	/***************************************************************************/
	public void beginScope(String scopeName, boolean isClass, TYPE_CLASS tc)
	{
		/************************************************************************/
		/* Though <SCOPE-BOUNDARY> entries are present inside the symbol table, */
		/* they are not really types. In order to be ablt to debug print them,  */
		/* a special TYPE_FOR_SCOPE_BOUNDARIES was developed for them. This     */
		/* class only contain their type name which is the bottom sign: _|_     */
		/************************************************************************/
		if (scopeName != null) { //function or class boundary
			enter(
					"SCOPE-BOUNDARY",
					new TYPE_FOR_SCOPE_BOUNDARIES(scopeName, isClass, tc));
			if (isClass) {
				inClassScope = true;
			}
			else {
				inFuncScope = true;
			}
		}
		else {
			enter(
					"SCOPE-BOUNDARY",
					new TYPE_FOR_SCOPE_BOUNDARIES("NONE", false, tc));
		}
		scopeLayer++;
		/*********************************************/
		/* Print the symbol table after every change */
		/*********************************************/
		PrintMe();
	}

	/********************************************************************************/
	/* end scope = Keep popping elements out of the data structure,                 */
	/* from most recent element entered, until a <NEW-SCOPE> element is encountered */
	/********************************************************************************/
	public void endScope(boolean saveElements)
	{
		/**************************************************************************/
		/* Pop elements from the symbol table stack until a SCOPE-BOUNDARY is hit */		
		/**************************************************************************/
		while (top.name != "SCOPE-BOUNDARY")
		{
			table[top.index] = top.next;
			top_index = top_index-1;
			top = top.prevtop;
		}
		TYPE_FOR_SCOPE_BOUNDARIES type = (TYPE_FOR_SCOPE_BOUNDARIES)top.type;
		// check if we end scope of function or class...
		if(type.funcBound == true) {
			inFuncScope = false;
		}
		if(type.classBound == true) {
			inClassScope = false;
		}

		/**************************************/
		/* Pop the SCOPE-BOUNDARY sign itself */		
		/**************************************/
		table[top.index] = top.next;
		top_index = top_index-1;
		top = top.prevtop;
		scopeLayer--;
		/*********************************************/
		/* Print the symbol table after every change */		
		/*********************************************/
		PrintMe();
	}
	
	public static int n=0;
	
	public void PrintMe()
	{
		int i=0;
		int j=0;
		String dirname="./output/";
		String filename=String.format("SYMBOL_TABLE_%d_IN_GRAPHVIZ_DOT_FORMAT.txt",n++);

		try
		{
			/*******************************************/
			/* [1] Open Graphviz text file for writing */
			/*******************************************/
			PrintWriter fileWriter = new PrintWriter(dirname+filename);

			/*********************************/
			/* [2] Write Graphviz dot prolog */
			/*********************************/
			fileWriter.print("digraph structs {\n");
			fileWriter.print("rankdir = LR\n");
			fileWriter.print("node [shape=record];\n");

			/*******************************/
			/* [3] Write Hash Table Itself */
			/*******************************/
			fileWriter.print("hashTable [label=\"");
			for (i=0;i<hashArraySize-1;i++) { fileWriter.format("<f%d>\n%d\n|",i,i); }
			fileWriter.format("<f%d>\n%d\n\"];\n",hashArraySize-1,hashArraySize-1);
		
			/****************************************************************************/
			/* [4] Loop over hash table array and print all linked lists per array cell */
			/****************************************************************************/
			for (i=0;i<hashArraySize;i++)
			{
				if (table[i] != null)
				{
					/*****************************************************/
					/* [4a] Print hash table array[i] -> entry(i,0) edge */
					/*****************************************************/
					fileWriter.format("hashTable:f%d -> node_%d_0:f0;\n",i,i);
				}
				j=0;
				for (SYMBOL_TABLE_ENTRY it=table[i];it!=null;it=it.next)
				{
					/*******************************/
					/* [4b] Print entry(i,it) node */
					/*******************************/
					fileWriter.format("node_%d_%d ",i,j);
					fileWriter.format("[label=\"<f0>%s|<f1>%s|<f2>prevtop=%d|<f3>next\"];\n",
						it.name,
						it.type.name,
						it.prevtop_index);

					if (it.next != null)
					{
						/***************************************************/
						/* [4c] Print entry(i,it) -> entry(i,it.next) edge */
						/***************************************************/
						fileWriter.format(
							"node_%d_%d -> node_%d_%d [style=invis,weight=10];\n",
							i,j,i,j+1);
						fileWriter.format(
							"node_%d_%d:f3 -> node_%d_%d:f0;\n",
							i,j,i,j+1);
					}
					j++;
				}
			}
			fileWriter.print("}\n");
			fileWriter.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static SYMBOL_TABLE instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected SYMBOL_TABLE() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static SYMBOL_TABLE getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new SYMBOL_TABLE();

			/*****************************************/
			/* [1] Enter primitive types int, string */
			/*****************************************/
			instance.enter("int",   TYPE_INT.getInstance());
			instance.enter("string",TYPE_STRING.getInstance());
			instance.enter("void", TYPE_VOID.getInstance());

			/*************************************/
			/* [2] How should we handle void ??? */
			/*************************************/

			/***************************************/
			/* [3] Enter library function PrintInt */
			/***************************************/
			instance.enter("PrintInt", new TYPE_FUNCTION(TYPE_VOID.getInstance(), "PrintInt", new TYPE_LIST(TYPE_INT.getInstance(), null)));

			instance.enter("PrintString", new TYPE_FUNCTION(TYPE_VOID.getInstance(), "PrintString", new TYPE_LIST(TYPE_STRING.getInstance(), null)));

			instance.enter("PrintTrace", new TYPE_FUNCTION(TYPE_VOID.getInstance(), "PrintTrace", null));
			
		}
		return instance;
	}

	/**
	 *
	 * for vardec ast nodes to set their annotations
	 */
	public AnnotAst setAstAnnotations() {
		// check if global
		AnnotAst info = new AnnotAst();
		if (this.isGlobalScope()) {
			info.setGlobal();
		}
		else {
			SYMBOL_TABLE_ENTRY e;
			TYPE_FOR_SCOPE_BOUNDARIES t;
			int offset = 0;
			for (e = top; e != null; e = e.prevtop)
			{
				if(e.name.equals("SCOPE-BOUNDARY")){
					t = (TYPE_FOR_SCOPE_BOUNDARIES)e.type;
					if (t.name.equals("NONE")) continue;
					if (t.classBound) {
						TYPE_CLASS tc = t.scopeClassType;
						info.setField();
						info.setClassName(tc.name);
						if (tc.father != null) {
							// count here how many fields father have
							//counted :)
							offset += tc.father.countFieldWithAncs();
						}
					}
					if (t.funcBound) {
						info.setLocal();
						info.setFuncName(t.name);
					}
					break;
				}
				if(e.type instanceof TYPE_FUNCTION) continue;
				if(e.paramIndex != null) continue;
				offset++;
			}
			info.setOffset(offset);
		}
		return info;
	}

	public SYMBOL_TABLE_ENTRY findEntry(String name)
	{
		SYMBOL_TABLE_ENTRY e;

		for (e = table[hash(name)]; e != null; e = e.next)
		{

			if (name.equals(e.name))
			{
				System.out.println("found!");
				return e;
			}
		}
		System.out.println("leaving found");
		return null;
	}

	/**
	 *
	 * for accessing vars :)
	 */
	// this is done (?)
	public AnnotAst getVarAnnotations(String name, TYPE_CLASS tc){
		System.out.println("getVarAnnotations getting annotations for: " + name);
		SYMBOL_TABLE_ENTRY e = this.findEntry(name);
		if (inClassScope && inFuncScope) {
			/**  method call */
			if (e == null) {
				/** looking in ancestors fields
				 * (var isnt current class field)
				 * (var isnt a defined local/global)
				 */
				if (tc == null) {
					System.out.println("shouldn't be here: getVarAnnotations error C");
					System.exit(1);
				}
				int offset = tc.getOffsetForVar(name);
				if (offset == -1) {
					System.out.println("shouldn't be here: getVarAnnotations error B");
					System.exit(1); // shouldn't happen
				}
				AnnotAst res = new AnnotAst(offset, null, tc.name);
				System.out.println("getVarAnnotations A getting annotations for: " + name  + " offset " + offset);
				res.setField(); /** ancestor class field */
				return res;
			}
			else {
				AnnotAst InnerScopeInfo = e.info; // need to add annotations to table_entry
				TYPE t = e.type;
				if (InnerScopeInfo.isGlobal()) {
					/** var is global var but maybe also a parent class field */
					// need to look in father class
					if(tc == null) // not in super class case
						return InnerScopeInfo;
					else {
						// maybe in father class
						int offset = tc.getOffsetForVar(name);
						if (offset == -1) return InnerScopeInfo;
						System.out.println("getVarAnnotations B getting annotations for: " + name + " offset " + offset);
						AnnotAst res = new AnnotAst(offset, null, tc.name);
						res.setField();
						return res;
					}
				}
				else {
					/** var is local/global/param or current class field */
					System.out.println("getVarAnnotations C getting annotations for: " + name + " offset " + InnerScopeInfo.getOffset());
					return InnerScopeInfo;
				}

			}
		}
		else if (inFuncScope) {
			if (e == null) {
				System.out.println("shouldn't be here: getVarAnnotations error A");
				System.exit(1); // error variable is not defined
			}
			AnnotAst InnerScopeInfo = e.info;
			/** var is local/global/param */
			return InnerScopeInfo;
		}
		System.out.println("shouldn't be here: getVarAnnotations error");
		return null;
	}

	public boolean isInClassDec() {
		return (inClassScope && !inFuncScope);
	}
}
