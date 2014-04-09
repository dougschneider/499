import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.remote.RemoteMotor;
import lejos.robotics.DirectionFinder;
import lejos.robotics.Gyroscope;
import lejos.robotics.navigation.CompassPilot;

/**
 * Test PC project for playing with gyroscope and compass pilot.
 * Doesn't appear to work because USB doesn' support threads.
 */
public class Main {

	public static void main(String[] args) {
		RemoteMotor leftMotor = Motor.C;
		RemoteMotor rightMotor = Motor.A;
		
		leftMotor.setSpeed(100);
		rightMotor.setSpeed(100);
		Gyroscope gyroscope = new GyroSensor(SensorPort.S1);
		System.out.println(leftMotor.getMaxSpeed());
		DirectionFinder directionFinder = new GyroDirectionFinder(gyroscope);
		CompassPilot compassPilot = new CompassPilot(directionFinder, 56, 120, leftMotor, rightMotor);
//		compassPilot.calibrate();
//		compassPilot.rotate(180);
	}

}
