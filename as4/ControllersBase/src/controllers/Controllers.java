package controllers;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;

@SuppressWarnings({"deprecation"})
/**
 * Controllers class provides BangBang, P, PD, PI, and PID controller implementations.
 * 
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

	/**
	 * P controller implementation
	 * @param Kp
	 * @param Ki
	 * @param Kd
	 * @param sensor
	 * @param leftMotor
	 * @param rightMotor
	 */
	public static void P(double Kp, double Ki, double Kd, LightSensor sensor, MotorPort leftMotor,
			MotorPort rightMotor) {
//		double Kp = 3;
//		double Ki = 0;
//		double Kd = 0;

		int targetValue = 35;
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		leftMotor.controlMotor(100, MotorPort.STOP);
		rightMotor.controlMotor(100, MotorPort.STOP);
		System.exit(0);
	}

	public static void PD(double Kp, double Ki, double Kd, LightSensor sensor, MotorPort leftMotor,
			MotorPort rightMotor) {
//		double Kp = 1.8;
//		double Ki = 0;
//		double Kd = 1.5;

		int targetValue = 40;
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		leftMotor.controlMotor(100, MotorPort.STOP);
		rightMotor.controlMotor(100, MotorPort.STOP);
		System.exit(0);
	}

	public static void PI(double Kp, double Ki, double Kd, LightSensor sensor, MotorPort leftMotor,
			MotorPort rightMotor) {
//		double Kp = 2;
//		double Ki = 0.1;
//		double Kd = 0;

		int targetValue = 35;
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		leftMotor.controlMotor(100, MotorPort.STOP);
		rightMotor.controlMotor(100, MotorPort.STOP);
		System.exit(0);
	}

	public static void PID(double Kp, double Ki, double Kd, LightSensor sensor, MotorPort leftMotor,
			MotorPort rightMotor) {
//		double Kp = 1.8;
//		double Ki = 0.1;
//		double Kd = 0.3;

		int targetValue = 36;
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		leftMotor.controlMotor(100, MotorPort.STOP);
		rightMotor.controlMotor(100, MotorPort.STOP);
		System.exit(0);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		leftMotor.controlMotor(100, MotorPort.STOP);
		rightMotor.controlMotor(100, MotorPort.STOP);
		System.exit(0);
	}
}
