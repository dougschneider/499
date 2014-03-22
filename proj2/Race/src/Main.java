import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;


public class Main {

	public static void main(String[] args) {

		LightSensor lightSensor = new LightSensor(SensorPort.S1);
		
		Racer racer = new Racer(lightSensor, Motor.A, Motor.C);
		racer.race();
	}

}
