import java.io.File;

import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import referees.OnePlayerReferee;
import agents.IAgent;
import algorithms.WatkinsSelector;
import environment.IEnvironmentSingle;


public class Main {

	public static void main(String[] args) {
		IAgent agent = null;
		boolean learning = true;

		WatkinsSelector sel = null;
		IEnvironmentSingle env = new RoboEnvironment();
		
		// load an agent for training/learning if it exists
		if(new File("../p3.agt").exists())
		{
			agent = RoboAgent.readAgent("../p3", env);
			sel = (WatkinsSelector) agent.getAlgorithm();
		}
		else// otherwise create a new one
		{
			sel = new WatkinsSelector(0.7);
			agent = new RoboAgent(env, sel);
		}
		
		// if we're learning
		if(args.length == 0)
		{
			System.out.println("learning");
			sel.setEpsilonGreedy();
			sel.setGamma(0.7);
			sel.setEpsilon(0.5);
			sel.setAlpha(0.1);
			((WatkinsSelector)sel).setlambda(0.7);
			agent.enableLearning();
		}
		else// otherwise we're testing
		{
			System.out.println("testing");
			learning = false;
			sel.setEpsilonGreedy();
			sel.setEpsilon(0);
			sel.setAlpha(0);
			agent.freezeLearning();
		}
		
		// create a ref to run our episodes
		OnePlayerReferee ref = new OnePlayerReferee(agent);
		ref.setMaxIter(100000000);

		// learn until we're out of episodes or we choose to terminate
		boolean done = false;
		while(true)
		{
			// run an episode
			int length = ref.episode(env.defaultInitialState());
			
			// stop between episodes
			SensorController.controlLeftMotor(100, MotorPort.STOP);
			SensorController.controlRightMotor(100, MotorPort.STOP);
			
			// print the average reward for the last episode
			System.out.println("Episode Reward: " + ref.getRewardForEpisode()/(length*1.0));
			
			// wait for further instruction
			while(true)
			{
				// press right to start a new episode
				if(Button.RIGHT.isDown())
					break;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// press left to quit
				if(Button.LEFT.isDown())
				{
					done = true;
					break;
				}
			}
			
			// if we're done, quit
			if(done)
				break;
		}

		// stop at the end of learning/testing
		MotorPort.A.controlMotor(100, MotorPort.STOP);
		MotorPort.C.controlMotor(100, MotorPort.STOP);
		
		// if we're learning, save the learned agent
		if(learning)
		{
			agent.saveAgent("../p3");
			System.out.println("done writing agent.");
		}
		System.exit(0);
	}

}
