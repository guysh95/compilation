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

public class MIPSGenerator
{
	private int WORD_SIZE=4;
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

	static label_counter = 0;

	public static String labelGenerator(String msg)
	{
		return String.format("ourLabel_%d_%s",label_counter++,msg);
	}

	// maybe need to consider option of argument being a constant
	public void print_int(TEMP t)
	{
		int idx=t.getSerialNumber();
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
		int idx=t.getSerialNumber();
		fileWriter.format("\tmove $a0,Temp_%d\n",idx);
		fileWriter.format("\tli $v0,4\n");
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

	public void callFunc(String funcName, TEMP_LIST tlist) {
		tlist = reverseTempList(tlist);
		int counter = 0;
		int idxprm;
		// prepare function arguments
		for (TEMP_LIST t = tlist; t != null; t = tlist.tail) {
			idxprm = tlist.head.getSerialNumber();
			fileWriter.format("\tsubu $sp,$sp,4\n");
			fileWriter.format("\tsw Temp_%d,0($sp)\n",idxprm);
			counter++;
		}
		fileWriter.format("\tjal %s\n", funcName);
		fileWriter.format("\taddu $sp,$sp,%d\n", counter*4);
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
	public void allocateGlobalInt(String var_name, int value)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\tglobal_%s: .word %d\n",var_name, value);
	}
	public void loadGlobal(TEMP dst,String var_name)
	{
		//TODO add all cases of write commands as shown in tirgul 10 page 7
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tlw Temp_%d,global_%s\n",idxdst,var_name);
	}
	public void store(String var_name,TEMP src)
	{
		//TODO add all cases of load commands as shown in tirgul 10 page 8
		int idxsrc=src.getSerialNumber();
		fileWriter.format("\tsw Temp_%d,global_%s\n",idxsrc,var_name);		
	}
	public void liInt(TEMP t,int value)
	{
		int idx=t.getSerialNumber();
		fileWriter.format("\tli Temp_%d,%d\n",idx,value);
	}
	public void liString(TEMP t,String value)
	{
		int idx=t.getSerialNumber();
		//Maybe need to add asciiz for null terminator in end of string
		fileWriter.format("\tli Temp_%d,%s\n",idx,value);
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

	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2, Integer cnst1, Integer cnst2)
	{
		// Assumming that when both are constant we optimize it in MIPSme()
		int val1;
		int val2;
		int i1;
		int i2;
		int dstidx = dst.getSerialNumber();
		if ((cnst1 == null && oprnd1 == null) || (cnst2 == null && oprnd2 == null)) {
			System.out.println("Error in MIPS add function");
			exit(1);
		}
		if (oprnd1 == null) {
			// first oprnd is constant: 3 + x
			val1 = cnst1.intValue();
			i2 = oprnd2.getSerialNumber();
			fileWriter.format("\tadd Temp_%d,%d,Temp_%d\n",dstidx,val1,i2);
		}
		else if (oprnd2 == null) {
			// second operand is constant: x + 3
			val2 = cnst1.intValue();
			i1 = oprnd2.getSerialNumber();
			fileWriter.format("\tadd Temp_%d,Temp_%d,%d\n",dstidx,i1,val2);
		}
		else {
			// none is constant: x + y
			i1 =oprnd1.getSerialNumber();
			i2 =oprnd2.getSerialNumber();
			fileWriter.format("\tadd Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
		}
		binopCheckup(dstidx);
	}


	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2, Integer cnst1, Integer cnst2)
	{
		// Assumming that when both are constant we optimize it in MIPSme()
		int val1;
		int val2;
		int i1;
		int i2;
		int dstidx = dst.getSerialNumber();
		if ((cnst1 == null && oprnd1 == null) || (cnst2 == null && oprnd2 == null)) {
			System.out.println("Error in MIPS mul function");
			exit(1);
		}
		if (oprnd1 == null) {
			// first oprnd is constant: 3 * x
			val1 = cnst1.intValue();
			i2 = oprnd2.getSerialNumber();
			fileWriter.format("\tmul Temp_%d,%d,Temp_%d\n",dstidx,val1,i2);
		}
		else if (oprnd2 == null) {
			// second operand is constant: x * 3
			val2 = cnst1.intValue();
			i1 = oprnd2.getSerialNumber();
			fileWriter.format("\tmul Temp_%d,Temp_%d,%d\n",dstidx,i1,val2);
		}
		else {
			// none is constant: x * y
			i1 =oprnd1.getSerialNumber();
			i2 =oprnd2.getSerialNumber();
			fileWriter.format("\tmul Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
		}
		binopCheckup(dstidx);
	}

	public void sub(TEMP dst,TEMP oprnd1,TEMP oprnd2, Integer cnst1, Integer cnst2)
	{
		// Assumming that when both are constant we optimize it in MIPSme()
		int val1;
		int val2;
		int i1;
		int i2;
		int dstidx = dst.getSerialNumber();
		if ((cnst1 == null && oprnd1 == null) || (cnst2 == null && oprnd2 == null)) {
			System.out.println("Error in MIPS sub function");
			exit(1);
		}
		if (oprnd1 == null) {
			// first oprnd is constant: 3 - x
			val1 = cnst1.intValue();
			i2 = oprnd2.getSerialNumber();
			fileWriter.format("\tsub Temp_%d,%d,Temp_%d\n",dstidx,val1,i2);
		}
		else if (oprnd2 == null) {
			// second operand is constant: x - 3
			val2 = cnst1.intValue();
			i1 = oprnd2.getSerialNumber();
			fileWriter.format("\tsub Temp_%d,Temp_%d,%d\n",dstidx,i1,val2);
		}
		else {
			// none is constant: x - y
			i1 =oprnd1.getSerialNumber();
			i2 =oprnd2.getSerialNumber();
			fileWriter.format("\tsub Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
		}
		binopCheckup(dstidx);
	}

	public void div(TEMP dst,TEMP oprnd1,TEMP oprnd2, Integer cnst1, Integer cnst2)
	{
		// Assumming that when both are constant we optimize it in MIPSme()
		int val1;
		int val2;
		int i1;
		int i2;
		int dstidx = dst.getSerialNumber();
		if (oprnd1 == null) {
			// first oprnd is constant: 3 / x
			val1 = cnst1.intValue();
			i2 = oprnd2.getSerialNumber();
			fileWriter.format("\tdiv Temp_%d,%d,Temp_%d\n",dstidx,val1,i2);
		}
		else if (oprnd2 == null) {
			// second operand is constant: x / 3
			val2 = cnst1.intValue();
			i1 = oprnd2.getSerialNumber();
			fileWriter.format("\tdiv Temp_%d,Temp_%d,%d\n",dstidx,i1,val2);
		}
		else {
			// none is constant: x / y
			i1 =oprnd1.getSerialNumber();
			i2 =oprnd2.getSerialNumber();
			fileWriter.format("\tdiv Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
		}
		binopCheckup(dstidx);
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
	//TODO for all branch function need to add option for oprnds to be constants
	//MIGHT need to replace bge with bgt
	public void blt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tblt Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void bge(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbge Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void bne(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbne Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void beq(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbeq Temp_%d,Temp_%d,%s\n",i1,i2,label);				
	}
	public void beqz(TEMP oprnd1,String label)
	{
		int i1 =oprnd1.getSerialNumber();
				
		fileWriter.format("\tbeq Temp_%d,$zero,%s\n",i1,label);				
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
			instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Illegal Division By Zero\"\n");
			instance.fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");
		}
		return instance;
	}
}
