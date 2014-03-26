import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.OpticalDistanceSensor;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Robot agent = new Robot(56, 120, Motor.C, Motor.A,
				                new UltrasonicSensor(SensorPort.S4),
				                new OpticalDistanceSensor(SensorPort.S3),
				                new OpticalDistanceSensor(SensorPort.S2));
		agent.run();
	}

}
