package behaviours;

import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

public class PIDBehaviour implements Behavior {

	// PID controller values
	private static final double K_P = 2.0;
	private static final double K_I = 0.05;
	private static final double K_D = 0.0;
	// clear the integral if we're within I_MARGIN of target
	private static final double I_MARGIN = 5;

	// configuration values
	private boolean isConfigured = false;
	private int regularTrack = 52;
	private int specialTrack = 45;
	private int inSpecialWhenBelow = (specialTrack+regularTrack)/2;
	private int specialTarget = 35;
	private int regularTarget = 40;
	

	private LightSensor lightSensor;
	private LightSensor targetSensor;
	private MotorPort rightMotor;
	private MotorPort leftMotor;
	
	int error = 0;
	int lastError = 0;
	int basePower = 5;
	private static final int MAX_BASE_POWER = 50;
	private static final int BASE_POWER_STEP = 1;
	

	double integral = 0;
	double derivative = 0;

	public PIDBehaviour(LightSensor lightSensor, LightSensor targetSensor,
			MotorPort rightMotor, MotorPort leftMotor) {
		this.lightSensor = lightSensor;
		this.targetSensor = targetSensor;

		this.rightMotor = rightMotor;
		this.leftMotor = leftMotor;
	}

	public void configure() {
		lightSensor.getLightValue();
		targetSensor.getLightValue();
		Delay.msDelay(2000);
		// TODO configure target values
		isConfigured = true;
	}

	@Override
	public boolean takeControl() {
		if (!isConfigured) {
			System.out.println("PID behavior not configured.");
			return false;
		}
		// by default use the PID controller
		return true;
	}

	@Override
	public void action() {
		// ramp up speed over time
		if (basePower < MAX_BASE_POWER) {
			basePower += BASE_POWER_STEP;
		}
		int targetValue = targetSensor.getLightValue();
//		System.out.println(targetValue);
		if (targetValue < inSpecialWhenBelow)
			targetValue = specialTarget;
		else
			targetValue = regularTarget;
		int current = lightSensor.getLightValue();
//		System.out.println(current);
		error = current - targetValue;
		
		if (Math.abs(error) < I_MARGIN) {
			integral = 0;
		}
		integral = integral + error;
		derivative = error - lastError;
//		System.out.println(integral);

		// find the turn based on the error and k values
		int turn = (int) Math.round(K_P * error + K_I * integral + K_D
				* derivative);
		System.out.println(turn);

		// set the motors power based on the error
		leftMotor.controlMotor(basePower-turn, MotorPort.FORWARD);
		rightMotor.controlMotor(basePower+turn, MotorPort.FORWARD);
		// try to be consistent with timing
		Delay.msDelay(50);
		
		lastError = error;
	}

	@Override
	public void suppress() {
		// does nothing
	}

}
