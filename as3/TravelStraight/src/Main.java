import java.io.File;
import java.io.FileInputStream;

import referees.OnePlayerReferee;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import agents.IAgent;
import algorithms.QLearningSelector;
import algorithms.WatkinsSelector;
import environment.IEnvironmentSingle;


public class Main {

	public static void main(String[] args) {
		IAgent agent = null;
		boolean learning = true;

		QLearningSelector sel = null;
		IEnvironmentSingle env = new RoboEnvironment();
		
		if(new File("../p3.agt").exists())
		{
			agent = RoboAgent.readAgent("../p3", env);
			sel = (QLearningSelector) agent.getAlgorithm();
		}
		else
		{
			sel = new QLearningSelector();
			agent = new RoboAgent(env, sel);
		}
		
		if(args.length == 0)
		{
			System.out.println("learning");
			sel.setEpsilonGreedy();
			sel.setGamma(0.7);
			sel.setEpsilon(0.5);
			sel.setAlpha(0.05);
			agent.enableLearning();
		}
		else
		{
			System.out.println("testing");
			learning = false;
			sel.setEpsilonGreedy();
			sel.setEpsilon(0);
			sel.setAlpha(0);
			agent.freezeLearning();
		}
		
		OnePlayerReferee ref = new OnePlayerReferee(agent);
		ref.setMaxIter(100000000);

		boolean done = false;
		while(true)
		{
			int length = ref.episode(env.defaultInitialState());
			SensorController.controlLeftMotor(100, MotorPort.STOP);
			SensorController.controlRightMotor(100, MotorPort.STOP);
			System.out.println(ref.getRewardForEpisode()/(length*1.0));
			while(true)
			{
				if(Button.RIGHT.isDown())
					break;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(Button.LEFT.isDown())
				{
					done = true;
					break;
				}
			}
			if(done)
				break;
		}

		MotorPort.A.controlMotor(100, MotorPort.STOP);
		MotorPort.C.controlMotor(100, MotorPort.STOP);
		
		if(learning)
		{
			agent.saveAgent("../p3");
			System.out.println("done writing agent.");
		}
	}

}
