import java.io.File;

import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
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
			loader.setSource(new File("../../logp3.csv"));
			data = loader.getDataSet();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		data.setClassIndex(data.numAttributes()-1);

//		MultilayerPerceptron mlp = new MultilayerPerceptron();
		NaiveBayes nb = new NaiveBayes();
		try {
//			mlp.buildClassifier(data);
			nb.buildClassifier(data);
//			Evaluation eval = new Evaluation(data);
//			eval.crossValidateModel(mlp, data, 10, new Random(1));
//			System.out.println(eval.pctCorrect());
		} catch (Exception e) {
			e.printStackTrace();
		}

		LightSensor rightSensor = new LightSensor(SensorPort.S1);
		UltrasonicSensor xSensor = new UltrasonicSensor(SensorPort.S4);
		TouchSensor rightTouch = new TouchSensor(SensorPort.S3);
		TouchSensor leftTouch = new TouchSensor(SensorPort.S2);
		MotorPort rightMotor = MotorPort.A;
		MotorPort leftMotor = MotorPort.C;
		while(true)
		{
			Instance i = new SparseInstance(3);
			i.setDataset(data);
			i.setValue(0, rightSensor.getLightValue());
			i.setValue(1, xSensor.getDistance());
			i.setValue(2, rightTouch.isPressed() ? "TRUE" : "FALSE");
			i.setValue(3, leftTouch.isPressed() ? "TRUE" : "FALSE");
			try {
//				double instanceClass = mlp.classifyInstance(i);
				double instanceClass = nb.classifyInstance(i);
				if (instanceClass == 2.0) {// FORWARD
					leftMotor.controlMotor(25, MotorPort.FORWARD);
					rightMotor.controlMotor(20, MotorPort.FORWARD);
				}
				else if (instanceClass == 1.0) {//BACKRIGHT
					leftMotor.controlMotor(30, MotorPort.BACKWARD);
					rightMotor.controlMotor(70, MotorPort.BACKWARD);
				} else if(instanceClass == 0.0) {//BACKLEFT
					leftMotor.controlMotor(70, MotorPort.BACKWARD);
					rightMotor.controlMotor(30, MotorPort.BACKWARD);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
