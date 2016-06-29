package ru.mirea.spo.lab1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class Parser {
	private List<Token> tokens;
	private Token currentToken;
	private Stack<Token> bracketsStack = new Stack<Token>();
	private int currentTokenNumber = 0;
	
	
	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
		
	}
	
	
	public void lang() throws Exception {
		boolean exist = false;
		
		while (currentTokenNumber < tokens.size() && expr()){
			exist = true;
		}
		if(!exist){
			throw new Exception("error in lang");
		}
		
	}
	
	
	public boolean expr() throws Exception {
		if(declare()||assign()){
			return true;
		}else {
			throw new Exception("declare or assign expected, but "
					+currentToken +"found.");
		}
		
		
	}
	
	
	
	
	


    public boolean declare() throws Exception{
        if(varKw()){
            if(ws()){
                if(var()){
                    if(!sm())
                        throw new Exception("Expected ';' ");
                    else
                        return true;
                }else {                 
                    return false;
                }
            } else {
                return false;
            }
        }else {
            return false;
        }
    }
	
	public boolean assign() throws Exception{
		if(var()){
			if(assignOp()){
				if(stmt()){
                    if(!sm())
                        throw new Exception("Expected ';' but "
                                +currentToken +" found.");
                    else
                        return true;
				}
                else {
                    throw new Exception("stmt  expected, but "
                            +currentToken +"found.");
                }
			} else{
				throw new Exception("assignOp  expected, but "
						+currentToken +"found.");

			}
		}else {
			return false;
		}

	}
	
	
	public boolean stmt() throws Exception{
		if(stmtUnit()){
			while(plus()||minus()||umn()||del()){
				if(!stmtUnit()){
					throw new Exception("stmt_unit  expected, but "
							+ currentToken +"found.");
				}
			} 
			if(bracketsStack.empty())
				return true;
			else
				throw new Exception("Braket ')' expected ");
		}else {
			throw new Exception("stmt_unit  expected, but "
					+ currentToken +"found.");
		}
		
	}
	public boolean stmtUnit() throws Exception{
		if (openBr()){
			bracketsStack.push(currentToken);
			if(!stmt()){
				throw new Exception("stmt expected, but "
						+ currentToken +"found.");
			}else
				return true;
		}else if(digit()||var()){
			if(closeBr()){
				do{
					if(bracketsStack.empty())
						throw new Exception("anexpected ')'");
					bracketsStack.pop();
				}while(closeBr());
				
			}
			return true;
		}
		return false;
		
	}
	

	
	public boolean sm()  {
		int temp=currentTokenNumber; 
		match();
		if(!currentToken.getName().equals("SM"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("SM");
		
	}
	public boolean varKw(){
		int temp=currentTokenNumber; 
		match();
		if(!currentToken.getName().equals("VAR_KW")){
			currentTokenNumber=temp;
		}
		return currentToken.getName().equals("VAR_KW");
	}
	public boolean assignOp() {
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("ASSIGN_OP"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("ASSIGN_OP");
	}

	
	public boolean plus() {
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("PL"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("PL");
	}
	
	public boolean minus() {
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("MN"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("MN");
	}
	public boolean umn() {
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("UMN"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("UMN");
	}
	public boolean del() {
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("DL"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("DL");
	}
	
	public boolean openBr() {
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("BR_OP"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("BR_OP");	
	}
	
	public boolean closeBr() {
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("BR_CL"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("BR_CL");	
	}
	
	public boolean digit() {
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("DIGIT"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("DIGIT");
	}
	public boolean var() {
		int temp=currentTokenNumber; 
		wsIgnore();
		if(!currentToken.getName().equals("VAR"))
			currentTokenNumber=temp;
		return currentToken.getName().equals("VAR");
	}
	
	public boolean ws() {
		int temp=currentTokenNumber; 
		match();
		return currentToken.getName().equals("WS");
	}
	
	private void wsIgnore(){
		do
			match();
		while (currentToken.getName().equals("WS"));
	}
	
	public boolean match(){
		if (currentTokenNumber < tokens.size()) {
			currentToken = tokens.get(currentTokenNumber);
			currentTokenNumber++;
			return true;
		} else {
			return false;
		}
		
	}
	private int getPrior(Token op) {
		
		if(op.getName().equals("PL")||op.getName().equals("MN")){
			return 1;
		}else if(op.getName().equals("DL")||op.getName().equals("UMN")){
			return 2;
		}
		return 0;
		
	}

	public List<PostfixToken> getPostfixToken() throws Exception {
		currentTokenNumber=0;
		
		List<PostfixToken> poliz = new ArrayList<PostfixToken>();
		Stack<PostfixToken> stack = new Stack<PostfixToken>();
		boolean t=true;
		while(currentTokenNumber<tokens.size()){
			
			System.out.println("token1 "+currentToken);
		//	if(varKw()){ currentTokenNumber=currentTokenNumber+2;
			 if(var()||digit()){
				poliz.add(new PostfixToken(currentToken.getName(), currentToken.getValue()));
			
			}
			else if (assignOp()||minus()||plus()||umn()||del()){
				if(!stack.empty()&&!stack.peek().equals("BR_OP")){
					
				
					while(!stack.empty()&&(getPrior(currentToken)<=getPrior(stack.peek()))){
						poliz.add(stack.pop());
						
					}
				}	
				stack.push(new PostfixToken(currentToken.getName(), currentToken.getValue()));
				
			}
			else if(openBr()){
				stack.push(new PostfixToken(currentToken.getName(), currentToken.getValue()));
			}
			else if(closeBr()){
				PostfixToken temp;
				temp=stack.pop();
				while(!temp.getName().equals("BR_OP")){
					poliz.add(temp);
					temp=stack.pop();
				}
				
			}else if(sm()){ 
				while(!stack.empty()){ 
					poliz.add(stack.pop()); }
				
			} else
				currentTokenNumber++;
		}
		while(!stack.empty()){
			poliz.add(stack.pop());
		}
		return poliz;
	}
}
