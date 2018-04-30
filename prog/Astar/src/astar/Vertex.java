
package astar;

import java.util.ArrayList;


public class Vertex extends Position implements Comparable<Vertex> 
{
	private final boolean debugMode = false;
	
	private final ArrayList<Integer> idList;
	private String name;
	
	private boolean	visited;
	private boolean	closed;
	
	private double costG;
	private double costH = -1.0;
	
	private Vertex parent;
	private final ArrayList<Vertex> neighbors;

	public Vertex(double x, double y, int id)
	{
		super(x, y);
		
		this.reset();
		
		this.idList = new ArrayList<>();
		this.idList.add(id);
		
		this.neighbors = new ArrayList<>();
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public double getCost()
	{
		return this.costG + this.costH;
	}

	public double getGraphCost()
	{
		return this.costG;
	}

	public Vertex getParent()
	{
		return this.parent;
	}

	public void setParent(Vertex par)
	{
		this.parent = par;
	}

	public void updateGraphCost(double newG)
	{
		this.costG = newG;
	}

	public void setHeuristicCost(double cost)
	{
		if (this.costH < 0)
		{
			this.costH = cost;
		}
	}

	public boolean contains(int id)
	{
		return this.idList.contains(id);
	}

	public void markVisited()
	{
		this.visited = true;
	}

	public void markClosed()
	{
		this.closed = true;
	}

	public boolean isAlreadyVisited()
	{
		return this.visited;
	}

	public boolean isMarkedClosed() 
	{
		return this.closed;
	}

	public void addNeighbor(Vertex v)
	{
		this.neighbors.add(v);
	}

	public ArrayList<Vertex> getNeighbors()
	{
		return this.neighbors;
	}

	public void appendId(int id)
	{
		this.idList.add(id);
	}
	
	public void reset()
	{
		this.costG = Double.MAX_VALUE;
		this.costH = -1;
		this.closed = false;
		this.visited = false;
	}

	@Override
	public String toString()
	{
		if (this.debugMode)
		{
			String neighbs = this.neighbors.stream()
					.map((v) -> "\n\t" + v.getX() + ", " + v.getY() + ",")
					.reduce("[", String::concat);
			
			neighbs += ']';

			return String.format("\nPos=%s, \nIds=%s, \nName=%s, \nNeigbors=%s", 
				super.toString(), this.idList.toString(), 
				this.name == null ? "None" : this.name, neighbs);
		}
		return String.format("%f,%f", super.getX(), super.getY());
	}

	@Override
	public int compareTo(Vertex other) 
	{
		double ret = this.getCost() - other.getCost();
		if (ret > 0)
			return 1;
		if (ret < 0)
			return -1;
		return 0;
	}
}