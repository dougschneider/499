package controllers;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;

@SuppressWarnings({"deprecation"})
/**
 * Controllers class provides BangBang, P, PD, PI, and PID controller implementations.
 * 
 */
public class Controllers {
	
	/**
	 * BangBang controller implementation.
	 * 
	 * @param sensor
	 * @param leftMotor
	 * @param rightMotor
	 */
	public static void bangBang(LightSensor sensor, MotorPort leftMotor,
			MotorPort rightMotor) {
		while (true) {
			if (sensor.getLightValue() < 35) {
				leftMotor.controlMotor(0, MotorPort.FORWARD);
				rightMotor.controlMotor(40, MotorPort.FORWARD);
			} else {
				leftMotor.controlMotor(20, MotorPort.FORWARD);
				rightMotor.controlMotor(20, MotorPort.BACKWARD);
			}
			if (Button.RIGHT.isPressed())
				break;
		}
		leftMotor.controlMotor(100, MotorPort.STOP);
		rightMotor.controlMotor(100, MotorPort.STOP);
		System.exit(0);
	}

	public static void P(double Kp, LightSensor sensor, MotorPort leftMotor,
			MotorPort rightMotor) {
		int targetValue = 35;
		runPID(targetValue, Kp, 0, 0, sensor, leftMotor, rightMotor);
	}

	public static void PD(double Kp, double Kd, LightSensor sensor, MotorPort leftMotor,
			MotorPort rightMotor) {
		int targetValue = 40;
		runPID(targetValue, Kp, 0, Kd, sensor, leftMotor, rightMotor);
	}

	public static void PI(double Kp, double Ki, LightSensor sensor, MotorPort leftMotor,
			MotorPort rightMotor) {
		int targetValue = 35;
		runPID(targetValue, Kp, Ki, 0, sensor, leftMotor, rightMotor);
	}

	public static void PID(double Kp, double Ki, double Kd, LightSensor sensor, MotorPort leftMotor,
			MotorPort rightMotor) {
		int targetValue = 36;
		runPID(targetValue, Kp, Ki, Kd, sensor, leftMotor, rightMotor);
	}
	
	private static void runPID(int targetValue, double Kp, double Ki, double Kd, LightSensor sensor, MotorPort leftMotor,
			MotorPort rightMotor){
		int error = 0;
		int lastError = 0;

		int basePower = 10;

		double integral = 0;
		double derivative = 0;
		
		while (true) {
			int current = sensor.getLightValue();
			error = current - targetValue;
			System.out.println(current);

			integral = integral + error;
			derivative = error - lastError;

			int turn = (int) Math.round(Kp * error + Ki * integral + Kd
					* derivative);
			// turn /= 200;

			leftMotor.controlMotor(basePower + turn, MotorPort.FORWARD);
			rightMotor.controlMotor(basePower - turn, MotorPort.FORWARD);
			lastError = error;
			// 28/45
			if (Button.RIGHT.isPressed())
				break;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		leftMotor.controlMotor(100, MotorPort.STOP);
		rightMotor.controlMotor(100, MotorPort.STOP);
		System.exit(0);
	}
}
