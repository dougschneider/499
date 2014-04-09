package main;

import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.nxt.remote.RemoteMotor;

/**
 * Storage class for Motors, Sensors, etc.
 * Provides some utility methods for common actions, but it intended to
 * be a dump object, not for controlling the robot.
 */
public class RobotInteractionMembers {

	// sensors
	public LightSensor lightSensor;
	public OpticalDistanceSensor leftObstacleSensor;
	public OpticalDistanceSensor rightObstacleSensor;
	public OpticalDistanceSensor farRightObstacleSensor;
	
	/* Motors
	 * 
	 * DifferentialPilot needs RemoteMotor instances, so each motor is
	 * here twice.
	 */
	public MotorPort rightMotor;
	public MotorPort leftMotor;
	public RemoteMotor rightRemoteMotor;
	public RemoteMotor leftRemoteMotor;
	
	public static final double WHEEL_DIAMETER = 56;
	public static final double TRACK_WIDTH = 120;

	public RobotInteractionMembers(LightSensor lightSensor,
			MotorPort rightMotor,
			MotorPort leftMotor, RemoteMotor motorRightMotor,
			RemoteMotor motorLeftMotor,
			OpticalDistanceSensor rightObstacleSensor,
			OpticalDistanceSensor leftObstacleSensor,
			OpticalDistanceSensor farRightObstacleSensor) {
		this.lightSensor = lightSensor;
		this.lightSensor.setFloodlight(true);

		this.rightMotor = rightMotor;
		this.leftMotor = leftMotor;

		this.rightRemoteMotor = motorRightMotor;
		this.leftRemoteMotor = motorLeftMotor;

		this.rightObstacleSensor = rightObstacleSensor;
		this.farRightObstacleSensor = farRightObstacleSensor;
		this.leftObstacleSensor = leftObstacleSensor;
	}

	/**
	 * Stop the motors.
	 */
	public void stop() {
		rightMotor.controlMotor(100, MotorPort.STOP);
		leftMotor.controlMotor(100, MotorPort.STOP);
	}

	/**
	 * Set the motors to move forward at slow speed.
	 */
	public void forward() {
		leftMotor.controlMotor(30, MotorPort.FORWARD);
		rightMotor.controlMotor(30, MotorPort.FORWARD);
	}
}
