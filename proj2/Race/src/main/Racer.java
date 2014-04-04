package main;

import javax.naming.ConfigurationException;

import behaviours.ObstacleAvoidBehaviour;
import behaviours.PIDBehaviour;
import behaviours.SingleThreadArbitrator;
import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;

public class Racer {
	private int NORMAL_ZONE_READING = 45;
	private int SPECIAL_ZONE_READING = 40;
	private int INNER_EDGE_READING = 30;
	private int OUTER_EDGE_READING = 24;

	private static double wheelDiameter = 56;
	private static double trackWidth = 120;

	private boolean isConfigured = false;
	private SingleThreadArbitrator arbitrator = null;

	private LightSensor lightSensor;
	private LightSensor targetSensor;
	private MotorPort rightMotor;
	private MotorPort leftMotor;
	private OpticalDistanceSensor leftObstacleSensor;
	private OpticalDistanceSensor rightObstacleSensor;

	public Racer(LightSensor lightSensor, LightSensor targetSensor,
			MotorPort rightMotor, MotorPort leftMotor,
			OpticalDistanceSensor rightObstacleSensor,
			OpticalDistanceSensor leftObstacleSensor) {
		this.lightSensor = lightSensor;
		this.lightSensor.setFloodlight(true);
		this.targetSensor = targetSensor;
		this.targetSensor.setFloodlight(true);

		this.rightMotor = rightMotor;
		this.leftMotor = leftMotor;

		this.rightObstacleSensor = rightObstacleSensor;
		this.leftObstacleSensor = leftObstacleSensor;
	}

	public void configure() {
		Behavior[] behaviors = null;
		// TODO configure sensor values

		behaviors = initBehaviors();
		this.arbitrator = new SingleThreadArbitrator(behaviors, true);
		this.isConfigured = true;
	}

	private Behavior[] initBehaviors() {
		PIDBehaviour pidDriver = new PIDBehaviour(lightSensor, targetSensor,
				rightMotor, leftMotor);
		pidDriver.configure();

		ObstacleAvoidBehaviour obstacleAvoider = new ObstacleAvoidBehaviour(
				rightObstacleSensor, leftObstacleSensor, rightMotor, leftMotor);
		obstacleAvoider.configure();

		// highest index in array has highest priority
		return new Behavior[] { pidDriver, obstacleAvoider };
	}

	public void race() throws ConfigurationException, RuntimeException {
		if (!this.isConfigured) {
			throw new ConfigurationException("Run configure() before race()");
		}
		arbitrator.start();
		rightMotor.controlMotor(100, MotorPort.STOP);
		leftMotor.controlMotor(100, MotorPort.STOP);
		throw new RuntimeException(
				"Arbitrator Should not exit. This means no behavior decided to take control.");
	}
}