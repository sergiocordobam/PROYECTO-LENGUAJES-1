/** Programming Languajes
	By: Carmen Carvajal
	Lexer class: This class generates an arraylist containing every token in the source code
		
**/

import java.util.*;
import java.lang.*;
import java.io.*;

public class Lexer{
	
	//Define token codes based on the grammar
	public static final int PROGRAM = 1;
	public static final int ENDPROGRAM = 2;
	public static final int DEF = 3;
	public static final int ENDDEF = 4;
	public static final int IF = 5;
	public static final int ELSE = 6;
	public static final int ENDIF = 7;
	public static final int VARIABLE = 8;
	public static final int CONSTANT = 9;
	public static final int EQUALS = 10;
	public static final int ASSIGN = 11;
	public static final int LPAREN = 12;
	public static final int RPAREN = 13;
	public static final int INT = 14;
	public static final int READ = 15;
	public static final int PRINT = 16;
	public static final int CALL = 17;
	public static final int WHILE = 18;
	public static final int DEQUALS = 19;
	public static final int ADD = 20;
	public static final int MULT = 21;
	public static final int ENDWHILE = 22;
	public static final int INVALIDTOKEN=98;
	public static final int EOF = 99;





	//fileScanner: (Scanner) to iterate over the source file
	private Scanner fileScanner;
	
	//lineScanner: (Scanner) to iterate over each line in the source file
	private Scanner lineScanner;
	//keywordsTable: (ArrayList) stores the language keywords and symbols (e.g., print, read, program, etc...)
	public ArrayList<Token> 			keywordsTable;
	//tokenList: (ArrayList) contains every token in the source file
	private ArrayList<Token> 			tokenList;
	//tokenIterator: (ListIterator) used for visiting each token in the tokenList object
	private ListIterator<Token>			tokenIterator;
	//currentToken: (Token) stores the current token where the tokenIterator is pointing to
	private Token						currentToken;
	//lineCount: (int) count line in the each source file
	private int							lineCount;
	


	/**
		Constructor:
			Initialize the keywordsTable
			recognize and store each token in the source file
			initialize the token iterator
		
			@param fileName: source file path
	**/
	public Lexer (String fileName) throws FileNotFoundException {
		
		//Define an scanner for the source file
		fileScanner = new Scanner(new File(fileName));
		
		//Initialize the linecount
		lineCount=0;
		
		//Initialize the keyword table
		keywordsTable = new ArrayList<Token>();
		keywordsTable.add(new Token(PROGRAM, "program", 0));
		keywordsTable.add(new Token(ENDPROGRAM, "endprogram", 0));
		keywordsTable.add(new Token(DEF, "def", 0));
		keywordsTable.add(new Token(ENDDEF, "enddef", 0));
		keywordsTable.add(new Token(IF, "if", 0));//MODIFICADO POR ESTUDIANTES
		keywordsTable.add(new Token(ELSE, "else", 0));//MODIFICADO POR ESTUDIANTES
		keywordsTable.add(new Token(ENDIF, "endif", 0));
		keywordsTable.add(new Token(VARIABLE, "var", 0));//MODIFICADO POR ESTUDIANTES
		keywordsTable.add(new Token(CONSTANT, "cons", 0));//MODIFICADO POR ESTUDIANTES
		keywordsTable.add(new Token(EQUALS, "::", 0));//MODIFICADO POR ESTUDIANTES
		keywordsTable.add(new Token(DEQUALS,"!:",0));//MODIFICADO POR ESTUDIANTES
		keywordsTable.add(new Token(ASSIGN, ":", 0));//MODIFICADO POR ESTUDIANTES
		keywordsTable.add(new Token(LPAREN, "(", 0));
		keywordsTable.add(new Token(RPAREN, ")", 0));
		keywordsTable.add(new Token(INT, "int", 0));
		keywordsTable.add(new Token(READ, "read", 0));
		keywordsTable.add(new Token(PRINT, "print", 0));
		keywordsTable.add(new Token(CALL, "call", 0));
		keywordsTable.add(new Token(WHILE,"while", 0));//MODIFICADO POR ESTUDIANTES
		keywordsTable.add(new Token(ENDWHILE,"endwhile",0));//MODIFICADO POR ESTUDIANTES
		keywordsTable.add(new Token(ADD, "+", 0));//MODIFICADO POR ESTUDIANTES
		keywordsTable.add(new Token(MULT, "*",0));//MODIFICADO POR ESTUDIANTES
		keywordsTable.add(new Token(INVALIDTOKEN,"$",0));//MODIFICADO POR ESTUDIANTES
		keywordsTable.add(new Token(EOF, "EOF", 0));
		
		
		/** extract each token from the source file **/
		String tokenText;
		int tokenCode;
		//create the tokenList
		tokenList = new ArrayList<Token>();
		//go through the source file
		do{
			//Obtain the next token
			tokenText = nextText();
			//Classify each token
			tokenCode = getTokenCode(tokenText);
			//print token's data
			System.out.println(tokenText + " code: " + tokenCode + " line:" + lineCount);
			//add token to the tokenList
			tokenList.add(new Token(tokenCode, tokenText, lineCount));
		}
		while(tokenText.compareTo("EOF")!=0);

		//Initialize the token iterator pointing to before the first element in the list
		tokenIterator = tokenList.listIterator();
		
	}

	/* Function nextText: extract each token from the source file
	*/
	public String nextText(){
		//actual token text`
		String text;
		//do while EOF (end of file) is not found
		do {
			text = null;
			//If the lineScanner has not been created, create a new lineScanner
			if (lineScanner == null) {
				//Verify for a new line in the source file
				if (fileScanner.hasNextLine()){
					//read the new line
					String line = fileScanner.nextLine();
					//Count the new line
					lineCount++;
					//Create a new lineScanner
					lineScanner = new Scanner(line);
				} else {
					//If not more line, EOF has been found
					text = "EOF";
				}
			} else {
				//read each token in the line
				if (lineScanner.hasNext()){
					//There are more tokens in the current line
					text = lineScanner.next();
				} else {
					//End of line, discard the lineScanner
					lineScanner = null;
				}
					
			}
		} while(text ==null);
		
		//Return the token
		return text;
	}

	/**
		Function nextToken: point the listIterator to the next token and return it
	**/
	public Token nextToken()
	{		
		//move the list iterator to the next token
		currentToken = tokenIterator.next(); 
		return currentToken;
	}
	
	/**
		Function getCurrentToken: return the token to which the listIterator is pointing to
	**/
	public Token getCurrentToken(){
		return currentToken;
	}

	/**
		Function getTokenCode: Search for the token code in the keywordsTable
			@param tokenText: (String) actual token text found in the source file
	**/
	public int getTokenCode(String tokenText)
	{
		//Create an iterator to traverse the keywordTable
		Iterator<Token> it = keywordsTable.iterator();
		Token tmpToken;
		//The token text has not been found
		int tokenCode = -1;
		//While the iterator can move forward and the token has not been found
		while ((it.hasNext()) && (tokenCode==-1)){
			//move the iterator to the next row in the keywordTable
			tmpToken = it.next();
			//Verify if the actual token text is equals to the iterator point
			if (tmpToken.text.compareTo(tokenText)==0)
			tokenCode = tmpToken.code;
		}
		
		//If the token was not found, it is a variable
		if (tokenCode ==-1)
			tokenCode = VARIABLE;
		
		//Return the actual token code
		return tokenCode;
	}
	
}
