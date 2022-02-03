package calculators.leetcode;

import java.util.Stack;

/**
 * https://leetcode.com/problems/basic-calculator/
 * operators: + - ( )
 * input expression guaranteed to be valid
 * only int
 * no need to cast to long
 * - could be used as unary
 * no consecutive operators
 */
public class BasicCalculator
{
	public static int calculate(String expression)
	{
		// no need to check validity of expression
		
		Stack<Integer> numStack = new Stack<>();
		char operatorRegistry = '\0';
		// no need operator stack because no priority issues;
		// do the operation once
		
		// traverse through expression
		for (int i = 0 ; i < expression.length() ; )
		{
			char ch = expression.charAt(i);
			
			/*
			possible ch:
				1. num
				2. operator
				3. (
				4. space
			 */
			if (Character.isDigit(ch))
			{
				int thisNum = Character.getNumericValue(ch); // initially
				
				// iterate to see this num has more digits
				// if yes, multiply current num by 10 and add new digit
				if (++i >= expression.length())
				{
					numStack.push(thisNum);
					break;
				}
				for ( ; i < expression.length() ; i++)
				{
					ch = expression.charAt(i);
					if (Character.isDigit(ch))
					{
						thisNum *= 10;
						thisNum += Character.getNumericValue(ch);
					}
					else break;
				}
				
				numStack.push(thisNum);
				// no need i++
			}
			else switch (ch)
			{
				case '+' -> {
					if (operatorRegistry != '\0')
					{
						int num2 = numStack.pop();
						int num1 = numStack.pop();
						switch (operatorRegistry)
						{
							case '+' -> numStack.push(num1 + num2);
							case '-' -> numStack.push(num1 - num2);
						}
					}
					operatorRegistry = '+';
					i++;
				}
				
				case '-' -> {
					// unary?
					if (numStack.isEmpty()) numStack.push(0);
					else if (operatorRegistry != '\0')
					{
						int num2 = numStack.pop();
						int num1 = numStack.pop();
						switch (operatorRegistry)
						{
							case '+' -> numStack.push(num1 + num2);
							case '-' -> numStack.push(num1 - num2);
						}
					}
					operatorRegistry = '-';
					i++;
				}
				
				case '(' -> {
					int leftBracketIndex = i;
					
					int bracketLv = 1;
					for (i++; bracketLv > 0 ; i++)
					{
						ch = expression.charAt(i);
						switch (ch)
						{
							case '(' -> bracketLv++;
							case ')' -> bracketLv--;
							default -> {}
						}
					}
					
					int rightBracketIndex = i - 1;
					String bracketedExpression = expression.substring(leftBracketIndex + 1 , rightBracketIndex);
					int res = calculate(bracketedExpression);
					numStack.push(res);
					// no need i++
				}
				
				case ' ' -> i++;
				default -> throw new IllegalStateException("Unexpected value: " + ch);
			}
		}
		
		// last calc
		if (numStack.size() == 1) return numStack.pop();
		assert numStack.size() == 2;
		int num2 = numStack.pop();
		int num1 = numStack.pop();
		switch (operatorRegistry)
		{
			case '-' -> {return num1 - num2;}
			case '+' -> {return num1 + num2;}
			
			default -> throw new IllegalStateException("Unexpected value: " + operatorRegistry);
		}
	}
}