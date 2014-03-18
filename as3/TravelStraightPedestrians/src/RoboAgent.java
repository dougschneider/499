import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.util.Delay;
import agents.LoneAgent;
import algorithms.ISelector;
import environment.IAction;
import environment.IEnvironmentSingle;


public class RoboAgent extends LoneAgent {
	
	private DifferentialPilot pilot;

	public RoboAgent(IEnvironmentSingle s, ISelector al) {
		super(s, al);
	}
	
	@Override
	public IAction act()
	{
        // assume we're always facing straight to begin with
		RoboAction action = (RoboAction) super.act();
		if(action.direction == RoboAction.STRAIGHT)
		{
            goStraight();
		}
		else if(action.direction == RoboAction.LEFT)
		{
            angleLeft();
            goStraight();
            angleRight();
		}
		else// RIGHT
		{
            angleRight();
            goStraight();
            angleLeft();
		}
		return action;
	}

    private void goStraight()
    {
    	SensorController.goStraight();
    }

    private void angleLeft()
    {
    	SensorController.rotateLeft();
    }

    private void angleRight()
    {
    	SensorController.rotateRight();
    }

    private void stop()
    {
        SensorController.controlLeftMotor(100, MotorPort.STOP);
        SensorController.controlRightMotor(100, MotorPort.STOP);
        Delay.msDelay(10);
    }
}
