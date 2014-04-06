package main;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.OpticalDistanceSensor;

public class Main {

	public static void main(String[] args) {

		LightSensor lightSensor = new LightSensor(SensorPort.S1);
		OpticalDistanceSensor rightObstacleSensor = new OpticalDistanceSensor(
				SensorPort.S3);
		OpticalDistanceSensor farRightObstacleSensor = new OpticalDistanceSensor(
				SensorPort.S2);
		OpticalDistanceSensor leftObstacleSensor = new OpticalDistanceSensor(
				SensorPort.S4);

		RobotInteractionMembers ioMembers = new RobotInteractionMembers(
				lightSensor, MotorPort.A, MotorPort.C, Motor.A,
				Motor.C, rightObstacleSensor, leftObstacleSensor, farRightObstacleSensor);

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
