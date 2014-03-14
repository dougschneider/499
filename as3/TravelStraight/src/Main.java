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
		if(args.length == 0)
		{
			System.out.println("learning");
			sel = new QLearningSelector();
			sel.setEpsilonGreedy();
			sel.setGamma(0.3);
			sel.setEpsilon(0.8);
			sel.setAlpha(0.1);
			agent = new RoboAgent(env, sel);
			agent.enableLearning();
		}
		else
		{
			System.out.println("testing");
			learning = false;
			agent = RoboAgent.readAgent(args[0], env);
			sel = (QLearningSelector) agent.getAlgorithm();
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
			agent.saveAgent("../test");
			System.out.println("done writing agent.");
		}
	}

}
