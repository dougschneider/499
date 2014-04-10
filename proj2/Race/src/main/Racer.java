package main;

import javax.naming.ConfigurationException;

import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import arbitrators.SingleThreadArbitrator;
import behaviours.ObstacleAvoidBehaviour;
import behaviours.PIDBehaviour;

/**
 * Top-Level implementation of a behaviour-driven robot. Goes around the
 * circuit and avoids obstacles.
 * Internal implementation is a subsumption architecture that uses a PID
 * controller to follow the outer edge of the circuit and custom moves
 * to avoid obstacles using optical sensors.
 */
public class Racer {

	// PID controller target values
	private int NORMAL_ZONE = 52;
	private int SPECIAL_ZONE = 45;
	private int NORMAL_TARGET = 35;
	private int SPECIAL_TARGET = 35;
	
	// container for motors and sensors so they don't all have to be passed around
	private RobotInteractionMembers ioMembers = null;
	// arbitrator for subsumption architecture
	private SingleThreadArbitrator arbitrator = null;
	
	private boolean isConfigured = false;

	public Racer(RobotInteractionMembers ioMembers) {
		this.ioMembers = ioMembers;
	}

	/**
	 * Configure the racer. Gets any required initial sensor readings and
	 * sets up the behaviours. Try to make sure the robot is stationary.
	 */
	public void configure() {
		Behavior[] behaviors = null;
		
		PIDBehaviour pidDriver = new PIDBehaviour(ioMembers);
		pidDriver.configure(NORMAL_ZONE, SPECIAL_ZONE, NORMAL_TARGET,
				SPECIAL_TARGET);

		ObstacleAvoidBehaviour obstacleAvoider = new ObstacleAvoidBehaviour(
				ioMembers);
		obstacleAvoider.configure(NORMAL_ZONE, SPECIAL_ZONE, NORMAL_TARGET,
				SPECIAL_TARGET);

		// Highest priority behavior has highest index in array.
		behaviors = new Behavior[] { pidDriver,
				obstacleAvoider };
		
		// returnWhenInactive == true so that we can tell when no behaviour kicks in
		this.arbitrator = new SingleThreadArbitrator(behaviors, true);
		
		this.isConfigured = true;
	}

	/**
	 * Start the racer's behaviour.
	 * 
	 * @throws ConfigurationException
	 * 		When configure() has not been called.
	 * @throws RuntimeException
	 * 		When the arbitrator exits. This means no behaviour has taken control
	 * 		and probably means you have the behaviours in an incorrect priority order.
	 */
	public void race() throws ConfigurationException, RuntimeException {
		if (!this.isConfigured) {
			throw new ConfigurationException("Run configure() before race()");
		}
		arbitrator.start();

		// This code is only reached if arbitrator exits
		ioMembers.stop();

		throw new RuntimeException(
				"Arbitrator Should not exit. This means no behavior decided to take control.");
	}
}