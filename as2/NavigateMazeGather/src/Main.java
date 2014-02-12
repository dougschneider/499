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
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.OpticalDistanceSensor;
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

		UltrasonicSensor xSensor = new UltrasonicSensor(SensorPort.S4);
		TouchSensor rightTouch = new TouchSensor(SensorPort.S3);
		TouchSensor leftTouch = new TouchSensor(SensorPort.S2);
		xSensor.getDistance();
		Delay.msDelay(1000);
		LightSensor rightSensor = new LightSensor(SensorPort.S1);
		MotorPort leftMotor = MotorPort.C;
		MotorPort rightMotor = MotorPort.A;
		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
		oldRightTacho = rightMotor.getTachoCount();
		oldLeftTacho = leftMotor.getTachoCount();
		leftMotor.controlMotor(100, MotorPort.FLOAT);
		rightMotor.controlMotor(100, MotorPort.FLOAT);
		ArrayList<Data> data = new ArrayList<Data>();
		while (true) {
			Delay.msDelay(delay);
			time += delay;

			rightTacho = rightMotor.getTachoCount();
			leftTacho = leftMotor.getTachoCount();
			if(rightTacho - oldRightTacho != 0 && leftTacho - oldLeftTacho != 0)
			{
				data.add(new Data(rightTacho - oldRightTacho, leftTacho
						- oldLeftTacho, time, rightSensor.getLightValue(),
						xSensor.getDistance(), rightTouch.isPressed(), leftTouch.isPressed()));
			}
			oldRightTacho = rightTacho;
			oldLeftTacho = leftTacho;
			if(Button.LEFT.isDown())
				break;
		}

		DataOutputStream out = null;
		try {
			out = new DataOutputStream(
					new FileOutputStream(new File("logp3.csv"), true));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			out.write("rightIntensity,xdistance,rightTouch,leftTouch,class\n".getBytes());
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
	public int xdistance;
	public boolean rightTouch;
	public boolean leftTouch;

	public Data(int rightTacho, int leftTacho, int time, int rightIntensity, int xdistance, boolean rightTouch, boolean leftTouch) {
		this.rightTacho = rightTacho;
		this.leftTacho = leftTacho;
		this.time = time;
		this.rightIntensity = rightIntensity;
		this.xdistance = xdistance;
		this.rightTouch = rightTouch;
		this.leftTouch = leftTouch;
	}

	public String getLine() {
		String classStr;
		if(leftTacho < 0 && rightTacho >= 0)
			classStr = "BACKLEFT";
		else if(leftTacho >= 0 && rightTacho < 0)
			classStr = "BACKRIGHT";
		else if(leftTacho == rightTacho)
			classStr = "FORWARD";
		else
			classStr = rightTacho - leftTacho < 0 ? "BACKLEFT" : "BACKRIGHT";
		return rightIntensity + "," + xdistance + "," + (rightTouch ? "TRUE" : "FALSE") + "," + (leftTouch ? "TRUE" : "FALSE") + "," + classStr + "\n";
	}
}
