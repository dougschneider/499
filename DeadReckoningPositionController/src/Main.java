import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.util.Delay;


public class Main {

	private static double wheelRadius = 28;// millimeters
	private static double robotRadius = 56;// millimeters
	
	public static void main(String[] args) {
		int[][] commands = {{80, 60, 2},
				             {60, 60, 1},
				             {-50, 80, 2}};
//		int[][] commands = {{80, 80, 2}, {75, 80, 2}, {80, 80, 2}};
		followCommands(commands);
		MotorPort.C.controlMotor(100, MotorPort.STOP);
		MotorPort.A.controlMotor(100, MotorPort.STOP);
		Button.waitForAnyPress();
	}
	
	public static void followCommands(int[][] commands)
	{
		LocalizationData data = new LocalizationData();
		data.heading = Math.PI/2;
		data.x = 0;
		data.y = 0;
		for(int i = 0; i < commands.length; ++i)
		{
			int leftPower = commands[i][0];
			int leftMode = MotorPort.FORWARD;
			if(leftPower < 0)
			{
				leftPower *= -1;
				leftMode = MotorPort.BACKWARD;
			}
			int rightPower = commands[i][1];
			int rightMode = MotorPort.FORWARD;
			if(rightPower < 0)
			{
				rightPower *= -1;
				rightMode = MotorPort.BACKWARD;
			}
			MotorPort.C.controlMotor(leftPower, leftMode);
			MotorPort.A.controlMotor(rightPower, rightMode);
			localize(commands[i][2], data);
		}
	}
	
	public static void localize(int seconds, LocalizationData data)
	{
		int maxDelay = seconds*1000;
		int currentDelay = 0;
		int delta = 10;
		double distancePerTick = (Math.PI*wheelRadius)/180;
		double ticksPerRotation = (2*Math.PI*robotRadius)/distancePerTick;
		double radiansPerTick = (2*Math.PI)/ticksPerRotation;
		while(currentDelay < maxDelay)
		{
			currentDelay += delta;
			Delay.msDelay(delta);
			int leftTick = MotorPort.C.getTachoCount();
			int rightTick = MotorPort.A.getTachoCount();
			MotorPort.A.resetTachoCount();
			MotorPort.C.resetTachoCount();
			double deltaHeading = ((rightTick-leftTick)*radiansPerTick)/2;
			double deltaDistance = ((leftTick+rightTick)*distancePerTick)/2;
			double deltaX = deltaDistance*Math.cos(data.heading);
			double deltaY = deltaDistance*Math.sin(data.heading);
			data.x = data.x + deltaX;
			data.y = data.y + deltaY;
			data.heading = data.heading + deltaHeading;

		}
		System.out.println((int)data.x/10 + " " + (int) data.y/10 + " " + Math.toDegrees(data.heading));
	}
}

class LocalizationData
{
	public double heading;
	public double x;
	public double y;
}