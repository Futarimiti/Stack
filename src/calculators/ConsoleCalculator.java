package calculators;

import java.io.Console;
import java.math.BigDecimal;

/**
 * a console utility for evaluating arithmetic expressions.
 */
public class ConsoleCalculator
{
	private static final String GREEN = "\u001B[32m";
	private static final String RESET = "\u001B[0m";
	
	public static Console console = System.console();
	public static int defaultScale = 4;
	private static Calculator c = new PostfixCalculator(defaultScale);
	
	private static void chmod(Class<? extends Calculator> mode) throws ReflectiveOperationException
	{
		c = mode.getConstructor(int.class).newInstance(defaultScale);
		String currMode = mode.equals(InfixCalculator.class) ? "infix" : "postfix";
		String nextMode = currMode.equals("infix") ? "postfix" : "infix";
		
		console.printf("%sSwitch to %s mode\n" , GREEN , currMode);
		console.printf("Type \":%s\" to switch to %s mode%s\n\n" , nextMode , nextMode , RESET);
	}
	
	public static void main(String[] args)
	{
		if (console == null) System.exit(1);
		
		console.printf("Calculator initialised with default scale %d\n" , defaultScale);
		console.printf("Type \":scale <num>\" to amend scale\n");
		console.printf("Type \":infix\" to switch to infix mode\n");
		console.printf("Type \":quit\" to exit\n\n");
		
		while (true)
		{
			String receive = console.readLine();
			// could $receive be null?
			// java docs: (returns) null if an end of stream has been reached.
			
			try
			{
				String trimmedReceive = receive.trim();
				
				// identify commands
				if (trimmedReceive.startsWith(":"))
				{
					if (trimmedReceive.equals(":scale")) console.printf("%d\n\n" , c.scale());
					else if (trimmedReceive.matches(":scale\\s+[0-9]+"))
					{
						StringBuilder numSb = new StringBuilder();
						
						for (int i = 7 ; i < trimmedReceive.length() ; i++)
						{
							char ch = trimmedReceive.charAt(i);
							if (Character.isDigit(ch)) numSb.append(ch);
						}
						
						int newScale = Integer.parseInt(numSb.toString());
						
						c.setScale(newScale);
						// console.printf("New calculator initialised with capacity %d\n\n" , newScale);
						console.printf("\n");
					}
					else if (trimmedReceive.equals(":q") || trimmedReceive.equals(":quit")) System.exit(0);
					else if (trimmedReceive.equals(":infix")) chmod(InfixCalculator.class);
					else if (trimmedReceive.equals(":postfix")) chmod(PostfixCalculator.class);
					else console.printf("Unrecognised command, or argument(s) not fitting pattern: %s\n\n" ,
								trimmedReceive.substring(1));
				}
				else // expression
				{
					BigDecimal res = c.compute(trimmedReceive);
					console.printf("%s%s%s\n\n" , GREEN , res.toString() , RESET);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
				console.printf("\n");
			}
		}
	}
}