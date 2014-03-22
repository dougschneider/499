import java.util.LinkedList;


public class Path {
	
	private LinkedList<WayPoint> path;
	private int current;

	public Path()
	{
		current = 0;
		
		this.path = new LinkedList<WayPoint>();
		this.path.addLast(new WayPoint(858, 150));
		this.path.addLast(new WayPoint(358, 150));
		this.path.addLast(new WayPoint(280, 637));
		

		this.path.addLast(new WayPoint(858, 250));
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
		System.out.println(opp);
		System.out.println(adj);
		double angle = Math.atan(opp/adj);
		angle = Math.toDegrees(angle);
		if(opp < 0 && adj > 0)
		{
			System.out.println("1");
			angle += 180;
		}
		else if(opp < 0 && adj < 0)
		{
			System.out.println("1");
			angle += 270;
		}
		else if(opp > 0 && adj < 0)
		{
			System.out.println("3");
			angle += 90;
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
