import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
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

	public static ArrayList<Triple<Double, Double, Integer>> data = new ArrayList<Triple<Double, Double, Integer>>();

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
		System.out
				.println("Bottom Left: " + bottomLeft.x + ", " + bottomLeft.y);

		// gather data
		doGatherData(pilot, tracker, topRight, bottomLeft);

		generateArff(data);
		
		System.exit(0);
	}

	private static void generateArff(
			ArrayList<Triple<Double, Double, Integer>> data) {
		FileWriter out = null;
		try {
			out = new FileWriter(new File("../p2data.arff"));

			out.write("@RELATION elevation\n\n");
			out.write("@ATTRIBUTE x NUMERIC\n");
			out.write("@ATTRIBUTE y NUMERIC\n");
			out.write("@ATTRIBUTE elevation NUMERIC\n\n");
			out.write("@DATA\n");

			for (int i = 0; i < data.size(); i++) {
				out.write(data.get(i).x + "," + data.get(i).y + ","
						+ data.get(i).data + "\n");
			}

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void doGatherData(DifferentialPilot pilot,
			TrackerReader tracker, Tuple<Double, Double> topRight,
			Tuple<Double, Double> bottomLeft) {
		int numSweeps = 0;
		while (numSweeps < NUM_SWEEPS) {
			doSingleSweep(pilot, tracker, topRight, bottomLeft);
			numSweeps++;
		}
	}

	private static void doSingleSweep(DifferentialPilot pilot,
			TrackerReader tracker, Tuple<Double, Double> topRight,
			Tuple<Double, Double> bottomLeft) {
		Boolean sweepComplete = false;

		// tell user to place robot
		pauseAndPrompt("Place robot in top right corner, aimed left");

		while (sweepComplete == false) {
			doPass(tracker, pilot, Direction.LEFT, topRight, bottomLeft);
			pivot(pilot, Direction.LEFT);
			doPass(tracker, pilot, Direction.RIGHT, topRight, bottomLeft);
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

	private static void doPass(TrackerReader tracker, DifferentialPilot pilot,
			Direction direction, Tuple<Double, Double> topRight,
			Tuple<Double, Double> bottomLeft) {
		System.out.println("Robot: " + tracker.x + ", " + tracker.y
				+ "\tCorner: " + bottomLeft.x + ", " + bottomLeft.y);
		LightSensor lightsensor = new LightSensor(SensorPort.S1);
		if (direction == Direction.RIGHT) {
			while (tracker.x < topRight.x) {
				doPassActions(tracker, pilot, lightsensor);
			}
		} else {
			while (tracker.x > bottomLeft.x) {
				doPassActions(tracker, pilot, lightsensor);
			}
		}
		System.out.println("Done Pass");
	}

	private static void doPassActions(TrackerReader tracker,
			DifferentialPilot pilot, LightSensor lightsensor) {
		driveForward(pilot, STEP_TIME);
		// 100 - lightValue so that it represents elevation
		data.add(new Triple<Double, Double, Integer>(tracker.x, tracker.y,
				100 - lightsensor.getLightValue()));
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
		pilot.stop();
		pauseAndPrompt("Please rotate robot 180 degrees");
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