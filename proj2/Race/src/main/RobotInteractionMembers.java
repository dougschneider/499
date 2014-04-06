package main;

import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.nxt.remote.RemoteMotor;

public class RobotInteractionMembers {

	public LightSensor lightSensor;
	public MotorPort rightMotor;
	public MotorPort leftMotor;
	public RemoteMotor rightRemoteMotor;
	public RemoteMotor leftRemoteMotor;
	public OpticalDistanceSensor leftObstacleSensor;
	public OpticalDistanceSensor rightObstacleSensor;
	public OpticalDistanceSensor farRightObstacleSensor;

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

	public void stop() {
		rightMotor.controlMotor(100, MotorPort.STOP);
		leftMotor.controlMotor(100, MotorPort.STOP);
	}
	
	public void forward() {
		leftMotor.controlMotor(30, MotorPort.FORWARD);
		rightMotor.controlMotor(30, MotorPort.FORWARD);
	}
}
