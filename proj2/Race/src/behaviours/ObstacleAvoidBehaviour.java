package behaviours;

import lejos.nxt.MotorPort;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;

public class ObstacleAvoidBehaviour implements Behavior {

	private static final double OPTICAL_SENSOR_ERROR_MARGIN = 10;

	private double rightBaselineHeight = Double.POSITIVE_INFINITY;
	private double leftBaselineHeight = Double.POSITIVE_INFINITY;

	private OpticalDistanceSensor leftObstacleSensor;
	private OpticalDistanceSensor rightObstacleSensor;

	private MotorPort rightMotor;
	private MotorPort leftMotor;

	private boolean isConfigured = false;

	public ObstacleAvoidBehaviour(OpticalDistanceSensor rightObstacleSensor,
			OpticalDistanceSensor leftObstacleSensor, MotorPort rightMotor,
			MotorPort leftMotor) {
		this.rightObstacleSensor = rightObstacleSensor;
		this.leftObstacleSensor = leftObstacleSensor;

		this.rightMotor = rightMotor;
		this.leftMotor = leftMotor;
	}

	public void configure() {
		rightBaselineHeight = rightObstacleSensor.getDistance();
		leftBaselineHeight = leftObstacleSensor.getDistance();
		isConfigured = true;
	}

	@Override
	public boolean takeControl() {
//		if (!isConfigured) {
//			System.out.println("Obstacle avoid behavior not configured.");
//			return false;
//		}
//
//		double rightHeight = rightObstacleSensor.getDistance();
//		double leftHeight = leftObstacleSensor.getDistance();
//
//		if ((Math.abs(rightBaselineHeight - rightHeight) > OPTICAL_SENSOR_ERROR_MARGIN)
//				|| (Math.abs(leftBaselineHeight - leftHeight) > OPTICAL_SENSOR_ERROR_MARGIN)) {
//			return true;
//		}
		return false;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		System.out.println("OH SHIT! AVOID THAT THING!");

		Delay.msDelay(1000);

	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub

	}

}
