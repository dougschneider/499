import lejos.nxt.BasicMotorPort;
import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.SoundSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;


public class Main {

	private static double wheelRadius = 28;// millimeters
	private static double robotRadius = 56;// millimeters

	public static void main(String[] args) {
//		OpticalDistanceSensor distSensor = new OpticalDistanceSensor(SensorPort.S1);
//		while (true) {
//			lejos.nxt.LCD.clear();
//			System.out.println(distSensor.getDistLSB());
//			Delay.msDelay(100);
//		}

		UltrasonicSensor xSensor = new UltrasonicSensor(SensorPort.S2);
		OpticalDistanceSensor ySensor = new OpticalDistanceSensor(SensorPort.S3);
		LocalizationData data = new LocalizationData();
		data.heading = Math.PI/2;
		data.x = 0;
		data.y = 0;
		int startyDist = getYDist(ySensor);
		int startxDist = getXDist(xSensor);
		MotorPort.A.controlMotor(70, BasicMotorPort.FORWARD);
		MotorPort.C.controlMotor(70, BasicMotorPort.FORWARD);
		localize(1, data);
		MotorPort.C.controlMotor(100, MotorPort.STOP);
		MotorPort.A.controlMotor(100, MotorPort.STOP);
		Delay.msDelay(1000);
		int endyDist = getYDist(ySensor);
		int endxDist = getXDist(xSensor);
		int yTravelledDist = startyDist - endyDist;
		int xTravelledDist = startxDist - endxDist;
		double yEstTravelled = data.y;
		double xEstTravelled = data.x;
		System.out.println("ysensor: " + yTravelledDist);
		System.out.println("ymeasured: " + yEstTravelled);
		System.out.println("yerror: " + (yTravelledDist-yEstTravelled));
		Button.waitForAnyPress();
		System.out.println("xsensor: " + xTravelledDist);
		System.out.println("xmeasured: " + xEstTravelled);
		System.out.println("xerror: " + (xTravelledDist-xEstTravelled));
		Button.waitForAnyPress();
	}
	
	public static int getYDist(OpticalDistanceSensor sensor) {
		// adjust for sensor being ahead of wheel axis
		return sensor.getDistLSB() + 50;
	}
	
	public static int getXDist(UltrasonicSensor sensor) {
		// multiply to convert cm to mm
		// add constant to account for distance from robot centre
		return sensor.getDistance()*10 + 90;
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
	}
}

class LocalizationData
{
	public double heading;
	public double x;
	public double y;
}
