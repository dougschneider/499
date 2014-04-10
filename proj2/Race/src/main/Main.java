package main;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.OpticalDistanceSensor;

/**
 * Implementation of Project 2.
 */
public class Main {

	public static void main(String[] args) {

		// set up sensors
		LightSensor lightSensor = new LightSensor(SensorPort.S1);
		OpticalDistanceSensor rightObstacleSensor = new OpticalDistanceSensor(
				SensorPort.S3);
		OpticalDistanceSensor farRightObstacleSensor = new OpticalDistanceSensor(
				SensorPort.S2);
		OpticalDistanceSensor leftObstacleSensor = new OpticalDistanceSensor(
				SensorPort.S4);

		// put all the sensors in container class
		RobotInteractionMembers ioMembers = new RobotInteractionMembers(
				lightSensor, MotorPort.A, MotorPort.C, Motor.A,
				Motor.C, rightObstacleSensor, leftObstacleSensor, farRightObstacleSensor);

		// init, configure, and run racer
		Racer racer = new Racer(ioMembers);
		racer.configure();
		try {
			racer.race();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}

}
