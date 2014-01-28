import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.util.Delay;


public class Main {
	
	// scaling factor used to adjust the sensor input to make
	// the braitenburg bot behaviour more apparent.
	private static int scalingFactor = 30; 

	public static void main(String[] args){
		LightSensor rightSensor = new LightSensor(SensorPort.S1);
		LightSensor leftSensor = new LightSensor(SensorPort.S4);
		
		// turn off the flood lights to prevent unexpected behaviour
		rightSensor.setFloodlight(false);
		leftSensor.setFloodlight(false);
		
		// uncomment the line for the braitenburg bot that you want to run.
		coward(rightSensor, leftSensor);
//		aggressive(rightSensor, leftSensor);
//		love(rightSensor, leftSensor);
//		explore(rightSensor, leftSensor);
	}
	
	/**
	 * Right sensor positively affects right wheel
	 * Left sensor positively affects left wheel
	 */
	public static void coward(LightSensor rightSensor, LightSensor leftSensor)
	{
		int maxTime = 5000;
		int currentTime = 0;
		int delta = 10;
		while(currentTime < maxTime)
		{
			currentTime += delta;
			// we use a scaling factor to make the behavior more apparent
			int rightPower = rightSensor.getLightValue() + scalingFactor;
			int leftPower = leftSensor.getLightValue() + scalingFactor;
			MotorPort.C.controlMotor(leftPower, MotorPort.FORWARD);
			MotorPort.A.controlMotor(rightPower, MotorPort.FORWARD);
			Delay.msDelay(delta);
		}
		
	}

	/**
	 * Right sensor positively affects left wheel
	 * Left sensor positively affects right wheel
	 */
	public static void aggressive(LightSensor rightSensor, LightSensor leftSensor)
	{
		int maxTime = 5000;
		int currentTime = 0;
		int delta = 10;
		while(currentTime < maxTime)
		{
			currentTime += delta;
			// we use a scaling factor to make the behavior more apparent
			int rightPower = leftSensor.getLightValue() + scalingFactor;
			int leftPower = rightSensor.getLightValue() + scalingFactor;
			MotorPort.C.controlMotor(leftPower, MotorPort.FORWARD);
			MotorPort.A.controlMotor(rightPower, MotorPort.FORWARD);
			Delay.msDelay(delta);
		}
		
	}

	/**
	 * Right sensor negatively affects right wheel
	 * Left sensor negatively affects left wheel
	 */
	public static void love(LightSensor rightSensor, LightSensor leftSensor)
	{
		int maxTime = 5000;
		int currentTime = 0;
		int delta = 10;
		while(currentTime < maxTime)
		{
			currentTime += delta;
			// we use a scaling factor to make the behavior more apparent
			int rightPower = Math.min(100, 100 + scalingFactor - rightSensor.getLightValue());
			int leftPower = Math.min(100, 100 + scalingFactor - leftSensor.getLightValue());
			MotorPort.C.controlMotor(leftPower, MotorPort.FORWARD);
			MotorPort.A.controlMotor(rightPower, MotorPort.FORWARD);
			System.out.println(rightPower);
			Delay.msDelay(delta);
		}
		
	}

	/**
	 * Right sensor negatively affects left wheel
	 * Left sensor negatively affects right wheel
	 */
	public static void explore(LightSensor rightSensor, LightSensor leftSensor)
	{
		int maxTime = 5000;
		int currentTime = 0;
		int delta = 10;
		while(currentTime < maxTime)
		{
			currentTime += delta;
			// we use a scaling factor to make the behavior more apparent
			int rightPower = Math.min(100, 100 + scalingFactor - leftSensor.getLightValue());
			int leftPower = Math.min(100, 100 + scalingFactor - rightSensor.getLightValue());
			MotorPort.C.controlMotor(leftPower, MotorPort.FORWARD);
			MotorPort.A.controlMotor(rightPower, MotorPort.FORWARD);
			Delay.msDelay(delta);
		}
		
	}

}
