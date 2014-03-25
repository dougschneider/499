import java.util.LinkedList;


public class Path {
	
	private LinkedList<WayPoint> path;
	private int current;

	public Path()
	{
		current = 0;
		
		this.path = new LinkedList<WayPoint>();
		this.path.addLast(new WayPoint(848, 150));
		this.path.addLast(new WayPoint(248, 150));
		this.path.addLast(new WayPoint(200, 670));
		this.path.addLast(new WayPoint(356, 720));
		this.path.addLast(new WayPoint(468, 380));
		this.path.addLast(new WayPoint(848, 380));
	}
	
	public void goToNext()
	{
		this.current = (current + 1) % this.path.size();
	}
	
	/**
	 * Angle from current waypoint to next, angle from
	 * 0.
	 * @return
	 */
	public double getNextAngle()
	{
		WayPoint wp = this.path.get(current);
		WayPoint next = this.path.get((current + 1) % this.path.size());
		
		double opp = next.y - wp.y;
		double adj = next.x - wp.x;
		double angle = Math.atan(opp/adj);
		angle = Math.abs(Math.toDegrees(angle));
		if(opp <= 0 && adj <= 0)
		{
			angle += 180;
		}
		else if(opp >= 0 && adj <= 0)
		{
			angle = 180 - angle;
		}
		else if(opp <= 0 && adj >= 0)
		{
			angle = 360 - angle;
		}

		return angle;
	}
	
	public double getNextDistance()
	{
		WayPoint wp = this.path.get(current);
		WayPoint next = this.path.get((current + 1) % this.path.size()); 
		return Math.sqrt(Math.pow((wp.x - next.x), 2) + Math.pow(wp.y - next.y, 2));
	}
}
