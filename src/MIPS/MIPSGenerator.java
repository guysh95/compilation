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
		fileWriter.format("main:\n");
		fileWriter.format("\tjal user_main\n");
		fileWriter.format("\tli $a0, 10\n");
		fileWriter.format("\tli $v0, 11\n");
		fileWriter.print("\tsyscall\n");
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
		fileWriter.format("\tmove $a0,$t%d\n",idx);
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
		fileWriter.format("\tmove $a0,$t%d\n",idx);
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
		fileWriter.format("\tli $a0, 10\n");
		fileWriter.format("\tli $v0, 11\n");
		fileWriter.print("\tsyscall\n");
		fileWriter.format("\tli $v0,10\n");
		fileWriter.format("\tsyscall\n");
	}
	//public TEMP addressLocalVar(int serialLocalVarNum)
	//{
	//	TEMP t  = TEMP_FACTORY.getInstance().getFreshTEMP();
	//	int idx = t.getSerialNumber();
	//
	//	fileWriter.format("\taddi $t%d,$fp,%d\n",idx,-serialLocalVarNum*WORD_SIZE);
	//	
	//	return t;
	//}

	public void loadAddressToDest(String address, TEMP dst)
	{
		int dstidx = regColorTable[dst.getSerialNumber()];
		fileWriter.format("\tla $s4,%s\n", address);
		fileWriter.format("\tsw $s4,%d($t%d)\n", 0, dstidx);
	}

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
			fileWriter.format("\tsw $t%d,0($sp)\n", i);
		}
	}

	private void epilogueRestore() {
		for(int i = 0; i < 10; i++) {
			fileWriter.format("\tlw $t%d,%d($sp)\n", i, (-4) * (i + 1) );
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
		// tlist = reverseTempList(tlist);
		int counter = 0;
		int idxprm;
		// prepare function arguments
		for (TEMP_LIST t = tlist; t != null; t = t.tail) {
			System.out.println("MIPSme: callFunc in Loop round: " + counter);
			idxprm = regColorTable[t.head.getSerialNumber()];
			fileWriter.format("\tsubu $sp,$sp,4\n");
			fileWriter.format("\tsw $t%d,0($sp)\n",idxprm);
			counter++;
		}
		fileWriter.format("\tjal %s\n", funcName);
		fileWriter.format("\taddu $sp,$sp,%d\n", counter*4);
		if(dst != null) {
			int dstidx = regColorTable[dst.getSerialNumber()];
			fileWriter.format("\tmove $t%d,$v0\n", dstidx);
		}
	}

	public void callMethod(TEMP caller, int methodOffset, TEMP_LIST tlist, TEMP dst)
	{
		int counter = 0;
		int idxprm;
		for (TEMP_LIST t = tlist; t != null; t = t.tail) {
			idxprm = regColorTable[t.head.getSerialNumber()];
			fileWriter.format("\tsubu $sp,$sp,4\n");
			fileWriter.format("\tsw $t%d,0($sp)\n",idxprm);
			counter++;
		}
		int clridx = regColorTable[caller.getSerialNumber()];
		fileWriter.format("\tsubu $sp,$sp,4\n");
		fileWriter.format("\tsw $t%d,0($sp)\n",clridx);
		counter++;
		fileWriter.format("\tlw $s0,0($t%d)\n",clridx);
		fileWriter.format("\tlw $s1,%d($s0)\n",4*methodOffset);
		fileWriter.format("\tjalr $s1\n");
		fileWriter.format("\taddu $sp,$sp,%d\n", counter*4);
		if (dst != null) {
			int dstidx = regColorTable[dst.getSerialNumber()];
			fileWriter.format("\tmove $t%d,$v0\n", dstidx);
		}
	}

	public TEMP_LIST reverseTempList(TEMP_LIST tlist) {
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

	public  void allocateGlobalNIL(String var_name) {
		//TODO: maybe need to change
		// assuming NIL is 0 for now
		fileWriter.format(".data\n");
		fileWriter.format("global_%s: .word %d\n",var_name, 0);
	}

	public void allocateGlobalInt(String var_name, int value)
	{
		fileWriter.format(".data\n");
		fileWriter.format("global_%s: .word %d\n",var_name, value);
	}
	public void  allocateGlobalString(String var_name, String value)
	{
		String str_label = labelGenerator("str");
		fileWriter.format(".data\n");
		fileWriter.format("%s: .asciiz %s\n",str_label, value);
		fileWriter.format("global_%s: .word %s\n",var_name,str_label);
	}

	public void loadGlobal(TEMP dst, String label) {
		int idxdst = regColorTable[dst.getSerialNumber()];
		fileWriter.format("\tlw $t%d,%s\n",idxdst,label);
	}

	public void loadLocal(TEMP dst, int offset) {
		int idxdst = regColorTable[dst.getSerialNumber()];
		// assuming we store all temps ....
		offset = -1 * ( (offset * 4 ) + 40 );
		fileWriter.format("\tlw $t%d,%d($fp)\n",idxdst, offset);
	}

	public void loadParam(TEMP dst, int offset) {
		int idxdst = regColorTable[dst.getSerialNumber()];
		offset = (offset * 4 ) + 4; // additinoal 4 to skip return address
		fileWriter.format("\tlw $t%d,%d($fp)\n",idxdst, offset);
	}

	public void storeGlobal(TEMP src, String label) {
		int idxsrc = regColorTable[src.getSerialNumber()];
		fileWriter.format("\tsw $t%d,%s\n",idxsrc,label);
	}

	public void createGlobal(TEMP src, String label) {
		fileWriter.format(".data\n");
		int idxsrc = regColorTable[src.getSerialNumber()];
		fileWriter.format("\t%s: $t%d\n",label,idxsrc);
	}

	public void storeLocal(TEMP src, int offset) {
		int idxsrc = regColorTable[src.getSerialNumber()];
		// assuming we store all temps ....
		offset = -1 * ( (offset * 4 ) + 40 );
		fileWriter.format("\tsw $t%d,%d($fp)\n",idxsrc, offset);
	}

	public void storeParam(TEMP src, int offset) {
		int idxsrc = regColorTable[src.getSerialNumber()];
		offset = (offset * 4 ) + 4; // additinoal 4 to skip return address
		fileWriter.format("\tsw $t%d,%d($fp)\n",idxsrc, offset);
	}

	public void loadFieldMethod(TEMP dst, int offset) {
		int idxdst = regColorTable[dst.getSerialNumber()];
		// loading instance address
		// instance is first argument to method
		// we use $s0 register for the action
		fileWriter.format("\tlw $s0,8($fp)\n");
		/** instance construct:
		 // 0 --- vt
		 // 4 --- 1st field
		 // 8 --- 2nd field
		 // :
		 // 4n -- n-th field
		 */
		fileWriter.format("\tlw $t%d,%d($s0)\n",idxdst, 4 * offset);
	}

	public void storeFieldMethod(TEMP src, int offset) {
		int idxsrc = regColorTable[src.getSerialNumber()];
		// loading instance address
		// instance is first argument to method
		// we use $s0 register for the action
		fileWriter.format("\tlw $s0,8($fp)\n");
		/** instance construct:
		 // 0 --- vt
		 // 4 --- 1st field
		 // 8 --- 2nd field
		 // :
		 // 4n -- n-th field
		 */
		fileWriter.format("\tsw $t%d,%d($s0)\n",idxsrc, 4 * offset);
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
				location = location + String.format("+%d($t%d)",offset,idxsrc);
			}
			else if (index != null) {
				int offset = 4 * index.intValue();
				location = location + String.format("+%d",offset);
			}
			if (isLoad)
				fileWriter.format("\tlw $t%d,%s\n",idxdst,location);
			else
				fileWriter.format("\tsw $t%d,%s\n",idxdst,location);
		}
		else { // load from a given address to dst reg
			if(src == null || index == null) {
				System.out.println("Invalid call for MIPSme load function");
				System.exit(1);
			}
			int idxsrc = regColorTable[src.getSerialNumber()];
			int offset = 4 * index.intValue();
			if (isLoad)
				fileWriter.format("\tlw $t%d,%d($t%d)\n",idxdst,offset,idxsrc);
			else
				fileWriter.format("\tsw $t%d,%d($t%d)\n",idxdst,offset,idxsrc);
		}

	}
	/*public void store(String var_name,TEMP src)
	{
		int idxsrc=src.getSerialNumber();
		fileWriter.format("\tsw $t%d,global_%s\n",idxsrc,var_name);
	}*/
	public void liInt(TEMP t,int value)
	{
		int idx= regColorTable[t.getSerialNumber()];
		fileWriter.format("\tli $t%d,%d\n",idx,value);
	}
	public void liString(TEMP t,String value)
	{
		int idx= regColorTable[t.getSerialNumber()];
		//Maybe need to add asciiz for null terminator in end of string
		String str_label = labelGenerator("str");
		fileWriter.format(".data\n");
		fileWriter.format("%s: .asciiz %s\n",str_label, value);
		fileWriter.format(".text\n");
		fileWriter.format("\tla $t%d,%s\n",idx,str_label);
	}

	public void move(TEMP dst, TEMP src)
	{
		int idx1 = regColorTable[dst.getSerialNumber()];
		int idx2 = regColorTable[src.getSerialNumber()];
		fileWriter.format("\tmove $t%d, $t%d", idx1, idx2);
	}

	private void binopCheckup(int validx)
	{
		String end1 = labelGenerator("end");
		String end2 = labelGenerator("end");
		fileWriter.format("\tble $t%d,32767,%s\n",validx, end1);
		fileWriter.format("\tli $t%d,32767\n",validx);
		label(end1);
		fileWriter.format("\tbge $t%d,-32768,%s\n",validx, end2);
		fileWriter.format("\tli $t%d,-32768\n",validx);
		label(end2);
	}

	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		int i2 = regColorTable[oprnd2.getSerialNumber()];
		int dstidx = regColorTable[dst.getSerialNumber()];
		fileWriter.format("\tadd $t%d,$t%d,$t%d\n",dstidx,i1,i2);
		binopCheckup(dstidx);
	}


	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		int i2 = regColorTable[oprnd2.getSerialNumber()];
		int dstidx = regColorTable[dst.getSerialNumber()];
		fileWriter.format("\tmul $t%d,$t%d,$t%d\n",dstidx,i1,i2);
		binopCheckup(dstidx);
	}

	public void sub(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		int i2 = regColorTable[oprnd2.getSerialNumber()];
		int dstidx = regColorTable[dst.getSerialNumber()];
		fileWriter.format("\tsub $t%d,$t%d,$t%d\n",dstidx,i1,i2);
		binopCheckup(dstidx);
	}

	public void div(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		int i2 = regColorTable[oprnd2.getSerialNumber()];
		int dstidx = regColorTable[dst.getSerialNumber()];
		divisionByZeroCheck(i2);
		fileWriter.format("\tdiv $t%d,$t%d,$t%d\n",dstidx,i1,i2);
		binopCheckup(dstidx);
	}

	private void divisionByZeroCheck(int regI)
	{
		String end = labelGenerator("end");
		fileWriter.format("\tbne $t%d,$zero,%s\n",regI, end);
		printGlobalString("string_illegal_div_by_0");
		exitGracefully();
		label(end);
	}

	public void getArrayValue(TEMP arr, TEMP place, TEMP dst)
	{
		System.out.println("Debug ---> MIPS: getArrrayValue 1");
		arrayAccessBoundrayCheck(arr, place);
		int arrayLoc = regColorTable[arr.getSerialNumber()];
		int offset = regColorTable[place.getSerialNumber()];
		int dstidx = regColorTable[dst.getSerialNumber()];

		fileWriter.format("\tsll $s1,$t%d,2\n",offset);

		fileWriter.format("\tadd $s0,$s1,$t%d\n",arrayLoc);
		fileWriter.format("\tlw $t%d,0($s0)\n",dstidx);

	}

	public void putArrayValue(TEMP arr, TEMP place, TEMP value)
	{
		arrayAccessBoundrayCheck(arr, place);
		int arrayLoc = regColorTable[arr.getSerialNumber()];
		int offset = regColorTable[place.getSerialNumber()];
		int vlidx = regColorTable[value.getSerialNumber()];
		fileWriter.format("\tsll $s1,$t%d,2\n",offset);
		fileWriter.format("\tadd $s0,$s1,$t%d\n",arrayLoc);
		fileWriter.format("\tsw $t%d,0($s0)\n",vlidx);
	}

	private void arrayAccessBoundrayCheck(TEMP arr, TEMP place/*, TEMP check*/)
	{
		int arrayLoc = regColorTable[arr.getSerialNumber()];
		int indexVal = regColorTable[place.getSerialNumber()];
		String label1 = labelGenerator("not_error");
		String label2 = labelGenerator("not_error");
		// temp to hold size of array : -4($tarrayLoc)
		fileWriter.format("\tlw $s0,-4($t%d)\n",arrayLoc);
		// bge 0 lb1
		fileWriter.format("\tbge $t%d,$zero,%s\n",indexVal,label1);
		// print error and exit
		printGlobalString("string_access_violation");
		exitGracefully();
		// lb1 :
		label(label1);
		// blt size lb2
		fileWriter.format("\tblt $t%d,$s0,%s\n",indexVal,label2);
		// print error and exit
		printGlobalString("string_access_violation");
		exitGracefully();
		// lb2:
		label(label2);
		// continue...
	}

	public void pointerDereference(TEMP pointer)
	{
		System.out.println("pointerDeref --->> A pointer is " + pointer);
		int idx = regColorTable[pointer.getSerialNumber()];
		System.out.println("pointerDeref --->> B ");
		String conLabel = labelGenerator("not_error");
		System.out.println("pointerDeref --->> C ");
		fileWriter.format("\tbne $t%d,$zero,%s\n",idx,conLabel);
		System.out.println("pointerDeref --->> D ");
		printGlobalString("string_invalid_ptr_dref");
		exitGracefully();
		System.out.println("pointerDeref --->> E ");
		label(conLabel);
	}

	public void label(String inlabel)
	{

		if (inlabel.equals("user_main"))
		{
			fileWriter.format(".text\n");
			fileWriter.format("%s:\n",inlabel);
		}
		else
		{
			fileWriter.format("%s:\n",inlabel);
		}
	}

	public void funcLabel(String inlabel)
	{
		fileWriter.format(".text\n");
		fileWriter.format("%s:\n",inlabel);
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
		fileWriter.format("\tblt $t%d,$t%d,%s\n",i1,i2,label);
	}
	public void bge(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		int i2 = regColorTable[oprnd2.getSerialNumber()];
		fileWriter.format("\tbge $t%d,$t%d,%s\n",i1,i2,label);
	}
	public void ble(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		int i2 = regColorTable[oprnd2.getSerialNumber()];
		fileWriter.format("\tble $t%d,$t%d,%s\n",i1,i2,label);
	}
	public void bgt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		int i2 = regColorTable[oprnd2.getSerialNumber()];
		fileWriter.format("\tbgt $t%d,$t%d,%s\n",i1,i2,label);
	}
	public void bne(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		int i2 = regColorTable[oprnd2.getSerialNumber()];

		fileWriter.format("\tbne $t%d,$t%d,%s\n",i1,i2,label);
	}
	public void beq(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		int i2 = regColorTable[oprnd2.getSerialNumber()];

		fileWriter.format("\tbeq $t%d,$t%d,%s\n",i1,i2,label);
	}
	public void beqz(TEMP oprnd1,String label)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		fileWriter.format("\tbeq $t%d,$zero,%s\n",i1,label);
	}

	public void bnez(TEMP oprnd1, String label)
	{
		int i1 = regColorTable[oprnd1.getSerialNumber()];
		fileWriter.format("\tbne $t%d,$zero,%s\n",i1,label);
	}

	public void compareStrLoop(TEMP firstStr, TEMP secondStr, String eqLabel, String notEqLabel)
	{
		int s1 = regColorTable[firstStr.getSerialNumber()];
		int s2 = regColorTable[secondStr.getSerialNumber()];
		String loopLabel = labelGenerator("compareLoop");

		// start string compare loop
		label(loopLabel);

		// get first char from firstStr and secondStr
		fileWriter.format("\tlb $s1,0($t%d)\n", s1);
		fileWriter.format("\tlb $s2,0($t%d)\n", s2);

		// compare chars, if not equal jump to notEqLabel
		fileWriter.format("\tbne $s1,$s2,%s\n", notEqLabel);

		// check if s1 is the End Of String, if so jump to eqLabel
		fileWriter.format("\tbeq $s1,$zero,%s\n", eqLabel);

		// point to next char in both strings
		fileWriter.format("\taddi $t%d,$t%d,1\n", s1,s1);
		fileWriter.format("\taddi $t%d,$t%d,1\n", s2,s2);

		jump(loopLabel);
	}

	private void countMallocString(int s1, int s2, int dstidx)
	{
		String countFirst = labelGenerator("countFirst");
		String startSecond = labelGenerator("startSecond");
		String countSecond = labelGenerator("countSecond");
		String countFinish = labelGenerator("countFinish");


		fileWriter.format("\tmove $s0,$t%d\n", s1);
		// init counter

		fileWriter.format("\tmove $s6,$zero\n");
		// count len of first string
		label(countFirst);
		//load char
		fileWriter.format("\tlb $s3,0($s0)\n");
		//if char is zero termin go count second string
		fileWriter.format("\tbeq $s3,$zero,%s\n", startSecond);
		fileWriter.format("\taddi $s6,1\n");
		//move index
		fileWriter.format("\taddi $s0,$s0,1\n");
		jump(countFirst);

		label(startSecond);

		fileWriter.format("\tmove $s0,$t%d\n", s2);
		label(countSecond);
		fileWriter.format("\tlb $s3,0($s0)\n");

		fileWriter.format("\tbeq $s3,$zero,%s\n", countFinish);
		fileWriter.format("\taddi $s6,$s6,1\n");

		fileWriter.format("\taddi $s0,$s0,1\n");
		jump(countSecond);

		label(countFinish);

		//allocate space for destination
		fileWriter.format("\tmove $a0,$s6\n");
		fileWriter.format("\tli $v0,9\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tmove $t%d,$v0\n",dstidx);
	}

	public void addStrings(TEMP dst, TEMP firstStr, TEMP secondStr)
	{
		int s1 = regColorTable[firstStr.getSerialNumber()];
		int s2 = regColorTable[secondStr.getSerialNumber()];
		int dstidx = regColorTable[dst.getSerialNumber()];

		countMallocString(s1,s2,dstidx);

		String copyFirst = labelGenerator("copyFirst");
		String copySecond = labelGenerator("copySecond");
		String doneCopying = labelGenerator("doneCopying");

		// save string start address at s0
		fileWriter.format("\tmove $s0,$t%d\n", dstidx);

		// iterate on first string and add it to destination string
		label(copyFirst);

		// copy first char of first string to $s3 register
		fileWriter.format("\tlb $s3,0($t%d)\n", s1);

		// move to copy second string if char is zero (null byte)
		fileWriter.format("\tbeq $s3,$zero,%s\n", copySecond);

		// save char to destination string
		fileWriter.format("\tsb $s3,0($t%d)\n", dstidx);

		// point to next char in the string and dst string
		fileWriter.format("\taddi $t%d,$t%d,1\n", s1,s1);
		fileWriter.format("\taddi $t%d,$t%d,1\n", dstidx,dstidx);

		// jump to start of the copying loop
		jump(copyFirst);

		// iterate on second string and add it to destination string
		label(copySecond);

		// copy first char of second string to $s3 register
		fileWriter.format("\tlb $s3,0($t%d)\n", s2);

		// move to done copying if char is zero (null byte)
		fileWriter.format("\tbeq $s3,$zero,%s\n", doneCopying);

		// save char to destination string
		fileWriter.format("\tsb $s3,0($t%d)\n", dstidx);

		// point to next char in the string and dst string
		fileWriter.format("\taddi $t%d,$t%d,1\n", s2,s2);
		fileWriter.format("\taddi $t%d,$t%d,1\n", dstidx,dstidx);

		// jump to start of the copying loop
		jump(copySecond);


		// When done copying:
		label(doneCopying);

		// add the null terminate string - zero
		fileWriter.format("\tsb $zero,0($t%d)\n", dstidx);
		// return dst temp to point at the start of the string
		fileWriter.format("\tmove $t%d,$s0\n", dstidx);
	}

	public void swByOffset(TEMP value, TEMP memory, int offset)
	{
		int validx = regColorTable[value.getSerialNumber()];
		int memidx = regColorTable[memory.getSerialNumber()];
		fileWriter.format("\tsw $t%d,%d($t%d)\n", validx, 4*offset, memidx);
	}

	public void lwByOffset(TEMP dest, TEMP memory, int offset)
	{
		int dstidx = regColorTable[dest.getSerialNumber()];
		int memidx = regColorTable[memory.getSerialNumber()];
		fileWriter.format("\tlw $t%d,%d($t%d)\n", dstidx, 4*offset, memidx);
	}

	public void initVTable(String className)
	{
		fileWriter.format(".data\n");
		label("vt_" + className);
	}

	public void enterMethodLabel(String classMethodName)
	{
		fileWriter.format("\t.word %s\n", classMethodName);
	}

	public void endDataSection()
	{
		fileWriter.format(".text\n");
	}

	public void mallocSpace(TEMP dest, int space)
	{
		int dstidx = regColorTable[dest.getSerialNumber()];

		// malloc space
		fileWriter.format("\tli $a0,%d\n",space);
		fileWriter.format("\tli $v0,9\n");
		fileWriter.print("\tsyscall\n");

		// store space address into dest
		fileWriter.format("\tmove $t%d,$v0\n",dstidx);
	}

	public void mallocArray(TEMP size, TEMP dest)
	{
		int i1 = regColorTable[size.getSerialNumber()];
		int destIndex = regColorTable[dest.getSerialNumber()];
		fileWriter.format("\taddi $t%d,$t%d, 1\n",i1, i1); // inc size by 1
		// mult by 4
		fileWriter.format("\tsll $t%d,$t%d, 2\n",i1, i1);
		// malloc (size + 1)*4 bytes
		fileWriter.format("\tmove $a0,$t%d\n",i1);
		fileWriter.format("\tli $v0,9\n");
		fileWriter.print("\tsyscall\n");

		fileWriter.format("\taddi $t%d,$t%d, -4\n",i1, i1); // dec byte sub by 4

		fileWriter.format("\tsw $t%d,0($v0)\n",i1); // store size to start of allocated memory

		fileWriter.format("\tmove $t%d,$v0\n",destIndex); // return to dest address of allocated memory

		// increment pointer to allocated memory by 4
		fileWriter.format("\taddi $t%d,$t%d,4\n",destIndex,destIndex);
		// memory allocated for array will be: [size, 0 ,1 , ... , n-1]
		/** pointer to array is pointing to 0 */
	}

	public void swFieldInt(TEMP dest, int offset, int value) {
		int destIndex = regColorTable[dest.getSerialNumber()];

		fileWriter.format("\tli $s0,%d\n", value);
		fileWriter.format("\tsw $s0,%d($t%d)\n",4*offset, destIndex);
	}

	public void swFieldString(TEMP dest, int offset, String value) {
		int destIndex = regColorTable[dest.getSerialNumber()];

		String str_label = labelGenerator("str");
		fileWriter.format(".data\n");
		fileWriter.format("%s: .asciiz %s\n",str_label, value);
		fileWriter.format(".text\n");
		fileWriter.format("\tla $s0,%s\n",str_label);
		fileWriter.format("\tsw $s0,%d($t%d)\n",4*offset, destIndex);
	}

	public void return1(TEMP t, String funcName)
	{
		if (t != null) {
			int i1 = regColorTable[t.getSerialNumber()];
			fileWriter.format("\tmove $v0,$t%d\n",i1);
		}
		if (funcName.equals("main"))
			funcName = "user_main";
		fileWriter.format("\tj %s_epilogue\n",funcName);
	}

//	public void mainStub()
//	{
//		fileWriter.format("main:\n");
//		fileWriter.format("\tjal user_main\n");
//		exitGracefully();
//	}
	
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
			instance.fileWriter.print(".text\n");
		}
		return instance;
	}
}
