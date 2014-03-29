import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import controllers.Controllers;

/**
 * Inherits from base controller class. Overrides some methods to report error
 * appropriately.
 */
public class WaypointFollowControllers extends Controllers {
	
	private double ERROR_SCALE_FACTOR = 0.25;

	/**
	 * Current value is distance to line.
	 *  
	 * @param sensor
	 * @return
	 */
	@Override
	protected int getCurrentValue(LightSensor sensor) {
		return (int) Main.distanceToCurrLine();
	}

	/**
	 * Terminal state when robot is within radius of line endpoint.
	 * 
	 * @return
	 */
	@Override
	protected boolean isTerminalState() {
		return Main.reachedLineEnd() || super.isTerminalState();
	}

	/**
	 * On exit of controller, do not exit program, just stop robot.
	 * 
	 * @param leftMotor
	 * @param rightMotor
	 */
	@Override
	protected void exitController(MotorPort leftMotor,
			MotorPort rightMotor) {
		leftMotor.controlMotor(100, MotorPort.STOP);
		rightMotor.controlMotor(100, MotorPort.STOP);
	}
	
	/**
	 * Provide a margin within which we'll say we're on the line
	 */
	@Override
	protected int getError(int current, int targetValue) {
		int error = current - targetValue;
		// error is large (because distance in pixels), so scale it a little
		error *= ERROR_SCALE_FACTOR;
		System.out.println(error);
		// robot was turning the wrong direction, so negate the error
		return -error;
	}
}
