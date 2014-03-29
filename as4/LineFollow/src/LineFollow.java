import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import controllers.Controllers;

public class LineFollow {

	public static void main(String[] args) {
		
		Sound.beep();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LightSensor sensor = new LightSensor(SensorPort.S1);
		sensor.setFloodlight(true);
		
		MotorPort leftMotor = MotorPort.C;
		MotorPort rightMotor = MotorPort.A;
		Controllers c = new Controllers();
		c.bangBang(sensor, leftMotor, rightMotor);
//		c.P(33, 3, sensor, leftMotor, rightMotor);
//		c.PD(34, 3, 1, sensor, leftMotor, rightMotor);
//		c.PI(32, 3, 0.1, sensor, leftMotor, rightMotor);
//		c.PID(33, 3, 0.1, 0.3, sensor, leftMotor, rightMotor);
	}
}
