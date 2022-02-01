import static java.lang.String.format;
import static java.lang.String.join;

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

	public boolean isFull()
	{
		return top == maxSize - 1;
	}

	public boolean isEmpty()
	{
		return top == -1;
	}

	public boolean push(T elem)
	{
		if (this.isFull()) return false;
		
		this.top++;
		this.stack[top] = elem;

		return true;
	}
	
	@SuppressWarnings("unchecked")
	public T pop()
	{
		if (this.isEmpty()) throw new IndexOutOfBoundsException("Popping from an empty stack");

		this.top--;
		return (T)this.stack[top + 1];
	}

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

	@SuppressWarnings("unchecked")
	public T peek()
	{
		return (T)this.stack[top];
	}
}
