package calculators;

/**
 * Thrown to indicate the passed arithmetic expression is syntactically incorrect.
 */
public class IllegalArithmeticExpressionSyntaxException extends IllegalArgumentException
{
	public IllegalArithmeticExpressionSyntaxException(String message)
	{
		super(message);
	}
}