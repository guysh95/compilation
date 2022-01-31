/***********/
/* PACKAGE */
/***********/
package MIPS;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;
import REG_ALLOC.*;

public class MIPSGenerator
{
	private int WORD_SIZE=4;
	private int[] regColorTable;
	/***********************/
	/* The file writer ... */
	/***********************/
	private PrintWriter fileWriter;

	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalizeFile()
	{
		fileWriter.print("\tli $v0,10\n");
		fileWriter.print("\tsyscall\n");
		fileWriter.close();
	}

	static int label_counter = 0;

	public static String labelGenerator(String msg)
	{
		return String.format("ourLabel_%d_%s",label_counter++,msg);
	}

	// maybe need to consider option of argument being a constant
	public void print_int(TEMP t)
	{
		int idx = regColorTable[t.getSerialNumber()];
		fileWriter.format("\tmove $a0,Temp_%d\n",idx);
		fileWriter.format("\tli $v0,1\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tli $a0,32\n");
		fileWriter.format("\tli $v0,11\n");
		fileWriter.format("\tsyscall\n");
	}
	// maybe need to consider option of argument being a constant
	public void print_string(TEMP t)
	{
		int idx = regColorTable[t.getSerialNumber()];
		fileWriter.format("\tmove $a0,Temp_%d\n",idx);
		fileWriter.format("\tli $v0,4\n");
		fileWriter.format("\tsyscall\n");
	}

	public void printGlobalString(String label)
	{
		fileWriter.format("\tla $a0,%s\n",label);
		fileWriter.format("\tli $v0,4\n");
		fileWriter.format("\tsyscall\n");
	}

	public void exitGracefully()
	{
		fileWriter.format("\tli $v0,10\n");
		fileWriter.format("\tsyscall\n");
	}
	//public TEMP addressLocalVar(int serialLocalVarNum)
	//{
	//	TEMP t  = TEMP_FACTORY.getInstance().getFreshTEMP();
	//	int idx = t.getSerialNumber();
	//
	//	fileWriter.format("\taddi Temp_%d,$fp,%d\n",idx,-serialLocalVarNum*WORD_SIZE);
	//	
	//	return t;
	//}

	public void functionPrologue(int localCount)
	{
		int localSpace = localCount * 4;
		if (localSpace < 16) localSpace = 16;
		fileWriter.format("\tsubu $sp,$sp,4\n");
		fileWriter.format("\tsw $ra,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,4\n");
		fileWriter.format("\tsw $fp,0($sp)\n");
		fileWriter.format("\tmove $fp,$sp\n");
		prologueBackup();
		fileWriter.format("\tsub $sp,$sp,%d\n", localSpace);
	}

	private void prologueBackup() {
		for(int i = 0; i < 10; i++) {
			fileWriter.format("\tsubu $sp,$sp,4\n");
			fileWriter.format("\tsw Temp_%d,0($sp)\n", i);
		}
	}

	private void epilogueRestore() {
		for(int i = 0; i < 10; i++) {
			fileWriter.format("\tlw Temp_%d,%d($sp)\n", i, (-4) * (i + 1) );
		}
	}

	public void functionEpilogue() {
		fileWriter.format("\tmove $sp,$fp\n");
		epilogueRestore();
		fileWriter.format("\tlw $fp,0($sp)\n");
		fileWriter.format("\tlw $ra,4($sp)\n");
		fileWriter.format("\taddu $sp,$sp,8\n");
		fileWriter.format("\tjr $ra\n");
	}

	public void callFunc(String funcName, TEMP_LIST tlist, TEMP dst) {
		tlist = reverseTempList(tlist);
		int counter = 0;
		int idxprm;
		// prepare function arguments
		for (TEMP_LIST t = tlist; t != null; t = tlist.tail) {
			idxprm = regColorTable[tlist.head.getSerialNumber()];
			fileWriter.format("\tsubu $sp,$sp,4\n");
			fileWriter.format("\tsw Temp_%d,0($sp)\n",idxprm);
			counter++;
		}
		fileWriter.format("\tjal %s\n", funcName);
		fileWriter.format("\taddu $sp,$sp,%d\n", counter*4);
		if(dst != null) {
			int dstidx = regColorTable[dst.getSerialNumber()];
			fileWriter.format("\tmove Temp_%d,$v0\n", dstidx);
		}
	}

	public void reverseTempList(TEMP_LIST tlist) {
		TEMP_LIST prev = null;
		TEMP_LIST current = tlist;
		TEMP_LIST next = null;
		while(current != null) {
			next = current.tail;
			current.tail = prev;
			prev = current;
			current = next;
		}
		tlist = prev;
		return tlist;
	}

	public void allocateGlobalInt(String var_name, int value)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\tglobal_%s: .word %d\n",var_name, value);
	}
	public void allocateGlobalString(String var_name, String value)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\tglobal_%s: .asciiz %s\n",var_name, value);
	}
	public void MemoryAccess(boolean isLoad, TEMP dst,String gVarName, TEMP src, Integer index)
	{
		int idxdst = regColorTable[dst.getSerialNumber()];
		if (gVarName != null) { // load global variable to dst reg
			String location = String.format("global_%s",gVarName);
			// compute offset if given
			if (src != null) {
				int idxsrc = regColorTable[src.getSerialNumber()];
				int offset = 4 * index.intValue();
				location = location + String.format("+%d(Temp_%d)",offset,idxsrc);
			}
			else if (index != null) {
				int offset = 4 * index.intValue();
				location = location + String.format("+%d",offset)
			}
			if (isLoad)
				fileWriter.format("\tlw Temp_%d,%s\n",idxdst,location);
			else
				fileWriter.format("\tsw Temp_%d,%s\n",idxdst,location);
		}
		else { // load from a given address to dst reg
			if(src == null || index == null) {
				System.out.println("Invalid call for MIPSme load function");
				System.exit(1);
			}
			int idxsrc = regColorTable[src.getSerialNumber()];
			int offset = 4 * index.intValue();
			if (isLoad)
				fileWriter.format("\tlw Temp_%d,%d(Temp_%d)\n",idxdst,offset,idxsrc);
			else
				fileWriter.format("\tsw Temp_%d,%d(Temp_%d)\n",idxdst,offset,idxsrc);
		}

	}
	/*public void store(String var_name,TEMP src)
	{
		//TODO add all cases of store commands as shown in tirgul 10 page 8
		int idxsrc=src.getSerialNumber();
		fileWriter.format("\tsw Temp_%d,global_%s\n",idxsrc,var_name);		
	}*/
	public void liInt(TEMP t,int value)
	{
		int idx= regColorTable[t.getSerialNumber()];
		fileWriter.format("\tli Temp_%d,%d\n",idx,value);
	}
	public void liString(TEMP t,String value)
	{
		int idx= regColorTable[t.getSerialNumber()];
		//Maybe need to add asciiz for null terminator in end of string
		fileWriter.format("\tli Temp_%d,%s\n",idx,value);
	}

	public void move(TEMP dst, TEMP src)
	{
		int idx1 = regColorTable[dst.getSerialNumber()];
		int idx2 = regColorTable[src.getSerialNumber()];
		fileWriter.format("\tmove Temp_%d, Temp_%d", idx1, idx2);
	}

	private void binopCheckup(int validx)
	{
		String end1 = labelGenerator("end");
		String end2 = labelGenerator("end");
		fileWriter.format("\tble Temp_%d,max,%s\n",validx, end1);
		fileWriter.format("\tli Temp_%d,max\n",validx);
		label(end1);
		fileWriter.format("\tbge Temp_%d,min,%s\n",validx, end2);
		fileWriter.format("\tli Temp_%d,min\n",validx);
		label(end2);
	}

	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		int i2 = regColorTable[oprnd2.getSerialNumber()];
		int dstidx = regColorTable[dst.getSerialNumber()];
		fileWriter.format("\tadd Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
		binopCheckup(dstidx);
	}


	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		int i2 = regColorTable[oprnd2.getSerialNumber()];
		int dstidx = regColorTable[dst.getSerialNumber()];
		fileWriter.format("\tmul Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
		binopCheckup(dstidx);
	}

	public void sub(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		int i2 = regColorTable[oprnd2.getSerialNumber()];
		int dstidx = regColorTable[dst.getSerialNumber()];
		fileWriter.format("\tsub Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
		binopCheckup(dstidx);
	}

	public void div(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		int i2 = regColorTable[oprnd2.getSerialNumber()];
		int dstidx = regColorTable[dst.getSerialNumber()];
		divisionByZeroCheck(i2);
		fileWriter.format("\tdiv Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
		binopCheckup(dstidx);
	}

	private void divisionByZeroCheck(int regI)
	{
		String end = labelGenerator("end");
		fileWriter.format("\tbne Temp_%d,$zero,%s\n",regI, end);
		printGlobalString("string_illegal_div_by_0");
		exitGracefully();
		label(end);
	}

	public getArrayValue(TEMP arr, TEMP place, TEMP dst)
	{
		arrayAccessBoundrayCheck(arr, place);
		int arrayLoc = regColorTable[arr.getSerialNumber()];
		int offset = regColorTable[place.getSerialNumber()];
		int dstidx = regColorTable[dst.getSerialNumber()];
		fileWriter.format("\tlw Temp_%d,Temp_%d(Temp_%d)\n",dstidx,offset,arr);
	}

	public putArrayValue(TEMP arr, TEMP place, TEMP value)
	{
		arrayAccessBoundrayCheck(arr, place);
		int arrayLoc = regColorTable[arr.getSerialNumber()];
		int offset = regColorTable[place.getSerialNumber()];
		int vlidx = regColorTable[value.getSerialNumber()];
		fileWriter.format("\tsw Temp_%d,Temp_%d(Temp_%d)\n",vidx,offset,arr);
	}

	private void arrayAccessBoundrayCheck(TEMP arr, TEMP place/*, TEMP check*/)
	{
		int arrayLoc = regColorTable[arr.getSerialNumber()];
		int indexVal = regColorTable[place.getSerialNumber()];
		int chkidx = regColorTable[check.getSerialNumber()];
		String label1 = labelGenerator("not_error");
		String label2 = labelGenerator("not_error");
		// temp to hold size of array : -4(Temp_arrayLoc)
		fileWriter.format("\tlw $s0,-4(Temp_%d)\n",arrayLoc);
		// bge 0 lb1
		fileWriter.format("\tbge Temp_%d,$zero,%s\n",indexVal,label1);
		// print error and exit
		printGlobalString("string_access_violation");
		exitGracefully();
		// lb1 :
		label(label1);
		// blt size lb2
		fileWriter.format("\tblt Temp_%d,$s0,%s\n",indexVal,label2);
		// print error and exit
		printGlobalString("string_access_violation");
		exitGracefully();
		// lb2:
		label(label2);
		// continue...
	}

	public void pointerDereference(TEMP pointer)
	{
		int idx = regColorTable[pointer.getSerialNumber()];
		String conLabel = labelGenerator("not_error");
		fileWriter.format("\tbne Temp_%d,$zero,%s\n",conLabel);
		printGlobalString("string_invalid_ptr_dref");
		label(conLabel);
	}

	public void label(String inlabel)
	{
		if (inlabel.equals("main"))
		{
			fileWriter.format(".text\n");
			fileWriter.format("%s:\n",inlabel);
		}
		else
		{
			fileWriter.format("%s:\n",inlabel);
		}
	}
	public void jump(String inlabel)
	{
		fileWriter.format("\tj %s\n",inlabel);
	}
	//MIGHT need to replace bge with bgt
	public void blt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		int i2 = regColorTable[oprnd2.getSerialNumber()];
		fileWriter.format("\tblt Temp_%d,Temp_%d,%s\n",i1,i2,label);
	}
	public void bge(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		int i2 = regColorTable[oprnd2.getSerialNumber()];
		fileWriter.format("\tbge Temp_%d,Temp_%d,%s\n",i1,i2,label);
	}
	public void bgt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		int i2 = regColorTable[oprnd2.getSerialNumber()];
		fileWriter.format("\tbgt Temp_%d,Temp_%d,%s\n",i1,i2,label);
	}
	public void bne(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		int i2 = regColorTable[oprnd2.getSerialNumber()];

		fileWriter.format("\tbne Temp_%d,Temp_%d,%s\n",i1,i2,label);
	}
	public void beq(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		int i2 = regColorTable[oprnd2.getSerialNumber()];

		fileWriter.format("\tbeq Temp_%d,Temp_%d,%s\n",i1,i2,label);
	}
	public void beqz(TEMP oprnd1,String label)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];

		fileWriter.format("\tbeq Temp_%d,$zero,%s\n",i1,label);
	}


	public void mallocArray(TEMP size, TEMP dest)
	{
		int i1 = regColorTable[size.getSerialNumber()];
		int destIndex = regColorTable[dst.getSerialNumber()];
		fileWriter.format("\taddi Temp_%d,Temp_%d, 1\n",i1, i1); // inc size by 1
		// mult by 4
		fileWriter.format("\tsll Temp_%d,Temp_%d, 2\n",i1, i1);

		// malloc (size + 1)*4 bytes
		fileWriter.format("\tmove $a0,Temp_%d\n",i1);
		fileWriter.format("\tli $v0,9\n");
		fileWriter.print("\tsyscall\n");

		fileWriter.format("\taddi Temp_%d,Temp_%d, -4\n",i1, i1); // dec byte sub by 4

		fileWriter.format("\tsw Temp_%d,0($v0)\n",i1); // store size to start of allocated memory

		fileWriter.format("\tmove Temp_%d,$v0\n",destIndex); // return to dest address of allocated memory

		// increment pointer to allocated memory by 4
		fileWriter.format("\taddi Temp_%d,Temp_%d,4\n",destIndex,destIndex);
		// memory allocated for array will be: [size, 0 ,1 , ... , n-1]
		/** pointer to array is pointing to 0 */
	}


	public void return1(TEMP t, String funcName)
	{
		// Might need to change consider epilogue name for method/function
		int i1 = regColorTable[t.getSerialNumber()];
		fileWriter.format("\tmove $v0,Temp_%d\n",i1);
		fileWriter.format("\tj %s_epilogue\n",funcName);
	}
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static MIPSGenerator instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected MIPSGenerator() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static MIPSGenerator getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new MIPSGenerator();
			instance.regColorTable = IG.getInstance().coloring();

			try
			{
				/*********************************************************************************/
				/* [1] Open the MIPS text file and write data section with error message strings */
				/*********************************************************************************/
				String dirname="./output/";
				String filename=String.format("MIPS.txt");

				/***************************************/
				/* [2] Open MIPS text file for writing */
				/***************************************/
				instance.fileWriter = new PrintWriter(dirname+filename);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			/*****************************************************/
			/* [3] Print data section with error message strings */
			/*****************************************************/
			instance.fileWriter.print(".data\n");
			instance.fileWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
			instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Division By Zero\"\n");
			instance.fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");
		}
		return instance;
	}
}
