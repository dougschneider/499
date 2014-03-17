import java.io.File;

import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import weka.classifiers.functions.SMO;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;
import environment.AbstractEnvironmentSingle;
import environment.ActionList;
import environment.IAction;
import environment.IState;


@SuppressWarnings("serial")
public class RoboEnvironment extends AbstractEnvironmentSingle {
	public static final int CENTER_CM = 55;

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
		return new SensorState(this, SensorController.getFrontDist(), SensorController.getBackDist(), SensorController.getLightValue());
	}

	@Override
	public IState defaultInitialState() {
		return new SensorState(this, SensorController.getFrontDist(), SensorController.getBackDist(), SensorController.getLightValue());
	}
	
	private static int getDiff(int distance, int middle) {
		if (distance > middle) {
			return middle - distance;
		} else {
			return distance - middle;
		}
	}

	@Override
	public double getReward(IState s1, IState s2, IAction a) {
		int reward = -1000;
		int oldbucket = s1.hashCode();
		int newbucket = s2.hashCode();
		int olddiff = getDiff(oldbucket, CENTER_CM);
		int newdiff = getDiff(newbucket, CENTER_CM);
		
		boolean action_right = ((RoboAction)a).direction == RoboAction.RIGHT;
		boolean action_left = ((RoboAction)a).direction == RoboAction.LEFT;
		boolean action_straight = ((RoboAction)a).direction == RoboAction.STRAIGHT;
		
		// less than since they're negative values
		boolean away_from_center = newdiff < olddiff;
		
		boolean left_of_center = newbucket < CENTER_CM;
		boolean right_of_center = newbucket > CENTER_CM;
		boolean center = newbucket == CENTER_CM;
		
		// moved between buckets
		if (oldbucket != newbucket) {
			if (away_from_center) {
				reward = newdiff * 2;
			} else {
				reward = newdiff;
			}
		}
		// stayed in same bucket
		else if (oldbucket == newbucket) {
			if (center) {
				if (action_straight) {
					reward = newdiff + 10;
				} else {
					reward = newdiff;
				}
			} else if ((left_of_center && action_left) || (right_of_center && action_right)) {
				reward = newdiff * 3;
			} else 	if ((left_of_center && action_straight) || (right_of_center && action_straight)) {
				reward = newdiff * 2;
			} else if ((left_of_center && action_right) || (right_of_center && action_left)) {
				reward = newdiff;
			}
		}
		
		System.out.println("Old Bucket: " + oldbucket + "\tNew Bucket: " + newbucket + "\tReward: " + reward + "\tAction: " + a.toString());
		return reward;
	}

	@Override
	public boolean isFinal(IState s) {
        SensorState st = (SensorState) s;
        
        // magic value of 30 when light sensor is over tape
        if (st.lightValue > 30) {
        	System.out.println("TERMINAL");
            return true;
        } else {
                System.out.println("NOT TERMINAL");
                return false;
        }
	}

	@Override
	public int whoWins(IState s) {
		// TODO Auto-generated method stub
		return 0;
	}

}
