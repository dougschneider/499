import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import controllers.Controllers;

public class LineFollow {

	public static void main(String[] args) {
		
		Sound.beep();
		LightSensor sensor = new LightSensor(SensorPort.S1);
		sensor.setFloodlight(true);
		
		MotorPort leftMotor = MotorPort.C;
		MotorPort rightMotor = MotorPort.A;
		Controllers.bangBang(sensor, leftMotor, rightMotor);
		Controllers.P(3, 0, 0, sensor, leftMotor, rightMotor);
		Controllers.PD(1.8, 0, 1.5, sensor, leftMotor, rightMotor);
		Controllers.PI(2, 0.1, 0, sensor, leftMotor, rightMotor);
		Controllers.PID(1.8, 0.1, 0.3, sensor, leftMotor, rightMotor);
	}
}
