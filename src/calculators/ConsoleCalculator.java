package calculators;

import java.io.Console;
import java.math.BigDecimal;

/**
 * a console utility for evaluating arithmetic expressions.
 */
public class ConsoleCalculator
{
	public static void main(String[] args)
	{
		// a console calculator util
		Console console = System.console();
		if (console == null) System.exit(1);
		
		int defaultScale = 4;
		InfixCalculator c = new InfixCalculator(defaultScale);
		
		console.printf("Calculator initialised with default scale %d\n" , defaultScale);
		console.printf("Type \":scale <num>\" to amend scale\n");
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
				if (trimmedReceive.matches(":scale\\s+[0-9]+"))
				{
					StringBuilder numSb = new StringBuilder();
					
					for (int i = 7 ; i < trimmedReceive.length() ; i++)
					{
						char ch = trimmedReceive.charAt(i);
						if (Character.isDigit(ch)) numSb.append(ch);
					}
					
					int newCapacity = Integer.parseInt(numSb.toString());
					
					c = new InfixCalculator(newCapacity);
					console.printf("New calculator initialised with capacity %d\n\n" , newCapacity);
				}
				else if (trimmedReceive.equals(":q") || trimmedReceive.equals(":quit")) System.exit(0);
				else if (trimmedReceive.startsWith(":"))
					console.printf("Unrecognised command, or argument(s) not fitting pattern: %s\n\n" ,
					               trimmedReceive.substring(1));
				else // expression
				{
					BigDecimal res = c.compute(trimmedReceive);
					String GREEN = "\u001B[32m";
					String RESET = "\u001B[0m";
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