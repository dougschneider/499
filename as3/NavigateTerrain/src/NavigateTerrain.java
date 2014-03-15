import java.io.IOException;

import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;

public class NavigateTerrain {

	public static int NUM_SWEEPS = 1;
	
	public static int SPEED = 20;
	public static int STEP_TIME = 20;
	public static int PIVOT_TIME = 1000;
	
	public static DifferentialPilot pilot;

	public static TrackerReader tracker;
	public static Tuple<Double, Double> topRight;
	public static Tuple<Double, Double> bottomLeft;

	public static void main(String[] args) {
		tracker = new TrackerReader();
		tracker.start();
		
		pilot = new DifferentialPilot(56, 120, Motor.C, Motor.A);
		pilot.setTravelSpeed(SPEED);
		pilot.setRotateSpeed(SPEED);
		
		// set up the tracker
		pauseAndPrompt("Select the robot's marker to track it");

		// get corner references
		topRight = getCorner(tracker, "top right");
		System.out.println("Top Right: " + topRight.x + ", " + topRight.y);
		bottomLeft = getCorner(tracker, "bottom left");
		System.out.println("Bottom Left: " + bottomLeft.x + ", " + bottomLeft.y);
		
		// gather data
		doGatherData(pilot, tracker, topRight, bottomLeft);

	}

	public static void doGatherData(
			DifferentialPilot pilot, TrackerReader tracker,
			Tuple<Double, Double> topRight, Tuple<Double, Double> bottomLeft) {
		int numSweeps = 0;
		while (numSweeps < NUM_SWEEPS) {
			doSingleSweep(pilot, tracker, topRight, bottomLeft);
			numSweeps++;
		}
	}

	private static void doSingleSweep(
			DifferentialPilot pilot, TrackerReader tracker,
			Tuple<Double, Double> topRight, Tuple<Double, Double> bottomLeft) {
		Boolean sweepComplete = false;
		
		// tell user to place robot
		pauseAndPrompt("Place robot in top right corner, aimed left");

		while (sweepComplete == false) {
			doPass(pilot, Direction.LEFT, topRight, bottomLeft);
			pivot(pilot, Direction.LEFT);
			doPass(pilot, Direction.RIGHT, topRight, bottomLeft);
			pivot(pilot, Direction.RIGHT);
			sweepComplete = isSweepComplete(tracker, topRight, bottomLeft);
		}
		System.out.println("Sweep Complete");
	}

	private static Boolean isSweepComplete(TrackerReader tracker,
			Tuple<Double, Double> topRight, Tuple<Double, Double> bottomLeft) {
		if (tracker.y > bottomLeft.y) {
			return true;
		}
		return false;
	}

	private static void doPass(
			DifferentialPilot pilot, Direction direction,
			Tuple<Double, Double> topRight, Tuple<Double, Double> bottomLeft) {
		System.out.println("Robot: " + tracker.x + ", " + tracker.y + "\tCorner: " + bottomLeft.x + ", " + bottomLeft.y);
		if (direction == Direction.RIGHT) {
			while (tracker.x < topRight.x) {
				driveForward(pilot, STEP_TIME);
			}
		} else {
			while (tracker.x > bottomLeft.x) {
				driveForward(pilot, STEP_TIME);
			}
		}
		System.out.println("Done Pass");

	}

	private static void driveForward(DifferentialPilot pilot, int time) {
		pilot.forward();
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void pivot(DifferentialPilot pilot, Direction direction) {
		pauseAndPrompt("Please align the robot for the next pass");
	}

	/**
	 * Prompt the user to set up the target for the given corner and then return
	 * a new tuple for it.
	 * 
	 * @param tracker
	 * @param corner
	 *            : text such as "top right" or "bottom left"
	 * @return new Tuple instance with the corner coordinates
	 */
	public static Tuple<Double, Double> getCorner(TrackerReader tracker,
			String corner) {
		pauseAndPrompt("Set target to " + corner + " corner and press enter.");
		return new Tuple<Double, Double>(tracker.targetx, tracker.targety);
	}

	/**
	 * Display text and wait for user to press enter key.
	 * 
	 * @param text
	 */
	public static void pauseAndPrompt(String text) {
		System.out.println(text);

		// ignore any user input; just wait for enter key
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
