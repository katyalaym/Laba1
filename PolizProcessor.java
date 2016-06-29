package ru.mirea.spo.lab1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class PolizProcessor {
	
	private Map<String, Integer> varTable = new HashMap<String, Integer>();
	 public static final String ASOP = "ASSIGN_OP";
	 public static final String OP_PL = "PL";
	 public static final String OP_MN = "MN";
	 public static final String OP_UMN = "UMN";
	 public static final String OP_DL = "DL";
	 public static final String VAR = "VAR";
	 public static final String DIG = "DIGIT";
	 
	 Stack<PostfixToken> stack = new Stack<PostfixToken>();
	
	 String mapvalue;
	 String mapname;
	private List<PostfixToken> postfixTokens;
	public PolizProcessor(List<PostfixToken> postfixTokens) {
		// TODO Auto-generated constructor stub
		this.postfixTokens = postfixTokens;
		
	}

	public void go() {
		for(PostfixToken a: postfixTokens ){
			
			if(a.getName().equals(VAR)||a.getName().equals(DIG)){
				stack.push(a);
				
		} else if(a.getName().equals(ASOP)){
			PostfixToken  fist = stack.pop();
			PostfixToken  second = stack.pop();
			varTable.put(second.getValue(),Integer.valueOf(fist.getValue()));
		} else if(a.getName().equals(OP_PL)){
			PostfixToken  fist = stack.pop();
			PostfixToken  second = stack.pop();
			stack.push(new PostfixToken("DIGIT", String.valueOf(selectionvalue(fist)+selectionvalue(second))));
		}else if(a.getName().equals(OP_MN)){
			PostfixToken  fist = stack.pop();
			PostfixToken  second = stack.pop();
			stack.push(new PostfixToken("DIGIT", String.valueOf(selectionvalue(fist)-selectionvalue(second))));
		} else if(a.getName().equals(OP_UMN)){
			PostfixToken  fist = stack.pop();
			PostfixToken  second = stack.pop();
			stack.push(new PostfixToken("DIGIT", String.valueOf(selectionvalue(fist)*selectionvalue(second))));
		} else if(a.getName().equals(OP_DL)){
			PostfixToken  fist = stack.pop();
			PostfixToken  second = stack.pop();
			stack.push(new PostfixToken("DIGIT", String.valueOf(selectionvalue(fist)/selectionvalue(second))));
		} 
	}	System.out.println("Решение");
		System.out.println(varTable);
	}
	private int selectionvalue(PostfixToken token){
		if(token.getName().equals("DIGIT"))
			return Integer.valueOf(token.getValue());
		if(token.getName().equals("VAR")){
			return Integer.valueOf(varTable.get(token.getValue()));

		} else return 0;

	}
	
	
	//public void assign(String name, int value){
		//varTable.put(name, value);
		
	//}
	
	

}
