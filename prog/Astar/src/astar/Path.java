
package astar;

import java.util.ArrayList;
import java.util.Collections;


public class Path implements Comparable<Path>
{
	private final ArrayList<Position> path;
	private double totalDistance;
	private boolean isMin;
	private String taxiName;
	
	private final StringBuilder str;

	public Path()
	{
		path = new ArrayList<>(1000); // <-- size?
		totalDistance = 0.0;
		isMin = false;
		
		// 20000?
		this.str = new StringBuilder(20000); // <-- size?
	}
	
	public void addRoute(Position pos)
	{
		path.add(pos);
		
		str.append("\n");
		str.append(pos.toString());
		str.append(",0");
	}
	
	public String getTotalRouteString()
	{
		str.append("\n");		
		return str.toString();
	}
	
	public ArrayList<Position> getTotalRouteArray()
	{
		Collections.reverse(path);
		return this.path;
	}
	
	public double getTotalDistance()
	{
		return totalDistance;
	}
	
	public void setTotalDistance(double dist)
	{
		this.totalDistance = dist;
	}
	
	public void setTaxiName(String name)
	{
		this.taxiName = name;
	}
	
	public String getTaxiName()
	{
		return this.taxiName;
	}
	
	public void markShortest()
	{
		isMin = true;
	}
	
	public boolean isShortest()
	{
		return this.isMin;
	}

	@Override
	public int compareTo(Path other) 
	{
		double ret = this.getTotalDistance() - other.getTotalDistance();
		if (ret > 0)
			return 1;
		if (ret < 0)
			return -1;
		return 0;
	}
}
