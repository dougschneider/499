import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;


public class Robot {

	// the directions the robot can face
	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	
	// the current location and heading of the robot
	public int y;
	public int x;
	public int direction;
	
	private DifferentialPilot pilot;
	
	/**
	 * Create a robot with the given x, y location facing
	 * the given direction.
	 */
	public Robot(int x, int y, int direction)
	{
		this.y = y;
		this.x = x;
		this.direction = direction;

		// create a pilot for this robot
		this.pilot = new DifferentialPilot(56, 120, Motor.C, Motor.A);
		this.pilot.setRotateSpeed(100);
		this.pilot.setTravelSpeed(100);
	}
	
	public void travel(int x, int y)
	{
		// make the robot face the required direction
		if(x<this.x)
			face(EAST);
		else if(x>this.x)
			face(WEST);
		else if(y<this.y)
			face(SOUTH);
		else if(y>this.y)
			face(NORTH);
		
		// move forward
		pilot.travel(25);
		
		// set the new location
		this.x = x;
		this.y = y;
	}

	/**
	 * Make the robot face the given direction.
	 * Only left turns are used.
	 * @param direction
	 */
	public void face(int direction)
	{
		// if we're already facing the direction, we're done
		if(this.direction == direction)
			return;
		
		// turn left until we're facing the given direction
		while(this.direction != direction)
		{
			pilot.rotate(95);
			this.direction = (this.direction-1+4) % 4;
		}
	}
	
}
