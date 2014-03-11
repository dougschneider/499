import java.io.IOException;

import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.util.Delay;

public class NavigateTerrain {

	public static int NUM_SWEEPS = 1;

	public static TrackerReader tracker;
	public static Tuple<Double, Double> topRight;
	public static Tuple<Double, Double> bottomLeft;

	public static void main(String[] args) {
		tracker = new TrackerReader();
		tracker.start();

		// get corner references
		topRight = getCorner(tracker, "top right");
		bottomLeft = getCorner(tracker, "bottom left");

		// tell user to place robot
		pauseAndPrompt("Place robot in top right corner, aimed left");

		// gather data
		doGatherData(tracker, topRight, bottomLeft);

	}

	public static void doGatherData(TrackerReader tracker,
			Tuple<Double, Double> topRight, Tuple<Double, Double> bottomLeft) {
		int numSweeps = 0;
		while (numSweeps < NUM_SWEEPS) {
			doSingleSweep(tracker, topRight, bottomLeft);
			numSweeps++;
		}
	}

	private static void doSingleSweep(TrackerReader tracker,
			Tuple<Double, Double> topRight, Tuple<Double, Double> bottomLeft) {
		// TODO implement full grid sweep
		doPass(Direction.LEFT, topRight, bottomLeft);

	}

	private static void doPass(Direction direction,
			Tuple<Double, Double> topRight, Tuple<Double, Double> bottomLeft) {
		// TODO implement direction
		while (tracker.x < bottomLeft.x) {
			driveForward(20);
		}
		System.out.println("Done");

	}
	
	private static void driveForward(int time) {
		MotorPort.A.controlMotor(60, MotorPort.FORWARD);
		MotorPort.C.controlMotor(60, MotorPort.FORWARD);
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
