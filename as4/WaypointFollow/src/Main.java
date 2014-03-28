import lejos.nxt.MotorPort;
import lejos.util.Delay;

public class Main {
	public static double TARGET_RADIUS = 0.5;
	public static TrackerReader tracker;
	public static int numWaypoints = 0;
	public static int currLineStart = 0;
	public static int currLineEnd = 1;
	public static MotorPort leftMotor = null;
	public static MotorPort rightMotor = null;
	public static WaypointFollowControllers controller = new WaypointFollowControllers();

	public static void main(String[] args) {
		tracker = new TrackerReader();
		leftMotor = MotorPort.C;
		rightMotor = MotorPort.A;

		tracker.start();
		while (true) {
			updateWaypoints();
			printStatus();
			if (numWaypoints > currLineEnd) {
				followCurrLine();
			}
			setNextLine();
			Delay.msDelay(1000);
		}
	}

	public static double distanceToCurrLine() {
		return distanceToLine(tracker.x, tracker.y, getCurrLineStart(0),
				getCurrLineStart(1), getCurrLineEnd(0), getCurrLineEnd(1));
	}

	/**
	 * Return True if robot is within TARGET_RADIUS of current line endpoint.
	 */
	public static boolean reachedLineEnd() {
		return distance(tracker.x, tracker.y, getCurrLineEnd(0),
				getCurrLineEnd(1)) < TARGET_RADIUS;
	}

	/**
	 * Print robot and waypoint status info.
	 */
	private static void printStatus() {
		System.out.println("Location: " + tracker.x + ", " + tracker.y);
		System.out.println("Line Start (wp " + currLineStart + "): "
				+ getCurrLineStart(0) + ", " + getCurrLineStart(1));
		System.out.println("Line End (wp " + currLineEnd + "): "
				+ getCurrLineEnd(0) + ", " + getCurrLineEnd(1));
		System.out.println("Distance to Line: " + distanceToCurrLine());
		System.out.println("Reached Line End: " + reachedLineEnd());
		System.out.println("Waypoints (" + numWaypoints + "):");
		for (int i = 0; i < numWaypoints; i++) {
			System.out.println(tracker.waypoints.get(i)[0] + ", "
					+ tracker.waypoints.get(i)[1]);
		}
		System.out.println("------------------------");
	}

	/**
	 * Get the x or y point value of the current line start point.
	 * 
	 * @param idx
	 *            - 0 for x, 1 for y
	 * @return
	 */
	private static double getCurrLineStart(int idx) {
		if (currLineStart < numWaypoints) {
			return tracker.waypoints.get(currLineStart)[idx];
		}
		return Double.NaN;
	}

	/**
	 * Get the x or y point value of the current line end point.
	 * 
	 * @param idx
	 *            - 0 for x, 1 for y
	 * @return
	 */
	private static double getCurrLineEnd(int idx) {
		if (currLineEnd < numWaypoints) {
			return tracker.waypoints.get(currLineEnd)[idx];
		}
		return Double.NaN;
	}

	private static void followCurrLine() {
		controller.PID(1.8, 0.1, 0.3, null, leftMotor, rightMotor);
	}

	/**
	 * Update the current waypoint and print waypoint info.
	 */
	private static void updateWaypoints() {
		numWaypoints = tracker.waypoints.size();
	}

	/**
	 * Set the start and end point of the line to follow.
	 */
	private static void setNextLine() {
		if (numWaypoints >= 2) {
			currLineStart++;
			currLineEnd++;
		}
	}

	/**
	 * Cartesian distance between p1 and p2.
	 * 
	 * @param p1
	 * @param p2
	 */
	private static double distance(double x1, double y1, double x2, double y2) {
		// d = sqrt((x2 - x1)^2 + (y2 - y1)^2)
		// d = sqrt(dx^2 - dy^2)
		double dx = x2 - x1;
		double dy = y2 - y1;
		dx = Math.pow(dx, 2);
		dy = Math.pow(dy, 2);
		return Math.sqrt(dx + dy);
	}

	/**
	 * Shortest distance from p to line that runs through both l1 and l2.
	 * 
	 * @param p
	 * @param l1
	 * @param l2
	 * @return
	 */
	private static double distanceToLine(double x1, double y1, double xl1,
			double yl1, double xl2, double yl2) {
		// see
		// http://math.ucsd.edu/~wgarner/math4c/derivations/distance/distptline.htm

		// get y=m*x+b format for line
		double m = yl2 - yl1;
		double b = xl1;

		return Math.abs(y1 - m * x1 - b) / Math.sqrt(Math.pow(m, 2) + 1);
	}
}
