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
//		UltrasonicSensor xSensor = new UltrasonicSensor(SensorPort.S2);
//		OpticalDistanceSensor ySensor = new OpticalDistanceSensor(SensorPort.S3);
		//measureXYError(100, xSensor, ySensor, data);
		measureRotationalErrorWithSensor(100);
		measureRotationalErrorWithSensor(100);
		measureRotationalErrorWithSensor(100);
		measureRotationalErrorWithSensor(90);
		measureRotationalErrorWithSensor(90);
		measureRotationalErrorWithSensor(90);
		measureRotationalErrorWithSensor(80);
		measureRotationalErrorWithSensor(80);
		measureRotationalErrorWithSensor(80);
		measureRotationalErrorWithSensor(70);
		measureRotationalErrorWithSensor(70);
		measureRotationalErrorWithSensor(70);
//		measureRotationalError(100, data);
//		measureRotationalError(100, data);
//		measureRotationalError(100, data);
//		measureRotationalError(90, data);
//		measureRotationalError(90, data);
//		measureRotationalError(90, data);
//		measureRotationalError(80, data);
//		measureRotationalError(80, data);
//		measureRotationalError(80, data);
//		measureRotationalError(70, data);
//		measureRotationalError(70, data);
//		measureRotationalError(70, data);
	}
	
	public static void measureXYError(int power, UltrasonicSensor xSensor,
			OpticalDistanceSensor ySensor, LocalizationData data)	{	
		
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
		System.out.println("yerror: " + (yTravelledDist - yEstTravelled));
		Button.waitForAnyPress();
		System.out.println("xsensor: " + xTravelledDist);
		System.out.println("xmeasured: " + xEstTravelled);
		System.out.println("xerror: " + (xTravelledDist - xEstTravelled));
		Button.waitForAnyPress();
	}
	
	public static void measureRotationalError(int pow, LocalizationData data)
	{
		Delay.msDelay(1000);
		data.heading = Math.PI/2;
		data.x = 0;
		data.y = 0;
		MotorPort.A.controlMotor(pow, BasicMotorPort.BACKWARD);
		MotorPort.C.controlMotor(pow, BasicMotorPort.FORWARD);
		localize(1, data);
		MotorPort.C.controlMotor(100, MotorPort.STOP);
		MotorPort.A.controlMotor(100, MotorPort.STOP);
		System.out.println("pow" + pow);
		System.out.println("heading: " + Math.toDegrees(data.heading));
		Button.waitForAnyPress();
	}
	
	public static void measureRotationalErrorWithSensor(int pow)
	{
		Delay.msDelay(1000);
		UltrasonicSensor rightSensor = new UltrasonicSensor(SensorPort.S2);
		OpticalDistanceSensor leftSensor = new OpticalDistanceSensor(SensorPort.S3);
		LocalizationData sensorData = new LocalizationData();
		LocalizationData tachData = new LocalizationData();
		sensorData.heading = Math.PI/2;
		sensorData.x = 0;
		sensorData.y = 0;
		tachData.heading = Math.PI/2;
		tachData.x = 0;
		tachData.y = 0;
		MotorPort.A.controlMotor(pow, BasicMotorPort.FORWARD);
		MotorPort.C.controlMotor(pow, BasicMotorPort.FORWARD);
		localizeWithSensors(1, sensorData, tachData, rightSensor, leftSensor);
		MotorPort.C.controlMotor(100, MotorPort.STOP);
		MotorPort.A.controlMotor(100, MotorPort.STOP);
		System.out.println("pow" + pow);
		System.out.println("sensor: " + Math.toDegrees(sensorData.heading));
		System.out.println("tach: " + Math.toDegrees(tachData.heading));
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
	
	public static void localizeWithSensors(int seconds, LocalizationData sensorData,
			LocalizationData tachData, UltrasonicSensor rightSensor,
			OpticalDistanceSensor leftSensor)
	{
		int maxDelay = seconds*250;
		int currentDelay = 0;
		int delta = 10;
		
		double distancePerTick = (Math.PI*wheelRadius)/180;
		double ticksPerRotation = (2*Math.PI*robotRadius)/distancePerTick;
		double radiansPerTick = (2*Math.PI)/ticksPerRotation;
		
		int rightSensorOld = rightSensor.getDistance() * 10;
		int leftSensorOld = leftSensor.getDistLSB();
		System.out.println(rightSensor.getDistance()*10);
		while(currentDelay < maxDelay)
		{
			currentDelay += delta;
			Delay.msDelay(delta);
			
			// update sensor data
			int rightSensorNew = rightSensor.getDistance()*10;
			int rightSensorDiff = rightSensorNew - rightSensorOld;
			rightSensorOld = rightSensorNew;
//			System.out.println("old: " + leftSensorDist);
			int leftSensorNew = leftSensor.getDistLSB();
			int leftSensorDiff = leftSensorNew - leftSensorOld;
			leftSensorOld = leftSensorNew;
			System.out.println(rightSensor.getDistance()*10);
//			System.out.println("new: " + leftSensorDist);
//			System.out.println(rightSensorDist);
//			System.out.println(leftSensorDist);
			double deltaSensorHeading = (((rightSensorDiff/distancePerTick)-(leftSensorDiff/distancePerTick))*radiansPerTick)/2;
			System.out.println("right sticks: " + (rightSensorDiff/distancePerTick));
//			System.out.println("delta: " + deltaSensorHeading);
			double deltaSensorDistance = (rightSensorDiff+leftSensorDiff)/2;
			double deltaSensorX = deltaSensorDistance*Math.cos(sensorData.heading);
			double deltaSensorY = deltaSensorDistance*Math.sin(sensorData.heading);
			sensorData.x = sensorData.x + deltaSensorX;
			sensorData.y = sensorData.y + deltaSensorY;
			sensorData.heading = sensorData.heading + deltaSensorHeading;
			
			// update tach data
			int leftTick = MotorPort.C.getTachoCount();
			int rightTick = MotorPort.A.getTachoCount();
			MotorPort.A.resetTachoCount();
			MotorPort.C.resetTachoCount();
			double deltaTachHeading = ((rightTick-leftTick)*radiansPerTick)/2;
			System.out.println("right ticks: " + rightTick);
			double deltaTachDistance = ((leftTick+rightTick)*distancePerTick)/2;
			double deltaTachX = deltaTachDistance*Math.cos(tachData.heading);
			double deltaTachY = deltaTachDistance*Math.sin(tachData.heading);
			tachData.x = tachData.x + deltaTachX;
			tachData.y = tachData.y + deltaTachY;
			tachData.heading = tachData.heading + deltaTachHeading;
		}
	}
}

class LocalizationData
{
	public double heading;
	public double x;
	public double y;
}
