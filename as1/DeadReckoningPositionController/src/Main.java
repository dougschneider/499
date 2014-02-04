import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.util.Delay;


public class Main {

	private static double wheelRadius = 28;// millimeters
	private static double robotRadius = 56;// millimeters
	
	public static void main(String[] args) {
		// the commands to run (left power, right power, time)
		int[][] commands = {{-75, -85, 1},
				             {-85, -75, 1},
				             {-70, 70, 1}};
		followCommands(commands);
		
		// stop the motors and wait for button press so we can
		// read the values from the screen
		MotorPort.C.controlMotor(100, MotorPort.STOP);
		MotorPort.A.controlMotor(100, MotorPort.STOP);
		Button.waitForAnyPress();
	}
	
	/**
	 * Follow the input sense of commands and print out the final
	 * X, Y, and heading.
	 * 
	 * commands is a list of int lists. Each nested list is a command.
	 * A command consists of a left power, right power, and time. Negative
	 * powers are used to reverse a wheel.
	 */
	public static void followCommands(int[][] commands)
	{
		// initialize the starting location data
		LocalizationData data = new LocalizationData();
		data.heading = Math.PI/2;
		data.x = 0;
		data.y = 0;
		
		// follow each command
		for(int i = 0; i < commands.length; ++i)
		{
			// set the power and the direction for the left
			// motor
			int leftPower = commands[i][0];
			int leftMode = MotorPort.FORWARD;
			if(leftPower < 0)
			{
				leftPower *= -1;
				leftMode = MotorPort.BACKWARD;
			}
			
			// set the power and the direction for the right
			// motor
			int rightPower = commands[i][1];
			int rightMode = MotorPort.FORWARD;
			if(rightPower < 0)
			{
				rightPower *= -1;
				rightMode = MotorPort.BACKWARD;
			}
			
			// run the motors
			MotorPort.C.controlMotor(leftPower, leftMode);
			MotorPort.A.controlMotor(rightPower, rightMode);
			
			// run for the specified time, and localize the new location
			localize(commands[i][2], data);
		}
	}
	
	/**
	 * Compute the location changes over the specified amount of time.
	 * The computed changes are stored in the input data parameter.
	 */
	public static void localize(int seconds, LocalizationData data)
	{
		int maxDelay = seconds*1000;
		int currentDelay = 0;
		int delta = 10;
		
		// constants used to calculate the travelled distance
		double distancePerTick = (Math.PI*wheelRadius)/180;
		double ticksPerRotation = (2*Math.PI*robotRadius)/distancePerTick;
		double radiansPerTick = (2*Math.PI)/ticksPerRotation;
		
		// run until the time is up
		while(currentDelay < maxDelay)
		{
			currentDelay += delta;
			Delay.msDelay(delta);
			
			// read the tachometer reading
			int leftTick = MotorPort.C.getTachoCount();
			int rightTick = MotorPort.A.getTachoCount();
			
			// reset the tachometers
			MotorPort.A.resetTachoCount();
			MotorPort.C.resetTachoCount();
			
			// compute the deltax, deltay, and deltaheading
			double deltaHeading = ((rightTick-leftTick)*radiansPerTick)/2;
			double deltaDistance = ((leftTick+rightTick)*distancePerTick)/2;
			double deltaX = deltaDistance*Math.cos(data.heading);
			double deltaY = deltaDistance*Math.sin(data.heading);
			
			// increment the total change
			data.x = data.x + deltaX;
			data.y = data.y + deltaY;
			data.heading = data.heading + deltaHeading;
		}
		// output the final x location(in centimeters), y location(in centimeters),
		// and heading(in degrees)
		System.out.println(data.x/10.0 + " " + data.y/10.0 + " " + Math.toDegrees(data.heading));
	}
}

class LocalizationData
{
	public double heading;
	public double x;
	public double y;
}