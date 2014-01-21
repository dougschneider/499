import lejos.nxt.BasicMotorPort;
import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
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

		OpticalDistanceSensor distSensor = new OpticalDistanceSensor(SensorPort.S1);
		LocalizationData data = new LocalizationData();
		data.heading = Math.PI/2;
		data.x = 0;
		data.y = 0;
		int startDist = distSensor.getDistLSB();
		driveStraight();
		localize(2, data);
		int endDist = distSensor.getDistLSB();
		int travelledDist = startDist - endDist;
		int estTravelled = (int)data.y;
		System.out.println("sensor: " + travelledDist);
		System.out.println("measured: " + estTravelled);
		System.out.println("error: " + (travelledDist-estTravelled));
		MotorPort.C.controlMotor(100, MotorPort.STOP);
		MotorPort.A.controlMotor(100, MotorPort.STOP);
		Button.waitForAnyPress();
	}

	private static void driveStraight() {
		MotorPort.A.controlMotor(76, BasicMotorPort.FORWARD);
		MotorPort.C.controlMotor(70, BasicMotorPort.FORWARD);
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
