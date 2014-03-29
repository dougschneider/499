import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;


public class Main {

	public static void main(String[] args) {

		LightSensor lightSensor = new LightSensor(SensorPort.S1);
		LightSensor targetSensor = new LightSensor(SensorPort.S2);
		
		Racer racer = new Racer(lightSensor, targetSensor, MotorPort.A, MotorPort.C);
		racer.race();
	}

}
