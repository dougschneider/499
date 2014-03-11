import lejos.nxt.Button;
import lejos.nxt.MotorPort;
import lejos.util.Delay;
import agents.IAgent;
import algorithms.ISelector;
import algorithms.QLearningSelector;
import algorithms.WatkinsSelector;
import environment.IEnvironmentSingle;


public class Main {

	public static void main(String[] args) {
		IAgent agent = null;
		boolean learning = true;
		
		WatkinsSelector sel = null;
		IEnvironmentSingle env = new RoboEnvironment();
		if(args.length == 0)
		{
			System.out.println("learning");
			sel = new WatkinsSelector(0.7);
			sel.setEpsilonGreedy();
			sel.setEpsilon(1);
			sel.setAlpha(0.1);
			agent = new RoboAgent(env, sel);
			agent.enableLearning();
		}
		else
		{
			System.out.println("testing");
			learning = false;
			agent = RoboAgent.readAgent(args[0], env);
			sel = (WatkinsSelector) agent.getAlgorithm();
			sel.setEpsilonGreedy();
			sel.setEpsilon(0);
			sel.setAlpha(0);
			agent.freezeLearning();
		}
		
		while(true)
		{
			agent.act();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(agent.getLastAction());
			System.out.println();
			System.out.println(agent.getCurrentState());
			System.out.println(sel.bestAction(agent.getCurrentState()));
			if(Button.LEFT.isDown())
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
