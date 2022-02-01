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
