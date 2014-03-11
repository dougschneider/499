import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.OpticalDistanceSensor;


public class SensorController {

	public static int getFrontDist()
	{
		return new OpticalDistanceSensor(SensorPort.S3).getDistance();
	}

	public static int getBackDist()
	{
		return new OpticalDistanceSensor(SensorPort.S2).getDistance();
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
