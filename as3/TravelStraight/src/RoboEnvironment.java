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

	@Override
	public double getReward(IState s1, IState s2, IAction a) {
		int bucket = s2.hashCode();
		int reward = -1000;
		
		if (bucket > CENTER_CM) {
			reward = CENTER_CM - bucket;
		} else {
			reward = bucket - CENTER_CM;
		}
		
		System.out.println("\tBucket: "	+ bucket + "\tReward: " + reward + "\tAction: " + a.toString());
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
