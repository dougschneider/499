package behaviours;

import lejos.nxt.MotorPort;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import main.RobotInteractionMembers;

/**
 * Control the robot using a PID controller to follow the outside line.
 */
public class PIDBehaviour implements Behavior {

	// PID controller values
	private static final double SPECIAL_K_P = 1.8;
	private static final double SPECIAL_K_I = 0.1;
	private static final double SPECIAL_K_D = 0.001;
	
	private static final double NORMAL_K_P = 1.8;
	private static final double NORMAL_K_I = 0.1;
	private static final double NORMAL_K_D = 0.001;

	// PID controller internal stuff
	int error = 0;
	int lastError = 0;
	int basePower = 5;
	double integral = 0;
	double derivative = 0;

	// configuration values
	private boolean isConfigured = false;
	private int regularTrack = -1;
	private int specialTrack = -1;
	private int inSpecialWhenBelow = -1;
	private int specialTarget = -1;
	private int regularTarget = -1;

	// sensors
	private RobotInteractionMembers ioMembers = null;

	// power is ramped up from 0 to max by step size
	private static final int MAX_BASE_POWER = 40;
	private static final int BASE_POWER_STEP = 2;

	public PIDBehaviour(RobotInteractionMembers ioMembers) {
		this.ioMembers = ioMembers;
	}

	/**
	 * Configure the PID controller.
	 * 
	 * @param regularTrack
	 * @param specialTrack
	 * @param regularTarget
	 * @param specialTarget
	 */
	public void configure(int regularTrack, int specialTrack,
			int regularTarget, int specialTarget) {
		this.regularTrack = regularTrack;
		this.specialTrack = specialTrack;
		this.regularTarget = regularTarget;
		this.specialTarget = specialTarget;
		this.inSpecialWhenBelow = (specialTrack + regularTrack) / 2;
		Delay.msDelay(1000);
		resetControllerValues();
		isConfigured = true;
	}

	/**
	 * Reset the PID controller's accumulated error.
	 */
	private void resetControllerValues() {
		error = 0;
		lastError = 0;
		basePower = 5;
		integral = 0;
		derivative = 0;
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
		int targetValue = ioMembers.targetSensor.getLightValue();
		int current = ioMembers.lightSensor.getLightValue();
		double K_P = Double.POSITIVE_INFINITY;
		double K_I = Double.POSITIVE_INFINITY;
		double K_D = Double.POSITIVE_INFINITY;
		
		// ramp up speed over time
		if (basePower < MAX_BASE_POWER) {
			basePower += BASE_POWER_STEP;
		}
		
		// System.out.println(targetValue);
		if (targetValue < inSpecialWhenBelow) {
			// in special zone
			targetValue = specialTarget;
			K_P = SPECIAL_K_P;
			K_I = SPECIAL_K_I;
			K_D = SPECIAL_K_D;
		} else {
			// in normal zone
			targetValue = regularTarget;
			K_P = NORMAL_K_P;
			K_I = NORMAL_K_I;
			K_D = NORMAL_K_D;
		}
		
		error = current - targetValue;

		integral = integral + error;
		derivative = error - lastError;

		// find the turn based on the error and k values
		int turn = (int) Math.round(K_P * error + K_I * integral + K_D
				* derivative);

		ioMembers.leftMotor.controlMotor(basePower - turn, MotorPort.FORWARD);
		ioMembers.rightMotor.controlMotor(basePower + turn, MotorPort.FORWARD);

		lastError = error;
	}

	@Override
	public void suppress() {
		// reset values
		resetControllerValues();
	}

}
