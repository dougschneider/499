import lejos.nxt.LightSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.nxt.remote.RemoteMotor;
import lejos.robotics.navigation.DifferentialPilot;


public class Robot {
	
	private static final int obstacleDist = 160;// mm (distance to obstacle from sensors facing down)
	private static final int midDist = 37;// cm (dist to wall from middle)
	private static final int obstacleWidth = 200;// mm
	private static final int farDistance = 90;// cm (dist to wall from far tape
	private static final int groundColor = 30;// a little above ground light value
	
    // the pilot navigating the robot
	private DifferentialPilot pilot;
	
    // the sensors used to avoid the obstacles
	private UltrasonicSensor wallSensor;
	private OpticalDistanceSensor leftObstacleSensor;
	private OpticalDistanceSensor rightObstacleSensor;
	private LightSensor lightSensor;
	
	public Robot(int wheelDiameter, int trackWidth, RemoteMotor leftMotor, RemoteMotor rightMotor, UltrasonicSensor wallSensor,
			     OpticalDistanceSensor leftObstacleSensor, OpticalDistanceSensor rightObstacleSensor,
			     LightSensor lightSensor)
	{
		// create a pilot to control the robot
		this.pilot = new DifferentialPilot(wheelDiameter, trackWidth, leftMotor, rightMotor);
		this.pilot.setRotateSpeed(100);
		this.pilot.setTravelSpeed(100);
		
		// store all the sensors
		this.wallSensor = wallSensor;
		this.leftObstacleSensor = leftObstacleSensor;
		this.rightObstacleSensor = rightObstacleSensor;
		this.lightSensor = lightSensor;
		this.lightSensor.setFloodlight(true);
	}
	
	public void run()
	{
        // avoid obstacles indefinitely
		while(true)
		{
            // if we should avoid an obstacle, do so
			if(shouldAvoid())
				avoid();
			else if(onTape())// if we're on tape, arc backward away from it
				this.pilot.arcBackward(-100);
			else
				this.pilot.arcForward(-900);// always arc towards the tape
			
            // sleep to give actions a while to take affect
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Avoid an obstacle
	 */
	private void avoid()
	{
        // stop moving before we arc
		this.pilot.stop();

		if(isCloseToWall())
		{
			// continue avoiding until in the clear
			while(shouldAvoid())
				avoidToRight();
		}
		else
		{
			// continue avoiding until in the clear
			while(shouldAvoid())
				avoidToLeft();
		}
	}
	
	/**
	 * Avoid an obstacle and go to the right
	 */
	private void avoidToRight()
	{
		this.pilot.arcBackward(100);
        // arc for 1200 ms
		try {
			Thread.sleep(1200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Avoid an obstacle and go to the left
	 */
	private void avoidToLeft()
	{
		this.pilot.arcBackward(-100);
        // arc for 1200 ms
		try {
			Thread.sleep(1200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @return true if the robot thinks it's facing the goal
	 */
	private boolean isFacingGoal()
	{

		return this.wallSensor.getDistance() <= Robot.farDistance;
	}
	
	/**
	 * 
	 * @return true if the robot thinks it's close to the wall
	 */
	private boolean isCloseToWall()
	{
        // if the distance of the wall sensor is less than the middle, we're close to the wall
		return this.wallSensor.getDistance() < Robot.midDist;
	}
	
	/**
	 * 
	 * @return true if the robot has an obstacle in front of it
	 */
	private boolean shouldAvoid()
	{
        // if either sensor can see an obstacle, we need to avoid it
		return this.leftObstacleSensor.getDistance() < Robot.obstacleDist || this.rightObstacleSensor.getDistance() < Robot.obstacleDist;
	}
	
	/**
	 * 
	 * @return true if the robot is currently on tape
	 */
	private boolean onTape()
	{
		return this.lightSensor.getLightValue() > groundColor;
	}
}
