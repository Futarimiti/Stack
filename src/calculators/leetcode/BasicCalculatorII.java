package calculators.leetcode;

import static java.lang.Character.getNumericValue;
import static java.lang.Character.isDigit;

import java.util.List;
import java.util.Stack;

/**
 * https://leetcode.com/problems/basic-calculator-ii/
 * now 4 operators, no brackets
 * - no longer unary because all ints are non-negative
 * always int, no need to cast to long
 * no syntax check need
 */
public class BasicCalculatorII
{
	private static int calc(int num1 , char operator , int num2)
	{
		return switch (operator)
				{
					case '+' -> num1 + num2;
					case '-' -> num1 - num2;
					case '*' -> num1 * num2;
					case '/' -> num1 / num2;
					default -> throw new IllegalStateException("wtf");
				};
	}
	
	private static int priorityOf(char operator)
	{
		// maybe use properties file at some time?
		return switch (operator)
				{
					case '+' , '-' -> 0;
					case '*' , '/' -> 1;
					default -> throw new IllegalStateException("wtf");
				};
	}
	
	private static boolean isOperator(char c)
	{
		return List.of('+' , '-' , '*' , '/').contains(c);
	}
	
	public static int calculate(String expression)
	{
		Stack<Integer> numStack = new Stack<>();
		Stack<Character> operatorStack = new Stack<>();
		
		for (int i = 0 ; i < expression.length() ; )
		{
			char ch = expression.charAt(i);
			
			if (isDigit(ch))
			{
				int num = getNumericValue(ch);
				for (i++; i < expression.length() ; i++)
				{
					ch = expression.charAt(i);
					if (isDigit(ch))
					{
						num *= 10;
						num += getNumericValue(ch);
					}
					else break;
				}
				numStack.push(num);
				// no i++
			}
			else if (isOperator(ch))
			{
				if (operatorStack.isEmpty()) operatorStack.push(ch);
				else if (priorityOf(ch) > priorityOf(operatorStack.peek())) operatorStack.push(ch);
				else
				{
					while (!operatorStack.isEmpty() && priorityOf(operatorStack.peek()) >= priorityOf(ch))
					{
						int num2 = numStack.pop();
						int num1 = numStack.pop();
						char operator = operatorStack.pop();
						numStack.push(calc(num1 , operator , num2));
					}
					operatorStack.push(ch);
				}
				i++;
			}
			else
			{
				assert ch == ' ';
				i++;
			}
		}
		
		while (numStack.size() > 1)
		{
			int num2 = numStack.pop();
			int num1 = numStack.pop();
			char operator = operatorStack.pop();
			numStack.push(calc(num1 , operator , num2));
		}
		
		assert numStack.size() == 1 && operatorStack.isEmpty();
		return numStack.pop();
	}
}