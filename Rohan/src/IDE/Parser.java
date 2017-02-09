
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rohan
 */
public class Parser {
public static void main(String[] args){
	System.out.println(parse(new java.util.Scanner(System.in).nextLine()));
}
/**
	* Evaluates a string as a grammar and returns a value dictated by math
	* @param s the string to parse
	* @return Its value.
	* This method will take input as an expression and evaluate it. An expression can be generally defined as
	* (Expression[+-/*^]expression)
	*/
public static double parse(String s){
Expression e = new Expression(s);
	return e.getValue();
}
public static int[] getMatchingMap(String s){
	 int[] matchingMap = new int[s.length()];
Stack<Integer> parenStack = new Stack<>();
for(int i=0;i<s.length();i++){
	if(s.charAt(i)=='('){
		parenStack.push(i);
	}
	if(s.charAt(i)==')'){
		int temp = parenStack.pop();
		matchingMap[temp] = i;
		matchingMap[i]= temp;
	}
}
return matchingMap;
}
public static class Expression{
	char operator;
	String straightValue;
private double val;
boolean variable;
	boolean isSingleValue;
	Expression left;
	Expression right;
	String s;
	public Expression(String s){
		this.s =s;
		if(!s.matches("(.*)[\\Q=+-*/^\\E](.*)")){
			isSingleValue = true;
			straightValue = s;
		try{
		val = Double.parseDouble(s);
		}catch(Exception ex){
			variable = true;
		}
		}
		else{
			int[] matchingMap = getMatchingMap(s);
			int x = matchingMap[1];


			int mid = x==0?getFirst(s):x+1;
		left = new Expression(s.substring(1,mid));

		right = new Expression(s.substring(mid+1,s.length()-1));
		operator = s.charAt(mid);
		}
	}
	public double getStraightValue(){
		return variable?getVariableValue():Double.parseDouble(straightValue);
	}
	public double getVariableValue(){
		return 2                                                                                                                                        ;
	}
public double getValue(){
	if(isSingleValue){
		return getStraightValue();
	}
	double leftVal = left.getValue();
	double rightVal = right.getValue();
	switch(operator){
		case '*':
			return leftVal*rightVal;
		case '-':
			return leftVal-rightVal;
					case '/':
			return leftVal/rightVal;
			case '+':
			return leftVal+rightVal;
			case '^':
			return Math.pow(leftVal,rightVal);
			default:
				//prerequisites not held
				System.err.println("Deformed pattern"+s);
	return -1;
	}
	
}
private int getFirst(String s){
	for(int i =0;i<s.length();i++){
		if("+-/*^".contains(s.charAt(i)+"")){
			return i;
		}
	}
return -1;
}
}
}
