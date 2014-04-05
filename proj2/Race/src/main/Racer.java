package main;

import javax.naming.ConfigurationException;

import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import arbitrators.SingleThreadArbitrator;
import behaviours.ObstacleAvoidBehaviour;
import behaviours.PIDBehaviour;

public class Racer {
	// private int NORMAL_ZONE_READING = 45;
	// private int SPECIAL_ZONE_READING = 40;
	// private int INNER_EDGE_READING = 30;
	// private int OUTER_EDGE_READING = 24;

	private int NORMAL_ZONE = 52;
	private int SPECIAL_ZONE = 45;
//	private int NORMAL_TARGET = 39;
//	private int SPECIAL_TARGET = 35;
	private int NORMAL_TARGET = 35;
	private int SPECIAL_TARGET = 35;

//	private int EXTREME_TURN_INTERVENTION_TIME = 400;
	
	private static final int NUM_SENSOR_POLLS = 100;

	private RobotInteractionMembers ioMembers = null;

	private boolean isConfigured = false;
	private SingleThreadArbitrator arbitrator = null;

	public Racer(RobotInteractionMembers ioMembers) {
		this.ioMembers = ioMembers;
	}

	public void configure() {
		Behavior[] behaviors = null;
		
		PIDBehaviour pidDriver = new PIDBehaviour(ioMembers);
		pidDriver.configure(NORMAL_ZONE, SPECIAL_ZONE, NORMAL_TARGET,
				SPECIAL_TARGET);

		ObstacleAvoidBehaviour obstacleAvoider = new ObstacleAvoidBehaviour(
				ioMembers);
		obstacleAvoider.configure(NORMAL_ZONE, SPECIAL_ZONE, NORMAL_TARGET,
				SPECIAL_TARGET);

//		TimedRecoveryBehavior extremeTurnRecovery = new TimedRecoveryBehavior(
//				EXTREME_TURN_INTERVENTION_TIME, ioMembers);
//		extremeTurnRecovery.configure(NORMAL_ZONE, SPECIAL_ZONE);

		// Highest priority behavior with highest index.
		behaviors = new Behavior[] { pidDriver,
				obstacleAvoider };
		this.arbitrator = new SingleThreadArbitrator(behaviors, true);
		
		ioMembers.stop();
		
		this.isConfigured = true;
	}

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