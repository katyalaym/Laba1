package ru.mirea.spo.lab1;

import java.io.IOException;
import java.util.List;

public class UI {
	static UtilHelper utilHelper = new UtilHelper();
	public static void main(String[] args) throws Exception{
		//System.out.println("hello word");
		
	validTest();
	
		
		
	}
	public static void validTest() throws Exception{
		//utilHelper.printFile("src/test-valid.input");
		process("src/test-valid.input");
		
	}

	
	public static void process(String filename) throws Exception{
		Lexer lexer = new Lexer();
		lexer.processInput(filename);
		List<Token> tokens = lexer.getTokens();
		Parser parser = new Parser();
		parser.setTokens(tokens);
		parser.lang();
		
		List<PostfixToken> postfixTokens = parser.getPostfixToken();
		for(PostfixToken p: postfixTokens){
			System.out.print(p.getValue()+" ");
		}
		PolizProcessor processor = new PolizProcessor(postfixTokens); 
		
		processor.go();
		
		
	}
	

}
