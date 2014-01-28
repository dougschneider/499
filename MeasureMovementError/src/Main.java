import lejos.nxt.BasicMotorPort;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;


public class Main {

	private static double wheelRadius = 28;// millimeters
	private static double robotRadius = 56;// millimeters

	public static void main(String[] args) {
		// uncomment the following to perform a linear error measurement
		UltrasonicSensor xSensor = new UltrasonicSensor(SensorPort.S2);
		// NOTE: The optical distance sensor class is taken from
		// mindsensors.com
		OpticalDistanceSensor ySensor = new OpticalDistanceSensor(SensorPort.S3);
		LocalizationData data = new LocalizationData();
		measureXYError(100, xSensor, ySensor, data);
		measureXYError(100, xSensor, ySensor, data);
		measureXYError(100, xSensor, ySensor, data);
		measureXYError(90, xSensor, ySensor, data);
		measureXYError(90, xSensor, ySensor, data);
		measureXYError(90, xSensor, ySensor, data);
		measureXYError(80, xSensor, ySensor, data);
		measureXYError(80, xSensor, ySensor, data);
		measureXYError(80, xSensor, ySensor, data);
		measureXYError(70, xSensor, ySensor, data);
		measureXYError(70, xSensor, ySensor, data);
		measureXYError(70, xSensor, ySensor, data);
		
		// uncomment the following to perform a rotational error measurement
		// This is used to drive in a straight line and measure the error
		// with the distance sensors.
//		measureRotationalErrorWithSensor(100);
//		measureRotationalErrorWithSensor(100);
//		measureRotationalErrorWithSensor(100);
//		measureRotationalErrorWithSensor(90);
//		measureRotationalErrorWithSensor(90);
//		measureRotationalErrorWithSensor(90);
//		measureRotationalErrorWithSensor(80);
//		measureRotationalErrorWithSensor(80);
//		measureRotationalErrorWithSensor(80);
//		measureRotationalErrorWithSensor(70);
//		measureRotationalErrorWithSensor(70);
//		measureRotationalErrorWithSensor(70);
		
		// uncomment the following to perform a rotational error measurement
		// This is used to rotate in circles for measuring with a protractor
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
	
	/**
	 * Measure the XY error for the given power.
	 * 
	 * @param power The power 0<=power<=100 to apply to the wheels to
	 * 				 measure the error for.
	 * @param xSensor The sensor pointing in the x direction.
	 * @param ySensor The sensor pointing in the y direction.
	 * @param data The data to populate.
	 */
	public static void measureXYError(int power, UltrasonicSensor xSensor,
			OpticalDistanceSensor ySensor, LocalizationData data)	{	
		
		// initialize the data
		data.heading = Math.PI/2;
		data.x = 0;
		data.y = 0;
		// get the starting location
		int startyDist = getYDist(ySensor);
		int startxDist = getXDist(xSensor);
		
		// move forward and measure the movement
		MotorPort.A.controlMotor(power, BasicMotorPort.FORWARD);
		MotorPort.C.controlMotor(power, BasicMotorPort.FORWARD);
		localize(1, data);
		// stop the motors
		MotorPort.C.controlMotor(100, MotorPort.STOP);
		MotorPort.A.controlMotor(100, MotorPort.STOP);
		Delay.msDelay(1000);
		
		// get the ending location
		int endyDist = getYDist(ySensor);
		int endxDist = getXDist(xSensor);
		
		// compute the changes in location from the sensors
		int yTravelledDist = startyDist - endyDist;
		int xTravelledDist = startxDist - endxDist;
		
		// print out the measured data
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
		LCD.clear();
	}
	
	/**
	 * Rotate the robot with the specified power. The robot is rotated to the
	 * left.
	 * @param pow
	 * @param data
	 */
	public static void measureRotationalError(int pow, LocalizationData data)
	{
		Delay.msDelay(1000);
		// initialize the data
		data.heading = Math.PI/2;
		data.x = 0;
		data.y = 0;
		// start rotating and measure the heading change
		MotorPort.A.controlMotor(pow, BasicMotorPort.BACKWARD);
		MotorPort.C.controlMotor(pow, BasicMotorPort.FORWARD);
		localize(1, data);
		
		// stop the motors
		MotorPort.C.controlMotor(100, MotorPort.STOP);
		MotorPort.A.controlMotor(100, MotorPort.STOP);
		
		// print the results
		System.out.println("pow" + pow);
		System.out.println("heading: " + Math.toDegrees(data.heading));
		Button.waitForAnyPress();
	}
	
	/**
	 * Drive the robot in a straight line with the given power. The distance
	 * sensors measure the change in heading.
	 * 
	 * @param pow The power to move forward with.
	 */
	public static void measureRotationalErrorWithSensor(int pow)
	{
		Delay.msDelay(1000);
		// set up the distance sensors
		UltrasonicSensor rightSensor = new UltrasonicSensor(SensorPort.S2);
		OpticalDistanceSensor leftSensor = new OpticalDistanceSensor(SensorPort.S3);
		
		// initialize the localization data
		LocalizationData sensorData = new LocalizationData();
		LocalizationData tachData = new LocalizationData();
		sensorData.heading = Math.PI/2;
		sensorData.x = 0;
		sensorData.y = 0;
		tachData.heading = Math.PI/2;
		tachData.x = 0;
		tachData.y = 0;
		
		// start moving forward, and measure the change in heading
		MotorPort.A.controlMotor(pow, BasicMotorPort.FORWARD);
		MotorPort.C.controlMotor(pow, BasicMotorPort.FORWARD);
		localizeWithSensors(1, sensorData, tachData, rightSensor, leftSensor);
		
		// stop the motors
		MotorPort.C.controlMotor(100, MotorPort.STOP);
		MotorPort.A.controlMotor(100, MotorPort.STOP);
		
		// print out the results
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
	}
	
	public static void localizeWithSensors(int seconds, LocalizationData sensorData,
			LocalizationData tachData, UltrasonicSensor rightSensor,
			OpticalDistanceSensor leftSensor)
	{
		int maxDelay = seconds*1000;
		int currentDelay = 0;
		int delta = 10;
		
		// constants used in calculation
		double distancePerTick = (Math.PI*wheelRadius)/180;
		double ticksPerRotation = (2*Math.PI*robotRadius)/distancePerTick;
		double radiansPerTick = (2*Math.PI)/ticksPerRotation;
		
		// get the starting measurements
		int rightSensorOld = rightSensor.getDistance() * 10;
		int leftSensorOld = leftSensor.getDistLSB();
		while(currentDelay < maxDelay)
		{
			currentDelay += delta;
			Delay.msDelay(delta);
			
			// sensor measurements
			// calculate the delta for each sensor
			int rightSensorNew = rightSensor.getDistance()*10;
			int rightSensorDiff = rightSensorNew - rightSensorOld;
			rightSensorOld = rightSensorNew;
			int leftSensorNew = leftSensor.getDistLSB();
			int leftSensorDiff = leftSensorNew - leftSensorOld;
			leftSensorOld = leftSensorNew;
			
			// calculate the delta x, y, and heading
			double deltaSensorHeading = (((rightSensorDiff/distancePerTick)-(leftSensorDiff/distancePerTick))*radiansPerTick)/2;
			double deltaSensorDistance = (rightSensorDiff+leftSensorDiff)/2;
			double deltaSensorX = deltaSensorDistance*Math.cos(sensorData.heading);
			double deltaSensorY = deltaSensorDistance*Math.sin(sensorData.heading);
			
			// update the current x, y, and heading
			sensorData.x = sensorData.x + deltaSensorX;
			sensorData.y = sensorData.y + deltaSensorY;
			sensorData.heading = sensorData.heading + deltaSensorHeading;
			
			// tachometer measurements
			// read the tachometer reading
			int leftTick = MotorPort.C.getTachoCount();
			int rightTick = MotorPort.A.getTachoCount();
			
			// reset the tachometers
			MotorPort.A.resetTachoCount();
			MotorPort.C.resetTachoCount();
			
			// compute the deltax, deltay, and deltaheading
			double deltaTachHeading = ((rightTick-leftTick)*radiansPerTick)/2;
			double deltaTachDistance = ((leftTick+rightTick)*distancePerTick)/2;
			double deltaTachX = deltaTachDistance*Math.cos(tachData.heading);
			double deltaTachY = deltaTachDistance*Math.sin(tachData.heading);
			
			// increment the total change
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
