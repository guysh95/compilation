/***********/
/* PACKAGE */
/***********/
package IR; import MIPS.*; import java.util.*;
import REG_ALLOC.*;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IR
{
	private IRcommand head=null;
	private IRcommandList tail=null;

	/******************/
	/* Add IR command */
	/******************/
	public void Add_IRcommand(IRcommand cmd)
	{
		if ((head == null) && (tail == null))
		{
			this.head = cmd;
		}
		else if ((head != null) && (tail == null))
		{
			this.tail = new IRcommandList(cmd,null);
		}
		else
		{
			IRcommandList it = tail;
			while ((it != null) && (it.tail != null))
			{
				it = it.tail;
			}
			it.tail = new IRcommandList(cmd,null);
		}

		// add creation of cfg node
		CFG.getInstance().addCFGNode(new CFG_node(cmd));
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		System.out.println(head);
		while(tail != null){
			System.out.println(tail.head);
			tail = tail.tail;
		}
		int count = 1;
		System.out.println(String.format("Debug file is: %s, counter: %d", "IR", count++));
		if (head != null) head.MIPSme();
		while (tail != null)
		{
			System.out.println(String.format("Debug file is: %s, counter: %d", "IR", count++));
			tail.head.MIPSme();
			tail = tail.tail;
		}
		System.out.println(String.format("Debug file is: %s, counter: %d", "IR", count++));
		MIPSGenerator.getInstance().mainStub();
		System.out.println(String.format("Debug file is: %s, counter: %d", "IR", count++));
	}

	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static IR instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected IR() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static IR getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new IR();
		}
		return instance;
	}
}
