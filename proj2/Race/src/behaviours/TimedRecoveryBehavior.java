package behaviours;

import java.util.Calendar;

import lejos.robotics.subsumption.Behavior;
import main.RobotInteractionMembers;

/**
 * If the robot has been off the edge line for longer than
 * the configured time, take control and try to recover.
 */
public class TimedRecoveryBehavior implements Behavior {
	
	private static final int SENSOR_ERROR_MARGIN = 5;
	private static final double DISCOUNT_RATE = 0.01;

	private double elapsedTime = 0;
	private long prevCallTime = -1;
	private long interventionTime = 0;
	
	private double probabilityOfOffEdge = 0;
	
	private boolean hasControl = false;
	private boolean isConfigured = false;
	
	private int regularTrack = -1;
	private int specialTrack = -1;
	private int inSpecialWhenBelow = -1;
	
	// sensors and motors
	private RobotInteractionMembers ioMembers = null;


	
	/**
	 * If the robot has been off the edge line for longer than
	 * the configured time, take control and try to recover.
	 * @param interventionTime
	 * 		Time milliseconds after which to take control
	 */
	public TimedRecoveryBehavior(long interventionTime, RobotInteractionMembers ioMembers) {
		this.ioMembers = ioMembers;
		this.interventionTime = interventionTime;
	}
	
	public void configure(int regularTrack, int specialTrack) {
		this.regularTrack = regularTrack;
		this.specialTrack = specialTrack;
		this.inSpecialWhenBelow = (specialTrack + regularTrack) / 2;
		
		isConfigured = true;
	}
	
	private boolean isOffEdgeLine() {
//		int trackReading = ioMembers.targetSensor.getLightValue();
		int trackReading = 0;
		int edgeReading = ioMembers.lightSensor.getLightValue();
		int isOffEdge = 0;
		
//		System.out.println("Track: " + trackReading + "\tEdge: " + edgeReading);
		if (trackReading < inSpecialWhenBelow) {
			if (Math.abs(specialTrack - edgeReading) < SENSOR_ERROR_MARGIN) {
				isOffEdge = 1;
			}
		} else {
			if (Math.abs(regularTrack - edgeReading) < SENSOR_ERROR_MARGIN) {
				isOffEdge = 1;
			}
		}
//		if (Math.abs(trackReading - edgeReading) < SENSOR_ERROR_MARGIN) {
//			return true;
//		}
		
		probabilityOfOffEdge = isOffEdge + DISCOUNT_RATE * probabilityOfOffEdge;
		return probabilityOfOffEdge > 0.5;
	}
	
	@Override
	public boolean takeControl() {
		if (!isConfigured) {
			System.out.println("Timed Recovery behavior not configured.");
			return false;
		}
		if (hasControl) return true;
		
		// Only accumulate the time that the robot is off the edge for
		if (isOffEdgeLine()) {
			System.out.println("Off Edge Line");
			long currTime = Calendar.getInstance().getTimeInMillis();
			if (prevCallTime == -1) {
				prevCallTime = currTime;
			}
			elapsedTime += currTime - prevCallTime;
			prevCallTime = currTime;
		} else {
			elapsedTime = 0;
			prevCallTime = -1;
		}
		
//		System.out.println(elapsedTime);
		
		if (elapsedTime > interventionTime) {
			elapsedTime = 0;
			hasControl = true;
			return true;
		}
		return false;
	}

	@Override
	public void action() {
//		System.out.println("Recovery behavior taking control");
		ioMembers.stop();
	}

	@Override
	public void suppress() {
		//hasControl = false;
	}

}
