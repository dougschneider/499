package behaviours;

import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.nxt.remote.RemoteMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import main.RobotInteractionMembers;

public class ObstacleAvoidBehaviour implements Behavior {

	private static final double OBSTACLE_MIN_HEIGHT = 150;
	private static final double NUM_BASELINE_SAMPLES = 100;
	private static final double NUM_SENSOR_SAMPLES = 3;

	private static final int TRAVEL_SPEED = 100;
	private static final int ROTATE_SPEED = 100;
	private static final int BACK_UP_DISTANCE = -50;
	private static final int SIDE_DISTANCE = 150;
	private static final int FORWARD_DISTANCE = 300;
	private static final int SIDE_STEP_ANGLE = 40;

	private double rightDistanceToFloor = Double.POSITIVE_INFINITY;
	private double leftDistanceToFloor = Double.POSITIVE_INFINITY;

	private int regularTrack = -1;
	private int specialTrack = -1;
	private int specialTarget = -1;
	private int regularTarget = -1;

	// sensors and motors
	private RobotInteractionMembers ioMembers = null;

	private DifferentialPilot pilot = null;

	private boolean isConfigured = false;
	private boolean hasControl = false;

	public ObstacleAvoidBehaviour(RobotInteractionMembers ioMembers) {
		this.ioMembers = ioMembers;
		this.pilot = new DifferentialPilot(
				RobotInteractionMembers.WHEEL_DIAMETER,
				RobotInteractionMembers.TRACK_WIDTH, ioMembers.leftRemoteMotor,
				ioMembers.rightRemoteMotor);
		this.pilot.setTravelSpeed(TRAVEL_SPEED);
		this.pilot.setRotateSpeed(ROTATE_SPEED);
	}

	public void configure(int regularTrack, int specialTrack,
			int regularTarget, int specialTarget) {
		this.regularTrack = regularTrack;
		this.specialTrack = specialTrack;
		this.regularTarget = regularTarget;
		this.specialTarget = specialTarget;

		rightDistanceToFloor = ioMembers.rightObstacleSensor.getDistance();
		leftDistanceToFloor = ioMembers.leftObstacleSensor.getDistance();
		for (int i = 0; i < NUM_BASELINE_SAMPLES - 1; i++) {
			rightDistanceToFloor += ioMembers.rightObstacleSensor.getDistance();
			leftDistanceToFloor += ioMembers.leftObstacleSensor.getDistance();
		}
		rightDistanceToFloor /= NUM_BASELINE_SAMPLES;
		leftDistanceToFloor /= NUM_BASELINE_SAMPLES;

		System.out.println("Left Baseline: " + leftDistanceToFloor);
		System.out.println("Right Baseline: " + rightDistanceToFloor);

		isConfigured = true;
	}

	@Override
	public boolean takeControl() {
		if (!isConfigured) {
			System.out.println("Obstacle avoid behavior not configured.");
			return false;
		}
		if (hasControl)
			return true;

		// double distance = ioMembers.leftObstacleSensor.getDistance();
		// System.out.println(distance);
		// return false;

		if (rightSensorTriggered() || leftSensorTriggered()) {
			System.out.println("Avoiding Obstacle: "
					+ ioMembers.leftObstacleSensor.getDistance());
			return true;
		}
		return false;
	}

	@Override
	public void action() {
		hasControl = true;

		ioMembers.stop();

		Sound.beep();
		System.out.println("Current Obs Reading: "
				+ ioMembers.leftObstacleSensor.getDistance() + "\t\t"
				+ ioMembers.rightObstacleSensor.getDistance());
		Delay.msDelay(500);

		/*
		 * check which sensors are triggered if sensors the same rotate left
		 * else rotate right
		 */
		if (Math.abs(ioMembers.lightSensor.getLightValue()
				- ioMembers.targetSensor.getLightValue()) < 5) {
			pilot.rotate(15);
		} else {
			pilot.rotate(-15);
		}

		boolean prevTrigger = true;
		boolean currTrigger = true;
		if (leftSensorTriggered()) {
			while (currTrigger || prevTrigger) {
				sideStepLeftBackward();
				prevTrigger = currTrigger;
				currTrigger = leftSensorTriggered();
			}
			moveForward(true);
			moveForward(true);
			moveForward(true);
			moveForward(true);
			shuffleBack(true, 0);
		} else {
			int shuffleCount = 0;
			while (currTrigger || prevTrigger) {
				sideStepRightBackward();
				prevTrigger = currTrigger;
				currTrigger = rightSensorTriggered();
				++shuffleCount;
			}
			moveForward(false);
			if (!isOnLine(120)) {
				moveForward(false);
				moveForward(false);
				moveForward(false);
				moveForward(false);
				shuffleBack(false, shuffleCount);
				System.out.println("Done shuffle back to center");
				shuffleBack(true, 0);
				System.out.println("Found line.");
			}
		}

		hasControl = false;
	}

	@Override
	public void suppress() {
		hasControl = false;
	}

	private void shuffleBack(boolean right, int shuffleCount) {
		int curCount = 0;
		while (true) {
			if (right) {
				if (ioMembers.lightSensor.getLightValue() < (specialTarget + 5)) {
					break;
				}
				sideStepRightForward();
				pilot.rotate(10);
				Delay.msDelay(10);
				if (ioMembers.lightSensor.getLightValue() < (specialTarget + 5)) {
					break;
				}
				pilot.rotate(-20);
				Delay.msDelay(10);
				if (ioMembers.lightSensor.getLightValue() < (specialTarget + 5)) {
					break;
				}
				pilot.rotate(10);
			} else {
				if (curCount > shuffleCount + 2) {
					break;
				}
				++curCount;
				sideStepLeftForward();
			}
		}
	}
	
	/**
	 * Pivot left, check for line, if line found return true.
	 * 
	 * @param degrees
	 * 		must be a multiple of 5 
	 * @return
	 */
	private boolean isOnLine(int degrees) {
		int degRotated = 0;
		// 1 or -1
		int unit = degrees / Math.abs(degrees) * 5;
		while (degRotated != degrees) {
			pilot.rotate(unit);
			degRotated += unit;
			if (ioMembers.lightSensor.getLightValue() < (specialTarget + 5)) {
				return true;
			}
		}
		pilot.rotate(-degrees);
		return false;
	}

	private void moveForward(boolean exit) {
		int distForward = 0;
		while (distForward < FORWARD_DISTANCE) {
			pilot.forward();
			Delay.msDelay(10);
			distForward += 10;
			if (exit
					&& (ioMembers.lightSensor.getLightValue() < (specialTarget + 5))) {
				break;
			}
		}
	}

	private void sideStepLeftBackward() {
		sideStep(true, true);
	}

	private void sideStepRightBackward() {
		sideStep(false, true);
	}

	private void sideStepLeftForward() {
		sideStep(false, false);
	}

	private void sideStepRightForward() {
		sideStep(true, false);
	}

	private void sideStep(boolean left, boolean backward) {
		int angle = left ? SIDE_STEP_ANGLE : -SIDE_STEP_ANGLE;
		int distance = backward ? BACK_UP_DISTANCE : -BACK_UP_DISTANCE;
		pilot.rotate(angle);
		pilot.travel(distance);
		pilot.rotate(-angle);
		pilot.travel(-distance);
	}

	private boolean rightSensorTriggered() {
		return sensorTriggered(ioMembers.rightObstacleSensor,
				rightDistanceToFloor);
	}

	private boolean leftSensorTriggered() {
		return sensorTriggered(ioMembers.leftObstacleSensor,
				leftDistanceToFloor);
	}

	private boolean sensorTriggered(OpticalDistanceSensor sensor,
			double distanceToFloor) {
		double distance = sensor.getDistance();
		for (int i = 0; i < NUM_SENSOR_SAMPLES - 1; i++) {
			distance += sensor.getDistance();
		}
		distance /= NUM_SENSOR_SAMPLES;
		System.out.println("Distance = " + distance);
		return distance < OBSTACLE_MIN_HEIGHT;

	}

}
