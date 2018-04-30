
package astar;

import java.util.ArrayList;


public class Queue<T extends Comparable<T>> 
{
	private ArrayList<T> queue;
	private int max_capacity;

	public Queue(int sz) 
	{
		if (sz <= 0)
			throw new IllegalArgumentException();

		this.queue = new ArrayList<>(sz);
		this.max_capacity = sz;
	}

	public void insert(T el)
	{
		if (this.size() == this.max_capacity)
		{
			if (el.compareTo(this.queue.get(this.size() - 1)) >= 0)
				return;

			this.queue.remove(this.size() - 1);
		}

		if (this.queue.isEmpty())
		{
			this.queue.add(el);
			return;
		}

		this.queue.add(this.findIndex(el, 0, this.size() - 1), el);
	}

	private int findIndex(T el, int first, int last) 
	{
		//System.out.println(String.format("Called with first: %d, last %d, el: %d", first, last, el));

		int mid = first + ((last - first) >> 1);
		//System.out.println("mid:" + mid);

		T midElement = this.queue.get(mid);

		if (first == last)
		{
			if (el.compareTo(midElement) <= 0)
			{
				return mid;
			}
			else
				return mid + 1;
		}

		if (el.compareTo(midElement) <= 0)
			return this.findIndex(el, first, mid);
		else
			return this.findIndex(el, mid + 1, last);
	}
	
	public void update(T el)
	{
		this.queue.remove(el);
		this.insert(el);
	}

	public T peek() 
	{
		return this.queue.get(0);
	}

	public T pop() 
	{
		T ret = this.peek();

		this.queue.remove(0);

		return ret;
	}

	public int size() 
	{
		return this.queue.size();
	}

	public boolean isEmpty() 
	{
		return this.queue.isEmpty();
	}

	@Override
	public String toString()
	{
		return this.queue.toString();
	}
}

