import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.BasicMotorPort;
import lejos.nxt.MotorPort;
import lejos.nxt.LCD;
import lejos.util.Delay;

public class HelloWorld {
	public static void main(String[] args) {
		//line();
		//rectangle();
		//circle();
		//figureEight();

		SensorPort.S2.setTypeAndMode(SensorPort.TYPE_SWITCH, SensorPort.MODE_BOOLEAN);
		
		// stuff to display tach count and wait
		//Delay.msDelay(2000);
		while(true) {
			Delay.msDelay(500);
			System.out.println(SensorPort.S2.readValue());
		}
		//MotorPort.A.getTachoCount();
		//MotorPort.C.getTachoCount();
		//LCD.drawInt(MotorPort.A.getTachoCount(), 0, 0);
		//LCD.drawInt(MotorPort.C.getTachoCount(), 7, 0);
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
	
	private static void line() {
		driveStraight();
	}
	
	private static void driveStraight() {
		MotorPort.A.controlMotor(84, BasicMotorPort.FORWARD);
		MotorPort.C.controlMotor(80, BasicMotorPort.FORWARD);
	}
	
	private static void circle() {
		circleRight();
		//circleLeft();
	}
	
	private static void figureEight() {
		circleRight();
		circleLeft();
	}
	
	private static void turnRight(){
		MotorPort.A.controlMotor(0, BasicMotorPort.FORWARD);
		MotorPort.C.controlMotor(80, BasicMotorPort.FORWARD);
		Delay.msDelay(1070);
		MotorPort.A.controlMotor(100, BasicMotorPort.STOP);
		MotorPort.C.controlMotor(100, BasicMotorPort.STOP);
	}
	
	private static void circleRight() {
		MotorPort.A.controlMotor(70, BasicMotorPort.FORWARD);
		MotorPort.C.controlMotor(80, BasicMotorPort.FORWARD);
		//Delay.msDelay(4700);
		Delay.msDelay(7000);
		MotorPort.A.controlMotor(100, BasicMotorPort.STOP);
		MotorPort.C.controlMotor(100, BasicMotorPort.STOP);
		Delay.msDelay(50);
	}
	private static void circleLeft() {
		MotorPort.A.controlMotor(85, BasicMotorPort.FORWARD);
		MotorPort.C.controlMotor(69, BasicMotorPort.FORWARD);
		//Delay.msDelay(4950);
		Delay.msDelay(7200);
		MotorPort.A.controlMotor(100, BasicMotorPort.STOP);
		MotorPort.C.controlMotor(100, BasicMotorPort.STOP);
		Delay.msDelay(50);
	}
}