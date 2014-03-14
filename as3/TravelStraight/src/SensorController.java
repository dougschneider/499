import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;


public class SensorController {

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
}
