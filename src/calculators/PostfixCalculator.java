package calculators;

import static java.lang.Character.isDigit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;

import stacks.ArrayStack;

public class PostfixCalculator implements Calculator
{
	/**
	 * a map of character of binary operator to corresponding binary BigDecimal function.
	 * to be initialised in constructor.
	 */
	public final Map<Character, BinaryOperator<BigDecimal>> operations;
	
	/**
	 * a set of recognised operators, basically `operations.keySet()`.
	 * to be initialised in constructor as that's where $operations is initialised.
	 */
	public final Set<Character> operators;
	
	/**
	 * the scale (i.e. number of d.p.) when representing a decimal numeral.
	 * protected with setter to avoid being negative.
	 * by default 4.
	 */
	private int scale = 4;
	
	public PostfixCalculator()
	{
		operations = Map.of(
				'+' , BigDecimal::add ,
				'-' , BigDecimal::subtract ,
				'*' , BigDecimal::multiply ,
				'/' , (num1 , num2) -> num1.divide(num2 , this.scale , RoundingMode.HALF_UP)
				// '^' , (num1 , num2) -> {
				// 	if (!num2.toPlainString().matches("\\d+(\\.0*)?")) throw new IllegalArgumentException("For now, the exponent can only be a positive integer");
				// 	return num1.pow(num2.intValue());
				// }
		);
		operators = operations.keySet();
	}
	
	public PostfixCalculator(int scale)
	{
		this.scale = scale;
		operations = Map.of(
				'+' , BigDecimal::add ,
				'-' , BigDecimal::subtract ,
				'*' , BigDecimal::multiply ,
				'/' , (num1 , num2) -> num1.divide(num2 , this.scale , RoundingMode.HALF_UP)
				// '^' , (num1 , num2) -> {
				// 	if (!num2.toPlainString().matches("\\d+(\\.0*)?")) throw new IllegalArgumentException("For now, the exponent can only be a positive integer");
				// 	return num1.pow(num2.intValue());
				// }
		);
		operators = operations.keySet();
	}
	
	private static void emptyCheck(String expression)
	{
		if (expression == null) throw new NullPointerException();
		if (expression.length() == 0) throw new IllegalArithmeticExpressionSyntaxException("Empty expression");
	}
	
	/**
	 * scientifically evaluates a given expression.
	 * accepts numerals in decimal and scientific notation.
	 *
	 * @param expression the expression to be evaluated;
	 *                   numerals and operators must be separated with
	 *                   blank characters to avoid ambiguity with unary operator.
	 * @return the computed result as BigDecimal.
	 */
	public BigDecimal compute(String expression)
	{
		emptyCheck(expression);
		
		String[] parts = expression.split("\\s");
		return compute(parts);
	}
	
	public BigDecimal compute(String[] parts)
	{
		emptyCheck(parts);
		ArrayStack<BigDecimal> numStack = new ArrayStack<>(parts.length);
		
		for (String part : parts)
		{
			if (isDigit(part.charAt(0)) || part.length() >= 2) // num with 2+ digits, or signed num
			{
				BigDecimal thisNum = new BigDecimal(part);
				numStack.push(thisNum);
			}
			else
			{ // 1 digit: operator, or undefined symbol
				char ch = part.charAt(0);
				if (isOperator(ch))
				{
					if (numStack.size() < 2)
						throw new IllegalArithmeticExpressionSyntaxException("Missing one or more operand(s)");
					BigDecimal num2 = numStack.pop();
					BigDecimal num1 = numStack.pop();
					
					BigDecimal res = operations.get(ch).apply(num1 , num2);
					numStack.push(res);
				}
				else throw new IllegalArithmeticExpressionSyntaxException("Unrecognised symbol: '" + ch + "'");
			}
		}
		
		if (numStack.size() > 1)
			throw new IllegalArithmeticExpressionSyntaxException("Missing one or more operator(s)");
		return numStack.pop();
	}
	
	private void emptyCheck(String[] parts)
	{
		if (parts == null) throw new NullPointerException();
		if (parts.length == 0) throw new IllegalArithmeticExpressionSyntaxException("Empty expression");
	}
	
	/**
	 * getter for $scale.
	 */
	public int scale()
	{
		return this.scale;
	}
	
	/**
	 * setter for $scale that prohibits negative input.
	 */
	public void setScale(int scale)
	{
		if (scale < 0) throw new IllegalArithmeticExpressionSyntaxException("Scale cannot be negative: " + scale);
		this.scale = scale;
	}
	
	/**
	 * identifies if a character is an operator by checking if it is in $operators.
	 */
	private boolean isOperator(char c)
	{
		return operators.contains(c);
	}
}