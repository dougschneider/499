import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;


public class Racer
{
	private static final int NORMAL_ZONE_READING = 45;
	private static final int SPECIAL_ZONE_READING = 40;
	private static final int INNER_EDGE_READING = 30;
	private static final int OUTER_EDGE_READING = 24;
	
	private static double wheelDiameter = 56;
	private static double trackWidth = 120;

	private LightSensor lightSensor;
	private LightSensor targetSensor;
	private MotorPort rightMotor;
	private MotorPort leftMotor;
	
	public Racer(LightSensor lightSensor, LightSensor targetSensor,
			      MotorPort rightMotor, MotorPort leftMotor)
	{
		this.lightSensor = lightSensor;
		this.lightSensor.setFloodlight(true);
		this.targetSensor = targetSensor;
		this.targetSensor.setFloodlight(true);
		
		this.rightMotor = rightMotor;
		this.leftMotor = leftMotor;
	}
	
	public void race()
	{
//		while(true)
//		{
//			System.out.println(lightSensor.getLightValue());
//			System.out.println(targetSensor.getLightValue());
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		this.PID(3.5, 0.11, 3);
	}
	
	private void PID(double Kp, double Ki, double Kd) {
		targetSensor.setFloodlight(true);
		int error = 0;
		int lastError = 0;

		int basePower = 15;

		double integral = 0;
		double derivative = 0;

		while (true) {
			int targetValue = targetSensor.getLightValue()-4;
			if(targetValue > 42)
				targetValue -= 4;
			else
				targetValue -= 3;
			int current = lightSensor.getLightValue();
			System.out.println(current);
			error = current - targetValue;

			integral = integral + error;
			derivative = error - lastError;

			int turn = (int) Math.round(Kp * error + Ki * integral + Kd
					* derivative);

			leftMotor.controlMotor(basePower + turn, MotorPort.FORWARD);
			rightMotor.controlMotor(basePower - turn, MotorPort.FORWARD);
			lastError = error;
		}
	}
}
