### Stack
Stack is a first-in-last-out (FILO) ordered list: the datum comes in first comes out last, and the datum comes in last comes out first. It is the opposite of Queue.

The stack only allows insert and deletion in one end of the list, known as *top*, while the other fixed ending is *bottom*. The element came in first is at the bottom, and the last element is put at the top; this is the opposite for deletion.

When added to (the top of) a stack, an element is said to be *pushed*; Similarly, the element is *popped* when being removed (from the top) 

## Code implementation
We'll first use array to implement a stack.</br>
A part from the array, the stack should also include a cursor pointing to the top element `top`, a maximum size `maxSize`, 
We also include a constructor determining size of `stack`.
```java
public class ArrayStack<T>
{
	private final Object[] stack;
	private int top = -1;
	final int maxSize;

	public ArrayStack(int maxSize)
	{
		this.maxSize = maxSize;
		this.stack = new Object[maxSize];
	}
}
```
`top` inclusively points to the current top element; therefore it starts with -1, when no elements have been pushed.

Some other methods that could be useful:
```java
public boolean isFull()
{
	return top == maxSize - 1;
}

public boolean isEmpty()
{
	return top == -1;
}
```

Now we'll work on `push` and `pop`, 2 core methods for a stack.

If the stack is full, the push action is failed and should return false;</br>
Otherwise, move up `top` by 1 unit and assign to that index in `stack`:
```java
public boolean push(T elem)
{
	if (this.isFull()) return false;
	
	this.top++;
	this.stack[top] = elem;
                                     
	return true;
}
```

Then we work on `pop`, which removes an element from the stack and returns it;</br>
Because of this, an exception should be thrown when `stack` is detected to be empty;</br>
Otherwise, move down `top` by 1 and return `stack[top + 1]`:
```java
@SuppressWarnings("unchecked")
public T pop()
{
	if (this.isEmpty()) throw new IndexOutOfBoundsException("Popping from an empty stack");
                                                                                            
	this.top--;
	return (T)this.stack[top + 1];
}
```

An alternative writing compressing 2 statements to 1: `return stack[top--]`.

What else to do? `toString`?
```java
public String toString(String sep)
{
	String[] arr = new String[top + 1];
	for (int i = 0 ; i <= top ; i++)
	{
		arr[i] = this.stack[i].toString();
	}
                                             
	return format("[%s]" , join(sep , arr));
}
                                             
@Override
public String toString()
{
	return this.toString(" , ");
}
```

Also `peek` to peek at the top element:
```java
@SuppressWarnings("unchecked")
public T peek()
{
	return (T)this.stack[top];
}
```
...where the user can change the top element with the memory location returned. I don't know if this is ought to happening.

## Calculator utility
Stack can be utilised as a calculator - not some real-time calculations, but more like entering a string of numbers and operators;
stacks are good at this as calculating priorities vary among different operators, where a stack can handle them easily.

An arithmetic expression can be either infix, prefix or postfix. We'll do the infix calculator first. 

### Infix calculator
Ideas and concepts before code implementation:
1. Use a cursor to traverse throughout the given expression to scan operands and operators
	* The start of the expression must be a number
	* If scanned a numeral, continue scanning until reached a space or operator; the next object scanned should be an operator
	* Ignore ALL spaces

2. After finishing scanning a numeral, convert to BigDecimal and push into numeral stack

3. If scanned an operator:
	* Make sure the next object is a numeral
	* If the operator stack is empty, push
	* If there is already operator(s) in the stack, then:
		* If this operation is __prior__ to the existing (*/ to +-), push
		* If not, compute the existing operation (prior) by popping first two numerals and one operator, push the resulting numeral into numeral stack, and at then end push this operator

4. When finished scanning without issues, calculate numerals in stack from top to bottom, each time popping two numerals and an operator, and eventually push the resulting numeral back into numeral stack;</br>
After all the calculations (operator stack empty), the numeral stack should only contain 1 numeral as the final answer. If this is not the case, report as an unexpected exception.
* It's fine to put an equals sign at the end of expression; however it must be made sure that no more elements are following this space.

It will be a huge effort to explain on the code implementation, while the source file of infix calculator is well commented, under directory `calculators`; have a check if you want to go deeper.
Also some leetcode calculator problems are included under directory `leetcode`.

### Postfix calculator
Consider the case `2 3 4 * +`, or `2 + 3 × 4` in infix expression:

Scan from left to right:
* `2` - Add to numeral stack
* `3` - Add to numeral stack
* `4` - Add to numeral stack
* `*` - pop last two numerals and push in their product `12`
* `+` - pop last two numerals and push in their sum `14`

A postfix does not contain brackets, so we do not need to take calculating priority into consideration; whenever we meet an operation, we perform it with two numerals in the stack.