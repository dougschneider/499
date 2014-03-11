import environment.AbstractEnvironmentSingle;
import environment.ActionList;
import environment.IAction;
import environment.IState;


public class RoboEnvironment extends AbstractEnvironmentSingle {
	
	public RoboEnvironment() {
		super();
	}
	
	@Override
	public ActionList getActionList(IState s) {
		ActionList l = new ActionList(s);
		l.add(new RoboAction(RoboAction.STRAIGHT));
		l.add(new RoboAction(RoboAction.LEFT));
		l.add(new RoboAction(RoboAction.RIGHT));
		return l;
	}

	@Override
	public IState successorState(IState s, IAction a) {
		return new SensorState(this, SensorController.getFrontDist()/10, SensorController.getBackDist()/10);
	}

	@Override
	public IState defaultInitialState() {
		return new SensorState(this, SensorController.getFrontDist()/10, SensorController.getBackDist()/10);
	}

	@Override
	public double getReward(IState s1, IState s2, IAction a) {
		// 45 cm is centre
		double front = SensorController.getFrontDist()/10;
		double back = SensorController.getBackDist()/10;
		
		double frontFromCentre = Math.abs(front-45);
		double backFromCentre = Math.abs(back-45);
		double fromCentre = (backFromCentre+frontFromCentre)/2.0;
		
		double difference = Math.abs(front-back);
		
		return -Math.abs((fromCentre*10) + difference);
	}

	@Override
	public boolean isFinal(IState s) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int whoWins(IState s) {
		// TODO Auto-generated method stub
		return 0;
	}

}
