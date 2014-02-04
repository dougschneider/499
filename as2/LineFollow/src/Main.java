import java.util.ArrayList;

import javax.swing.JFrame;

import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.util.Delay;

public class Main {

	public static void main(String[] args) {
//		Thread t = new Thread(new GUIThread());
//		t.start();
		Sound.beep();
		gatherData();
	}
	
	public static void gatherData()
	{
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
		while(time < 2000)
		{
			Delay.msDelay(delay);
			time += delay;
			System.out.println("FOO");
			
			rightTacho = rightMotor.getTachoCount();
			leftTacho = leftMotor.getTachoCount();
//			data.add(new Data(
//					rightTacho - oldRightTacho,
//					leftTacho - oldLeftTacho,
//					time,
//					sensor.getLightValue()));
//			oldRightTacho = rightTacho;
//			oldLeftTacho = leftTacho;
		}
		
		for(Data d : data)
		{
			d.print();
		}
		
		leftMotor.controlMotor(100, MotorPort.STOP);
		rightMotor.controlMotor(100, MotorPort.STOP);
	}
}

class GUIThread implements Runnable
{
	@Override
	public void run() {
		RemoteControlGUI gui = new RemoteControlGUI();
		gui.setVisible(true);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class Data
{
	public int rightTacho;
	public int leftTacho;
	public int time;
	public int intensity;
	
	public Data(int rightTacho, int leftTacho, int time, int intensity)
	{
		this.rightTacho = rightTacho;
		this.leftTacho = leftTacho;
		this.time = time;
		this.intensity = intensity;
	}
	
	public void print()
	{
		System.out.println(time + " " + rightTacho + " " + leftTacho + " " + intensity);
	}
}
