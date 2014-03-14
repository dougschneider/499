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


public class RoboEnvironment extends AbstractEnvironmentSingle {

    private SMO smo;
    private Instances data;
	
	public RoboEnvironment() {
		super();
        buildClassifier();
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
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 45 cm is centre
		int front = SensorController.getFrontDist();
		int back = SensorController.getBackDist();
		
		int frontFromCentre = Math.abs(front-45);
		int backFromCentre = Math.abs(back-45);
		int fromCentre = (int) (backFromCentre+frontFromCentre)/2;
		
		return -fromCentre;
	}

	@Override
	public boolean isFinal(IState s) {
        // set up the light sensor on the NXT
		MotorPort rightMotor = MotorPort.A;
		MotorPort leftMotor = MotorPort.C;

        SensorState st = (SensorState) s;
        Instance i = new SparseInstance(2);
        i.setValue(0, st.lightValue);
        i.setDataset(data);
        try {
            // classify the instance and take the corresponding action
            if (smo.classifyInstance(i) == 1.0) {// end episode
                System.out.println("TERMINAL");
                return true;
            } else {// continue
                System.out.println("NOT TERMINAL");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("NOT TERMINAL");
        return false;
	}

    private void buildClassifier()
    {
		DataSource source;
		data = null;
        // load in the training data
		try {
			CSVLoader loader = new CSVLoader();
			loader.setSource(new File("../p3data.csv"));
			data = loader.getDataSet();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		data.setClassIndex(data.numAttributes()-1);
		
        // create the model
		smo = new SMO();
		String[] options = {"-C 1.0", "-L 0.001", "-P 1.0E-12", "-N 0", "-V -1", "-W 1", "-K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\""};
		try {
			smo.setOptions(options);
            // train the model
			smo.buildClassifier(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	@Override
	public int whoWins(IState s) {
		// TODO Auto-generated method stub
		return 0;
	}

}
