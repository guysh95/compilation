/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/*************/
/* USER CODE */
/*************/
import java_cup.runtime.*;
import java.lang.Math;

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/

/*****************************************************/
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column

/*******************************************************************************/
/* Note that this has to be the EXACT same name of the class the CUP generates */
/*******************************************************************************/
%cupsym TokenNames

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup

/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied verbatim (letter to letter) into the Lexer class code.     */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */
/*****************************************************************************/
%{
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/
	private Symbol symbol(int type)               {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, Object value) {return new Symbol(type, yyline, yycolumn, value);}

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine() { return yyline + 1; }
	public int getCharPos() { return yycolumn; }

	/**********************************************/
	/* Enable token position extraction from main */
	/**********************************************/
	public int getTokenStartPosition() { return yycolumn + 1; }

	public Symbol check_integer(int val) throws RuntimeException {
		if (val <= (Math.pow(2, 15) - 1)){
			return symbol(TokenNames.INT, yytext());
		} else{
			/* throw new RuntimeException("Integer is too big"); */
			return symbol(TokenNames.TOKEN_ERROR);
		}
	}

%}

/***********************/
/* MACRO DECALARATIONS */
/***********************/
LineTerminator	= \r|\n|\r\n
WhiteSpace	   	= {LineTerminator} | [ \t\f]
INTEGER			    = 0 | [1-9][0-9]*
BAD_INTEGER         = 0[0-9]+
ID			       	= [a-zA-Z][a-zA-Z0-9]*
BAD_ID              = [0-9]+{ID}
NIL             = nil
TYPE_INT        = int
TYPE_STRING     = string
CLASS           = class
ARRAY           = array
EXTENDS         = extends
RETURN          = return
WHILE           = while
IF              = if
NEW             = new
STRING          = \"[a-zA-Z]*\" | \"\"
BAD_STRING      = \"[a-zA-Z]* | [a-zA-Z]*\" | \"([a-zA-Z]*)(~[a-zA-Z\"])
ValidInComment1  = ([\(\)\{\}\[\]\?\!\+\-\*\/\.\;a-zA-Z0-9 \t\f])*
ValidInComment2  = ([\(\)\{\}\[\]\?\!\+\-\*\/\.\;a-zA-Z0-9] | {WhiteSpace})*
COMMENT         = \/\/{ValidInComment1}{LineTerminator} | \/\*{ValidInComment2}\*\/

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/

/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/

<YYINITIAL> {

"+"					{ return symbol(TokenNames.PLUS);}
"-"					{ return symbol(TokenNames.MINUS);}
"*"				    { return symbol(TokenNames.TIMES);}
"/"					{ return symbol(TokenNames.DIVIDE);}
"("					{ return symbol(TokenNames.LPAREN);}
")"					{ return symbol(TokenNames.RPAREN);}
"["					{ return symbol(TokenNames.LBRACK);}
"]"					{ return symbol(TokenNames.RBRACK);}
"{"					{ return symbol(TokenNames.LBRACE);}
"}"					{ return symbol(TokenNames.RBRACE);}
","					{ return symbol(TokenNames.COMMA);}
"."					{ return symbol(TokenNames.DOT);}
";"					{ return symbol(TokenNames.SEMICOLON);}
"="                 { return symbol(TokenNames.EQ);}
":="                { return symbol(TokenNames.ASSIGN);}
"<"					{ return symbol(TokenNames.LT);}
">"					{ return symbol(TokenNames.GT);}
{TYPE_INT}          {return symbol(TokenNames.TYPE_INT);}
{TYPE_STRING}       {return symbol(TokenNames.TYPE_STRING);}
{NIL}			    { return symbol(TokenNames.NIL);}
{INTEGER}			{ return check_integer(new Integer(yytext())); }
{WhiteSpace}		{ /* just skip what was found, do nothing */ }
{CLASS}			    { return symbol(TokenNames.CLASS);}
{NIL}			    { return symbol(TokenNames.NIL);}
{ARRAY}			    { return symbol(TokenNames.ARRAY);}
{EXTENDS}			{ return symbol(TokenNames.EXTENDS);}
{RETURN}			{ return symbol(TokenNames.RETURN);}
{WHILE}			    { return symbol(TokenNames.WHILE);}
{IF}			    { return symbol(TokenNames.IF);}
{NEW}			    { return symbol(TokenNames.NEW);}
{ID}				{ return symbol(TokenNames.ID,     new String( yytext()));}
{COMMENT}			{ /* skip */ }
"//"                { return symbol(TokenNames.TOKEN_ERROR);}
"/*"                { return symbol(TokenNames.TOKEN_ERROR);}
{BAD_INTEGER}       { return symbol(TokenNames.TOKEN_ERROR);}
{STRING}			{ return symbol(TokenNames.STRING, new String( yytext()));}
{BAD_STRING}        { return symbol(TokenNames.TOKEN_ERROR);}
{BAD_ID}        { return symbol(TokenNames.TOKEN_ERROR);}
<<EOF>>				{ return symbol(TokenNames.EOF);}
}
