   
import java.io.*;
import java.io.PrintWriter;
import java_cup.runtime.Symbol;
import AST.*;
import IR.*;
import MIPS.*;
import REG_ALLOC.*;


public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		Symbol s;
		AST_PROGRAM AST;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		
		try
		{
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			file_reader = new FileReader(inputFilename);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			file_writer = new PrintWriter(outputFilename);
			
			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(file_reader);
			
			/*******************************/
			/* [4] Initialize a new parser */
			/*******************************/
			p = new Parser(l);

			/***********************************/
			/* [5] 3 ... 2 ... 1 ... Parse !!! */
			/***********************************/
			AST = (AST_PROGRAM) p.parse().value;
			
			/*************************/
			/* [6] Print the AST ... */
			/*************************/
			AST.PrintMe();

			/**************************/
			/* [7] Semant the AST ... */
			/**************************/
			AST.SemantMe(null);

			/**********************/
			/* [8] IR the AST ... */
			/**********************/
			AST.IRme();

			/*******************************************/
			/* [8.5] Run register allocation on IR ... */
			/*******************************************/
			CFG.getInstance().runAnalysis();
			
			/***********************/
			/* [9] MIPS the IR ... */
			/***********************/
			IR.getInstance().MIPSme();

			/**************************************/
			/* [10] Finalize AST GRAPHIZ DOT file */
			/**************************************/
			AST_GRAPHVIZ.getInstance().finalizeFile();			

			/***************************/
			/* [11] Finalize MIPS file */
			/***************************/
			MIPSGenerator.getInstance().finalizeFile();			

			/**************************/
			/* [12] Close output file */
			/**************************/
			file_writer.close();
    	}

		catch (lineException e){
			try {

				file_writer = new PrintWriter(outputFilename);
				file_writer.print("ERROR(" + e.getMessage() + ")");
				System.out.println("Semantic Error");
				file_writer.close();
			}
			catch (Exception e2) {
				System.out.println("### we got exception! e2");
				e2.printStackTrace();
			}
		}
		catch (Exception e)
		{
			try {
				file_writer = new PrintWriter(outputFilename);
				if (e.getMessage().equals("lexical")) {
					file_writer.print("ERROR");
					System.out.println("Lexical Error");
				}
				else if (isNumeric(e.getMessage()))
				{
					file_writer.print("ERROR(" + e.getMessage() + ")");
					System.out.println("Syntax Error");
				}
				file_writer.close();
			}
			catch (Exception e2) {
				System.out.println("### we got exception! e2");
				e2.printStackTrace();
			}
		}



	}

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch(NumberFormatException e){
			return false;
		}

	}
}


