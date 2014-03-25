import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;


public class LineFollow {

	public static void main(String[] args) {
		
		Sound.beep();
		LightSensor sensor = new LightSensor(SensorPort.S1);
		sensor.setFloodlight(true);
		
		MotorPort leftMotor = MotorPort.C;
		MotorPort rightMotor = MotorPort.A;
//		bangBang(sensor, leftMotor, rightMotor);
//		P(sensor, leftMotor, rightMotor);
//		PD(sensor, leftMotor, rightMotor);
//		PI(sensor, leftMotor, rightMotor);
		PID(sensor, leftMotor, rightMotor);
	}
	
	private static void bangBang(LightSensor sensor, MotorPort leftMotor, MotorPort rightMotor)
	{
		while(true)
		{
			if(sensor.getLightValue() < 35)
			{
				leftMotor.controlMotor(0, MotorPort.FORWARD);
				rightMotor.controlMotor(40, MotorPort.FORWARD);
			}
			else
			{
				leftMotor.controlMotor(20, MotorPort.FORWARD);
				rightMotor.controlMotor(20, MotorPort.BACKWARD);
			}
			if(Button.RIGHT.isPressed())
				break;
		}
		leftMotor.controlMotor(100, MotorPort.STOP);
		rightMotor.controlMotor(100, MotorPort.STOP);
		System.exit(0);
	}
	
	private static void P(LightSensor sensor, MotorPort leftMotor, MotorPort rightMotor)
	{
		double Kp = 3;
		double Ki = 0;
		double Kd = 0;
		
		int targetValue = 35;
		int error = 0;
		int lastError = 0;
		
		int basePower = 10;
		
		double integral = 0;
		double derivative = 0;
		
		while(true)
		{
			int current = sensor.getLightValue();
			error = current - targetValue;
			System.out.println(current);
			
			integral = integral + error;
			derivative = error - lastError;
			
			int turn = (int) Math.round(Kp*error + Ki*integral + Kd*derivative);
//			turn /= 200;

			leftMotor.controlMotor(basePower + turn, MotorPort.FORWARD);
			rightMotor.controlMotor(basePower - turn, MotorPort.FORWARD);
			lastError = error;
//			28/45
			if(Button.RIGHT.isPressed())
				break;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		leftMotor.controlMotor(100, MotorPort.STOP);
		rightMotor.controlMotor(100, MotorPort.STOP);
		System.exit(0);
	}
	
	private static void PD(LightSensor sensor, MotorPort leftMotor, MotorPort rightMotor)
	{
		double Kp = 1.8;
		double Ki = 0;
		double Kd = 1.5;
		
		int targetValue = 40;
		int error = 0;
		int lastError = 0;
		
		int basePower = 10;
		
		double integral = 0;
		double derivative = 0;
		
		while(true)
		{
			int current = sensor.getLightValue();
			error = current - targetValue;
			System.out.println(current);
			
			integral = integral + error;
			derivative = error - lastError;
			
			int turn = (int) Math.round(Kp*error + Ki*integral + Kd*derivative);
//			turn /= 200;

			leftMotor.controlMotor(basePower + turn, MotorPort.FORWARD);
			rightMotor.controlMotor(basePower - turn, MotorPort.FORWARD);
			lastError = error;
//			28/45
			if(Button.RIGHT.isPressed())
				break;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		leftMotor.controlMotor(100, MotorPort.STOP);
		rightMotor.controlMotor(100, MotorPort.STOP);
		System.exit(0);
	}
	
	private static void PI(LightSensor sensor, MotorPort leftMotor, MotorPort rightMotor)
	{
		double Kp = 2;
		double Ki = 0.1;
		double Kd = 0;
		
		int targetValue = 35;
		int error = 0;
		int lastError = 0;
		
		int basePower = 10;
		
		double integral = 0;
		double derivative = 0;
		
		while(true)
		{
			int current = sensor.getLightValue();
			error = current - targetValue;
			System.out.println(current);
			
			integral = integral + error;
			derivative = error - lastError;
			
			int turn = (int) Math.round(Kp*error + Ki*integral + Kd*derivative);
//			turn /= 200;

			leftMotor.controlMotor(basePower + turn, MotorPort.FORWARD);
			rightMotor.controlMotor(basePower - turn, MotorPort.FORWARD);
			lastError = error;
//			28/45
			if(Button.RIGHT.isPressed())
				break;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		leftMotor.controlMotor(100, MotorPort.STOP);
		rightMotor.controlMotor(100, MotorPort.STOP);
		System.exit(0);
	}
	
	private static void PID(LightSensor sensor, MotorPort leftMotor, MotorPort rightMotor)
	{
		double Kp = 1.8;
		double Ki = 0.1;
		double Kd = 0.3;
		
		int targetValue = 36;
		int error = 0;
		int lastError = 0;
		
		int basePower = 10;
		
		double integral = 0;
		double derivative = 0;
		
		while(true)
		{
			int current = sensor.getLightValue();
			error = current - targetValue;
			System.out.println(current);
			
			integral = integral + error;
			derivative = error - lastError;
			
			int turn = (int) Math.round(Kp*error + Ki*integral + Kd*derivative);
//			turn /= 200;

			leftMotor.controlMotor(basePower + turn, MotorPort.FORWARD);
			rightMotor.controlMotor(basePower - turn, MotorPort.FORWARD);
			lastError = error;
//			28/45
			if(Button.RIGHT.isPressed())
				break;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		leftMotor.controlMotor(100, MotorPort.STOP);
		rightMotor.controlMotor(100, MotorPort.STOP);
		System.exit(0);
	}

}
