import agents.LoneAgent;
import algorithms.ISelector;
import environment.IAction;
import environment.IEnvironmentSingle;


public class RoboAgent extends LoneAgent {
	
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
}
