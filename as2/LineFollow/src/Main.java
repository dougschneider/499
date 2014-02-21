import java.io.File;

import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.util.Delay;
import weka.classifiers.functions.SMO;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;


public class Main {

	public static void main(String[] args) {
		DataSource source;
		Instances data = null;
        // load in the training data
		try {
			CSVLoader loader = new CSVLoader();
			loader.setSource(new File("../data/logp1.csv"));
			data = loader.getDataSet();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		data.setClassIndex(data.numAttributes()-1);
		
        // create the model
		SMO smo = new SMO();
		String[] options = {"-C 1.0", "-L 0.001", "-P 1.0E-12", "-N 0", "-V -1", "-W 1", "-K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\""};
		try {
			smo.setOptions(options);
            // train the model
			smo.buildClassifier(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        // set up the light sensor on the NXT
		LightSensor sensor = new LightSensor(SensorPort.S1);
		MotorPort rightMotor = MotorPort.A;
		MotorPort leftMotor = MotorPort.C;
        // follow the line forever
		while(true)
		{
			int val = sensor.getLightValue();
			Instance i = new SparseInstance(2);
			i.setValue(0, val);
			i.setDataset(data);
			try {
                // classify the instance and take the corresponding action
				if (smo.classifyInstance(i) == 1.0) {// RIGHT
					leftMotor.controlMotor(100, MotorPort.STOP);
					rightMotor.controlMotor(30, MotorPort.FORWARD);
				} else {// LEFT
					leftMotor.controlMotor(30, MotorPort.FORWARD);
					rightMotor.controlMotor(100, MotorPort.STOP);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
