package main;

import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.OpticalDistanceSensor;

public class Main {

	public static void main(String[] args) {

		LightSensor lightSensor = new LightSensor(SensorPort.S1);
		LightSensor targetSensor = new LightSensor(SensorPort.S2);
		OpticalDistanceSensor leftObstacleSensor = new OpticalDistanceSensor(
				SensorPort.S3);
		OpticalDistanceSensor rightObstacleSensor = new OpticalDistanceSensor(
				SensorPort.S4);

		RobotInteractionMembers ioMembers = new RobotInteractionMembers(
				lightSensor, targetSensor, MotorPort.A, MotorPort.C,
				rightObstacleSensor, leftObstacleSensor);

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
