import lejos.nxt.MotorPort;
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
        stop();
		return action;
	}

    private void goStraight()
    {
        SensorController.controlLeftMotor(20, MotorPort.FORWARD);
        SensorController.controlRightMotor(20, MotorPort.FORWARD);
        Delay.msDelay(100);
    }

    private void angleLeft()
    {
        SensorController.controlLeftMotor(20, MotorPort.FORWARD);
        SensorController.controlRightMotor(20, MotorPort.BACKWARD);
        Delay.msDelay(50);
    }

    private void angleRight()
    {
        SensorController.controlLeftMotor(20, MotorPort.BACKWARD);
        SensorController.controlRightMotor(20, MotorPort.FORWARD);
        Delay.msDelay(50);
    }

    private void stop()
    {
        SensorController.controlLeftMotor(100, MotorPort.STOP);
        SensorController.controlRightMotor(100, MotorPort.STOP);
    }
}
