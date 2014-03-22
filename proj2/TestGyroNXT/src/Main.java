import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.nxt.addon.GyroSensor;
import lejos.robotics.DirectionFinder;
import lejos.robotics.Gyroscope;
import lejos.robotics.navigation.CompassPilot;
import lejos.util.Delay;


public class Main {

	public static void main(String[] args) {
		NXTRegulatedMotor leftMotor = Motor.C;
		NXTRegulatedMotor rightMotor = Motor.A;
		
		leftMotor.setSpeed(300);
		rightMotor.setSpeed(300);
		GyroSensor gyroscope = new GyroSensor(SensorPort.S1);
//		gyroscope.recalibrateOffset();
//		leftMotor.rotate(360);
//		while(true)
//		{
//			Delay.msDelay(200);
//			System.out.println(gyroscope.getAngularVelocity());
//			if(Button.RIGHT.isPressed())
//				break;
//		}
		gyroscope.recalibrateOffset();
		DirectionFinder directionFinder = new GyroDirectionFinder(gyroscope, true);
		directionFinder.resetCartesianZero();
		directionFinder.startCalibration();
		Delay.msDelay(2000);
		directionFinder.stopCalibration();
		System.out.println(directionFinder.getDegreesCartesian());
		leftMotor.rotate(360, true);
		rightMotor.rotate(-360, false);
		System.out.println(leftMotor.isMoving() || rightMotor.isMoving());
		System.out.println(directionFinder.getDegreesCartesian());
//		CompassPilot compassPilot = new CompassPilot(directionFinder, 56, 120, leftMotor, rightMotor);
//		compassPilot.calibrate();
//		compassPilot.setRotateSpeed(100);
//		System.out.println("rotating");
//		System.out.println(compassPilot.getHeading());
//		compassPilot.rotate(90);
//		System.out.println(compassPilot.getHeading());
		Button.waitForAnyPress();
		System.exit(0);
	}

}
