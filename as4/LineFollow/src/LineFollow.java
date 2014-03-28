import lejos.nxt.LightSensor;
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
		Controllers c = new Controllers();
		c.bangBang(sensor, leftMotor, rightMotor);
		c.P(3, sensor, leftMotor, rightMotor);
		c.PD(1.8, 1.5, sensor, leftMotor, rightMotor);
		c.PI(2, 0.1, sensor, leftMotor, rightMotor);
		c.PID(1.8, 0.1, 0.3, sensor, leftMotor, rightMotor);
	}
}
