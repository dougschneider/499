package behaviours;

import lejos.nxt.Sound;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.nxt.remote.RemoteMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import main.RobotInteractionMembers;

public class ObstacleAvoidBehaviour implements Behavior {

	private static final double OBSTACLE_MIN_HEIGHT = 60;
	private static final double NUM_BASELINE_SAMPLES = 100;

	private double rightDistanceToFloor = Double.POSITIVE_INFINITY;
	private double leftDistanceToFloor = Double.POSITIVE_INFINITY;

	// sensors and motors
	private RobotInteractionMembers ioMembers = null;

	private DifferentialPilot pilot = null;

	private boolean isConfigured = false;
	private boolean hasControl = false;

	public ObstacleAvoidBehaviour(RobotInteractionMembers ioMembers) {
		this.ioMembers = ioMembers;
		// this.pilot = new
		// DifferentialPilot(RobotInteractionMembers.WHEEL_DIAMETER,
		// RobotInteractionMembers.TRACK_WIDTH, RemoteMotor., rightMotor)
	}

	public void configure() {
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
		Delay.msDelay(1000);

		// back up and try to go around
		if (rightSensorTriggered() && leftSensorTriggered()) {
			// TODO
		} else if (rightSensorTriggered()) {
			// TODO
		} else if (leftSensorTriggered()) {
			// TODO
		} else {
			// default case, just back up a bit
		}
		hasControl = false;
	}

	@Override
	public void suppress() {
		hasControl = false;
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
		return (distanceToFloor - distance) > OBSTACLE_MIN_HEIGHT;
	}

}
