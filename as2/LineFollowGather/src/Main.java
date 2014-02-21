import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;

public class Main {
	public static void main(String[] args) {
		gatherData();
	}

	public static void gatherData() {
		int time = 0;// ms
		int delay = 10;// ms
		int oldRightTacho = 0;
		int oldLeftTacho = 0;
		int rightTacho = 0;
		int leftTacho = 0;

        // initialize the light sensor
		LightSensor rightSensor = new LightSensor(SensorPort.S1);

        // initialize the tachometers
		MotorPort leftMotor = MotorPort.C;
		MotorPort rightMotor = MotorPort.A;
		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
		oldRightTacho = rightMotor.getTachoCount();
		oldLeftTacho = leftMotor.getTachoCount();

        // float so we can manually get data
		leftMotor.controlMotor(100, MotorPort.FLOAT);
		rightMotor.controlMotor(100, MotorPort.FLOAT);
		ArrayList<Data> data = new ArrayList<Data>();
		while (true) {
			Delay.msDelay(delay);
			time += delay;

			rightTacho = rightMotor.getTachoCount();
			leftTacho = leftMotor.getTachoCount();
            // if the tachometers have moved, add a new data point
			if(rightTacho - oldRightTacho != 0 && leftTacho - oldLeftTacho != 0)
			{
				data.add(new Data(rightTacho - oldRightTacho, leftTacho
						- oldLeftTacho, time, rightSensor.getLightValue()));
			}
			oldRightTacho = rightTacho;
			oldLeftTacho = leftTacho;

            // if left button is pressed stop gathering data
			if(Button.LEFT.isDown())
				break;
		}

        // open a file to write data to
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(
					new FileOutputStream(new File("logp1.csv"), true));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
            // write each data point to file
			for (Data d : data) {
				out.write(d.getLine().getBytes());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		leftMotor.controlMotor(100, MotorPort.STOP);
		rightMotor.controlMotor(100, MotorPort.STOP);
	}
}

/**
 * This is a data class used to store data points gathered
 * by the Main class.
 */
class Data {
	public int rightTacho;
	public int leftTacho;
	public int time;
	public int rightIntensity;

	public Data(int rightTacho, int leftTacho, int time, int rightIntensity) {
		this.rightTacho = rightTacho;
		this.leftTacho = leftTacho;
		this.time = time;
		this.rightIntensity = rightIntensity;
	}

	public String getLine() {
        // return the string representing this data point
		String classStr = rightTacho - leftTacho < 0 ? "LEFT" : "RIGHT";
		return rightIntensity + "," + classStr + "\n";
	}
}
