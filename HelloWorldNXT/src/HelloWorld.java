import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.BasicMotorPort;
import lejos.nxt.MotorPort;
import lejos.nxt.LCD;
import lejos.util.Delay;

public class HelloWorld {
	public static void main(String[] args) {
		// uncomment the line below that represents the
		// action you want the bot to perform.
		line();
		//rectangle();
		//circle();
		//figureEight();
		
		Delay.msDelay(2000);
	}
	
	private static void rectangle() {
		driveStraight();
		Delay.msDelay(1500);
		turnRight();
		driveStraight();
		Delay.msDelay(700);
		turnRight();
		driveStraight();
		Delay.msDelay(1500);
		turnRight();
		driveStraight();
		Delay.msDelay(700);
		turnRight();
	}
	
	/**
	 * Draw a straight line.
	 */
	private static void line() {
		driveStraight();
	}
	
	/**
	 * Drive straight.
	 */
	private static void driveStraight() {
		MotorPort.A.controlMotor(84, BasicMotorPort.FORWARD);
		MotorPort.C.controlMotor(80, BasicMotorPort.FORWARD);
	}
	
	/**
	 * Drive in a circle to the right(clockwise)
	 */
	private static void circle() {
		circleRight();
	}
	
	/**
	 * Draw a figure eight. Starting in the middle, and beginning
	 * circling to the right.
	 */
	private static void figureEight() {
		circleRight();
		circleLeft();
	}
	
	/**
	 * Turn to the right.
	 */
	private static void turnRight(){
		MotorPort.A.controlMotor(0, BasicMotorPort.FORWARD);
		MotorPort.C.controlMotor(80, BasicMotorPort.FORWARD);
		Delay.msDelay(1070);
		MotorPort.A.controlMotor(100, BasicMotorPort.STOP);
		MotorPort.C.controlMotor(100, BasicMotorPort.STOP);
	}
	
	/**
	 * Circle to the right.
	 */
	private static void circleRight() {
		MotorPort.A.controlMotor(70, BasicMotorPort.FORWARD);
		MotorPort.C.controlMotor(80, BasicMotorPort.FORWARD);
		Delay.msDelay(7600);
		MotorPort.A.controlMotor(100, BasicMotorPort.STOP);
		MotorPort.C.controlMotor(100, BasicMotorPort.STOP);
		Delay.msDelay(50);
	}
	
	/**
	 * Circle to the left.
	 */
	private static void circleLeft() {
		MotorPort.A.controlMotor(85, BasicMotorPort.FORWARD);
		MotorPort.C.controlMotor(69, BasicMotorPort.FORWARD);
		Delay.msDelay(7000);
		MotorPort.A.controlMotor(100, BasicMotorPort.STOP);
		MotorPort.C.controlMotor(100, BasicMotorPort.STOP);
		Delay.msDelay(50);
	}
}