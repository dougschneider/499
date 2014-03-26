import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.nxt.remote.RemoteMotor;
import lejos.robotics.navigation.DifferentialPilot;


public class Robot {
	
	private static final int obstacleDist = 20;// mm (distance to obstacle from sensors facing down)
	private static final int midDist = 50;// cm (dist to wall from middle)
	private static final int obstacleWidth = 100;// mm
	private static final int farDistance = 90;// cm (dist to wall from far tape
	
	private DifferentialPilot pilot;
	
	private UltrasonicSensor wallSensor;
	private OpticalDistanceSensor leftObstacleSensor;
	private OpticalDistanceSensor rightObstacleSensor;
	
	public Robot(int wheelDiameter, int trackWidth, RemoteMotor leftMotor, RemoteMotor rightMotor, UltrasonicSensor wallSensor,
			     OpticalDistanceSensor leftObstacleSensor, OpticalDistanceSensor rightObstacleSensor)
	{
		this.pilot = new DifferentialPilot(wheelDiameter, trackWidth, leftMotor, rightMotor);
		this.pilot.setRotateSpeed(200);
		this.pilot.setTravelSpeed(200);
		
		this.wallSensor = wallSensor;
		this.leftObstacleSensor = leftObstacleSensor;
		this.rightObstacleSensor = rightObstacleSensor;
	}
	
	public void run()
	{
		faceGoal();
		while(true)
		{
			if(shouldAvoid())
			{
				this.pilot.stop();
				avoid();
			}
			else
			{
				this.pilot.forward();
			}
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void faceGoal()
	{
		while(true)
		{
			if(this.wallSensor.getDistance() <= Robot.farDistance)
				break;
			
			this.pilot.rotateRight();
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void avoid()
	{
		if(isCloseToWall())
		{
			while(shouldAvoid())
				avoidToRight();
		}
		else
		{
			while(shouldAvoid())
				avoidToLeft();
		}
	}
	
	private void avoidToRight()
	{
		this.pilot.rotate(-90);
		this.pilot.travel(Robot.obstacleWidth);
		this.pilot.rotate(90);
	}
	
	private void avoidToLeft()
	{
		this.pilot.rotate(-90);
		this.pilot.travel(Robot.obstacleWidth);
		this.pilot.rotate(90);
	}
	
	private boolean isCloseToWall()
	{
		return this.wallSensor.getDistance() < Robot.midDist;
	}
	
	private boolean shouldAvoid()
	{
		return this.leftObstacleSensor.getDistance() < Robot.obstacleDist || this.rightObstacleSensor.getDistance() < Robot.obstacleDist;
	}
}
