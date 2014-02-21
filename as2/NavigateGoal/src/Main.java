import java.io.File;

import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;


public class Main {
	public static void main(String[] args) {
		DataSource source;
		Instances data = null;
        // read in the training data
		try {
			CSVLoader loader = new CSVLoader();
			loader.setSource(new File("../data/logp2.csv"));
			data = loader.getDataSet();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		data.setClassIndex(data.numAttributes()-1);
		
        // uncomment one of the following lines based on what
        // model we want to use
    	MultilayerPerceptron mlp = new MultilayerPerceptron();
//		J48 dt = new J48();
		try {
            // uncomment one of the following lines based on what
            // model we want to use
			mlp.buildClassifier(data);
//			dt.buildClassifier(data);
		} catch (Exception e) {
			e.printStackTrace();
		}

        // initialize the sensors
		LightSensor rightSensor = new LightSensor(SensorPort.S1);
		UltrasonicSensor leftSensor = new UltrasonicSensor(SensorPort.S4);
		MotorPort rightMotor = MotorPort.A;
		MotorPort leftMotor = MotorPort.C;
		while(true)
		{
            // create a new instance using sensor data
			Instance i = new SparseInstance(3);
			i.setValue(0, rightSensor.getLightValue());
			i.setValue(1, leftSensor.getDistance());
			i.setDataset(data);
			try {
                // uncomment one of the following lines based on what
                // model we want to use
				double instanceClass = mlp.classifyInstance(i);
//				double instanceClass = dt.classifyInstance(i);

  				// classify the created instance
				if (instanceClass == 0.0) {//ROTATE
					leftMotor.controlMotor(30, MotorPort.BACKWARD);
					rightMotor.controlMotor(30, MotorPort.FORWARD);
				}
				else if (instanceClass == 2.0) {//LEFT
					leftMotor.controlMotor(100, MotorPort.STOP);
					rightMotor.controlMotor(60, MotorPort.FORWARD);
				} else if(instanceClass == 1.0) {//RIGHT
					leftMotor.controlMotor(30, MotorPort.FORWARD);
					rightMotor.controlMotor(100, MotorPort.STOP);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
