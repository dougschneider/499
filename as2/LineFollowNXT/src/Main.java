import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
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

		LightSensor sensor = new LightSensor(SensorPort.S4);
		MotorPort leftMotor = MotorPort.C;
		MotorPort rightMotor = MotorPort.A;
		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
		oldRightTacho = rightMotor.getTachoCount();
		oldLeftTacho = leftMotor.getTachoCount();
		leftMotor.controlMotor(20, MotorPort.FORWARD);
		rightMotor.controlMotor(20, MotorPort.FORWARD);
		ArrayList<Data> data = new ArrayList<Data>();
		while (time < 5000) {
			Delay.msDelay(delay);
			time += delay;

			rightTacho = rightMotor.getTachoCount();
			leftTacho = leftMotor.getTachoCount();
			data.add(new Data(rightTacho - oldRightTacho, leftTacho
					- oldLeftTacho, time, sensor.getLightValue()));
			oldRightTacho = rightTacho;
			oldLeftTacho = leftTacho;
		}

		DataOutputStream out = null;
		try {
			out = new DataOutputStream(
					new FileOutputStream(new File("log.csv"), true));
		} catch (FileNotFoundException e) {
			System.out.println("1");
			e.printStackTrace();
		}

		try {
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

class Data {
	public int rightTacho;
	public int leftTacho;
	public int time;
	public int intensity;

	public Data(int rightTacho, int leftTacho, int time, int intensity) {
		this.rightTacho = rightTacho;
		this.leftTacho = leftTacho;
		this.time = time;
		this.intensity = intensity;
	}

	public String getLine() {
		String classStr = rightTacho - leftTacho < 0 ? "FALSE" : "TRUE";
		return intensity + "," + classStr + "\n";
	}
}
