package behaviours;

import lejos.robotics.subsumption.Behavior;
import main.RobotInteractionMembers;

public class ObstacleAvoidBehaviour implements Behavior {

	private static final double OBSTACLE_MIN_HEIGHT = 60;
	private static final double NUM_BASELINE_SAMPLES = 100;

	private double rightDistanceToFloor = Double.POSITIVE_INFINITY;
	private double leftDistanceToFloor = Double.POSITIVE_INFINITY;

	// sensors and motors
	private RobotInteractionMembers ioMembers = null;

	private boolean isConfigured = false;
	private boolean hasControl = false;

	public ObstacleAvoidBehaviour(RobotInteractionMembers ioMembers) {
		this.ioMembers = ioMembers;
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

		double rightDistance = ioMembers.rightObstacleSensor.getDistance();
		double leftDistance = ioMembers.leftObstacleSensor.getDistance();

		System.out.println(leftDistance + "\t\t" + rightDistance);
		if ((Math.abs(rightDistanceToFloor - rightDistance) > OBSTACLE_MIN_HEIGHT)
				|| (Math.abs(leftDistanceToFloor - leftDistance) > OBSTACLE_MIN_HEIGHT)) {
			hasControl = true;
			return true;
		}
		return false;
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		System.out.println("OH SHIT! AVOID THAT THING!");
		ioMembers.stop();

		// back up and

	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		hasControl = false;
	}

}
