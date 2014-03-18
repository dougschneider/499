import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;


public class Robot {

	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	
	public int y;
	public int x;
	public int direction;
	
	private DifferentialPilot pilot;
	
	public Robot(int x, int y, int direction)
	{
		this.y = y;
		this.x = x;
		this.direction = direction;
		
		this.pilot = new DifferentialPilot(56, 120, Motor.C, Motor.A);
		this.pilot.setRotateSpeed(100);
		this.pilot.setTravelSpeed(100);
	}
	
	public void travel(int x, int y)
	{
		if(x<this.x)
			face(EAST);
		else if(x>this.x)
			face(WEST);
		else if(y<this.y)
			face(SOUTH);
		else if(y>this.y)
			face(NORTH);
		
		pilot.travel(25);
		
		this.x = x;
		this.y = y;
	}

	public void face(int direction)
	{
		System.out.println(direction);
		System.out.println(this.direction);
		if(this.direction == direction)
			return;
		
		while(this.direction != direction)
		{
			pilot.rotate(95);
			this.direction = (this.direction-1+4) % 4;
		}
	}
	
}
