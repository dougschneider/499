import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.util.Delay;

public class Main {

	public static void main(String[] args) {
		RemoteControlGUI gui = new RemoteControlGUI();
		gui.setVisible(true);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// do something trivial to initialize connection
		Sound.beep();
		
		gatherData();
	}

	public static void gatherData() {
		int time = 0;// ms
		int delay = 10;// ms
		int oldRightTacho = 0;
		int oldLeftTacho = 0;
		int rightTacho = 0;
		int leftTacho = 0;

		LightSensor leftSensor = new LightSensor(SensorPort.S4);
		LightSensor rightSensor = new LightSensor(SensorPort.S1);
		MotorPort leftMotor = MotorPort.C;
		MotorPort rightMotor = MotorPort.A;
		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
		oldRightTacho = rightMotor.getTachoCount();
		oldLeftTacho = leftMotor.getTachoCount();
		ArrayList<Data> data = new ArrayList<Data>();
		while (time < 5000) {
			Delay.msDelay(delay);
			time += delay;

			rightTacho = rightMotor.getTachoCount();
			leftTacho = leftMotor.getTachoCount();
			if(rightTacho - oldRightTacho != 0 && leftTacho - oldLeftTacho != 0)
			{
				data.add(new Data(rightTacho - oldRightTacho, leftTacho
						- oldLeftTacho, time, rightSensor.getLightValue(),
						leftSensor.getLightValue()));
			}
			oldRightTacho = rightTacho;
			oldLeftTacho = leftTacho;
		}

		DataOutputStream out = null;
		try {
			out = new DataOutputStream(
					new FileOutputStream(new File("tmp.csv"), true));
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
	public int rightIntensity;
	public int leftIntensity;

	public Data(int rightTacho, int leftTacho, int time, int rightIntensity, int leftIntensity) {
		this.rightTacho = rightTacho;
		this.leftTacho = leftTacho;
		this.time = time;
		this.rightIntensity = rightIntensity;
		this.leftIntensity = leftIntensity;
	}

	public String getLine() {
		String classStr = rightTacho - leftTacho < 0 ? "FALSE" : "TRUE";
		return rightIntensity + "," + leftIntensity + "," + classStr + "\n";
	}
}