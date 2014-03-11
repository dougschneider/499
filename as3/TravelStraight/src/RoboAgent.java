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
		RoboAction action = (RoboAction) super.act();
		if(action.direction == RoboAction.STRAIGHT)
		{
			SensorController.controlLeftMotor(40, MotorPort.FORWARD);
			SensorController.controlRightMotor(40, MotorPort.FORWARD);
		}
		else if(action.direction == RoboAction.LEFT)
		{
			SensorController.controlLeftMotor(40, MotorPort.FORWARD);
			SensorController.controlRightMotor(20, MotorPort.FORWARD);
		}
		else// RIGHT
		{
			SensorController.controlLeftMotor(20, MotorPort.FORWARD);
			SensorController.controlRightMotor(40, MotorPort.FORWARD);
		}
		return action;
	}
}
