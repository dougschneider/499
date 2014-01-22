import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.util.Delay;


public class Main {

	public static void main(String[] args){
		LightSensor rightSensor = new LightSensor(SensorPort.S1);
		LightSensor leftSensor = new LightSensor(SensorPort.S4);
		rightSensor.setFloodlight(false);
		leftSensor.setFloodlight(false);
		coward(rightSensor, leftSensor);
	}
	
	public static void coward(LightSensor rightSensor, LightSensor leftSensor)
	{
		int maxTime = 2000;
		int currentTime = 0;
		int delta = 10;
		while(currentTime < maxTime)
		{
			currentTime += delta;
			int rightPower = rightSensor.getLightValue();
			int leftPower = leftSensor.getLightValue();
			MotorPort.C.controlMotor(leftPower, MotorPort.FORWARD);
			MotorPort.A.controlMotor(rightPower, MotorPort.FORWARD);
			Delay.msDelay(delta);
		}
		
	}
	
	public static void aggressive(LightSensor rightSensor, LightSensor leftSensor)
	{
		int maxTime = 2000;
		int currentTime = 0;
		int delta = 10;
		while(currentTime < maxTime)
		{
			currentTime += delta;
			int rightPower = leftSensor.getLightValue();
			int leftPower = rightSensor.getLightValue();
			MotorPort.C.controlMotor(leftPower, MotorPort.FORWARD);
			MotorPort.A.controlMotor(rightPower, MotorPort.FORWARD);
			Delay.msDelay(delta);
		}
		
	}
	
	public static void love(LightSensor rightSensor, LightSensor leftSensor)
	{
		int maxTime = 2000;
		int currentTime = 0;
		int delta = 10;
		while(currentTime < maxTime)
		{
			currentTime += delta;
			int rightPower = 100 - rightSensor.getLightValue();
			int leftPower = 100 - leftSensor.getLightValue();
			MotorPort.C.controlMotor(leftPower, MotorPort.FORWARD);
			MotorPort.A.controlMotor(rightPower, MotorPort.FORWARD);
			Delay.msDelay(delta);
		}
		
	}
	
	public static void explore(LightSensor rightSensor, LightSensor leftSensor)
	{
		int maxTime = 2000;
		int currentTime = 0;
		int delta = 10;
		while(currentTime < maxTime)
		{
			currentTime += delta;
			int rightPower = 100 - leftSensor.getLightValue();
			int leftPower = 100 - rightSensor.getLightValue();
			MotorPort.C.controlMotor(leftPower, MotorPort.FORWARD);
			MotorPort.A.controlMotor(rightPower, MotorPort.FORWARD);
			Delay.msDelay(delta);
		}
		
	}

}
