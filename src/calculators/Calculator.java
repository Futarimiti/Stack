package calculators;

import java.math.BigDecimal;

public interface Calculator
{
	BigDecimal compute(String expression);
	
	int scale();
	
	void setScale(int newScale);
}
