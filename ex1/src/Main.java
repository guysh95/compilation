
import java.io.*;
import java.io.PrintWriter;

import java_cup.runtime.Symbol;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Symbol s;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		String outputFilename = argv[1];

		String[] tokens = {"EOF", "PLUS", "MINUS", "TIMES", "DIVIDE", "LPAREN", "RPAREN", "INT", "ID", "LBRACK", "RBRACK",
							"LBRACE", "RBRACE", "NIL", "COMMA", "DOT", "SEMICOLON", "ASSIGN", "EQ", "LT", "GT", "ARRAY",
							"CLASS", "EXTENDS", "RETURN", "WHILE", "IF", "NEW", "STRING", "TYPE_INT", "TYPE_STRING", "COMMENT"};

		try {file_writer = new PrintWriter(outputFilename);}
		catch(Exception exc) {exc.printStackTrace();}
		try
		{
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			file_reader = new FileReader(inputFilename);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/



			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(file_reader);

			/***********************/
			/* [4] Read next token */
			/***********************/
			s = l.next_token();

			/********************************/
			/* [5] Main reading tokens loop */
			/********************************/

			while (s.sym != TokenNames.EOF)
			{
				/************************/
				/* [6] Print to console */
				/************************/
				System.out.print(tokens[s.sym]);
				if (s.value != null) 	System.out.print("(" + s.value + ")");
				System.out.print("[");
				System.out.print(l.getLine());
				System.out.print(",");
				System.out.print(l.getTokenStartPosition());
				System.out.print("]");
				System.out.print("\n");

				/*********************/
				/* [7] Print to file */
				/*********************/

				file_writer.print(tokens[s.sym]);
				if (s.value != null) 	System.out.print("(" + s.value + ")");
				file_writer.print("[");
				file_writer.print(l.getLine());
				file_writer.print(",");
				file_writer.print(l.getTokenStartPosition());
				file_writer.print("]");
				file_writer.print("\n");


				/***********************/
				/* [8] Read next token */
				/***********************/
				s = l.next_token();
			}

			/******************************/
			/* [9] Close lexer input file */
			/******************************/
			l.yyclose();

			/**************************/
			/* [10] Close output file */
			/**************************/
			file_writer.close();
    	}

		catch (Exception e)
		{
			try{
				System.out.println("print from catch");
				file_writer.print("ERROR");
				file_writer.close();
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
}
