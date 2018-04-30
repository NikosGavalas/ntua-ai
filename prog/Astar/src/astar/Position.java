
package astar;


public class Position 
{
	private final double x;
	private final double y;

	public Position(double x, double y) 
	{
		this.x = x;
		this.y = y;
	}
	
	public double getX()
	{
		return this.x;
	}

	public double getY() 
	{
		return y;
	}
	
	public double distanceFrom(Position pos) 
	{
		double lon1 = this.x;
		double lat1 = this.y;
		double lon2 = pos.getX();
		double lat2 = pos.getY();
		
		final int R = 6371; // Radius of the earth

		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return R * c * 1000; // meters
	}

	@Override
	public String toString() 
	{
		return String.format("Position{x=%f, y=%f}", x, y);
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (obj == null)
			return false;
		
		final Position other = (Position) obj;
		return Double.doubleToLongBits(this.x) == Double.doubleToLongBits(other.getX())
				&& Double.doubleToLongBits(this.y) == Double.doubleToLongBits(other.getY());
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 97 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
		hash = 97 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
		return hash;
	}
}
