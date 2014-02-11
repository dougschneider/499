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
		try {
			CSVLoader loader = new CSVLoader();
			loader.setSource(new File("../../logp2.csv"));
			data = loader.getDataSet();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		data.setClassIndex(data.numAttributes()-1);
		
//		MultilayerPerceptron mlp = new MultilayerPerceptron();
		J48 dt = new J48();
		try {
//			mlp.buildClassifier(data);
			dt.buildClassifier(data);
//			Evaluation eval = new Evaluation(data);
//			eval.crossValidateModel(mlp, data, 10, new Random(1));
//			System.out.println(eval.pctCorrect());
		} catch (Exception e) {
			e.printStackTrace();
		}

		LightSensor rightSensor = new LightSensor(SensorPort.S1);
		UltrasonicSensor leftSensor = new UltrasonicSensor(SensorPort.S4);
		MotorPort rightMotor = MotorPort.A;
		MotorPort leftMotor = MotorPort.C;
		int current = 0;
		int delta = 3;
		while(current < 10000)
		{
			current += delta;
			Instance i = new SparseInstance(3);
			i.setValue(0, rightSensor.getLightValue());
			i.setValue(1, leftSensor.getDistance());
			i.setDataset(data);
			try {
//				double instanceClass = mlp.classifyInstance(i);
				double instanceClass = dt.classifyInstance(i);
				System.out.println(instanceClass);
				if (instanceClass == 0.0) {
					leftMotor.controlMotor(30, MotorPort.BACKWARD);
					rightMotor.controlMotor(30, MotorPort.FORWARD);
				}
				else if (instanceClass == 2.0) {
					leftMotor.controlMotor(100, MotorPort.STOP);
					rightMotor.controlMotor(60, MotorPort.FORWARD);
				} else if(instanceClass == 1.0) {
					leftMotor.controlMotor(30, MotorPort.FORWARD);
					rightMotor.controlMotor(100, MotorPort.STOP);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
