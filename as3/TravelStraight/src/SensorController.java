import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;


public class SensorController {
	
	private static DifferentialPilot pilot = null;

	public static int getFrontDist()
	{
		int distance = new UltrasonicSensor(SensorPort.S3).getDistance();
		System.out.println("Front: " + distance);
		return distance;
	}

	public static int getBackDist()
	{
		int distance = new UltrasonicSensor(SensorPort.S2).getDistance();
		System.out.println("Back: " + distance);
		return distance;
	}
	
	public static int getLightValue()
	{
		return new LightSensor(SensorPort.S1).getLightValue();
	}
	
	public static void controlRightMotor(int power, int mode)
	{
		MotorPort.A.controlMotor(power, mode);
	}
	
	public static void controlLeftMotor(int power, int mode)
	{
		MotorPort.C.controlMotor(power, mode);
	}
	
	public static void rotateLeft()
	{
		getPilot().rotate(45.3);
	}
	
	public static void rotateRight()
	{
    	getPilot().rotate(-45);
	}
	
	public static void goStraight()
	{
    	getPilot().travel(40, false);
	}
	
	public static DifferentialPilot getPilot()
	{
		if(pilot == null)
		{
			pilot = new DifferentialPilot(56, 120, Motor.C, Motor.A);
			pilot.setRotateSpeed(60);
			pilot.setTravelSpeed(120);
		}
		
		return pilot;
	}
}
