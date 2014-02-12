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
		try {
			CSVLoader loader = new CSVLoader();
			loader.setSource(new File("../../log.csv"));
			data = loader.getDataSet();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		data.setClassIndex(data.numAttributes()-1);
		
		SMO smo = new SMO();
		String[] options = {"-C 1.0", "-L 0.001", "-P 1.0E-12", "-N 0", "-V -1", "-W 1", "-K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\""};
		try {
			smo.setOptions(options);
			smo.buildClassifier(data);
//			Evaluation eval = new Evaluation(data);
//			eval.crossValidateModel(smo, data, 10, new Random(1));
//			System.out.println(eval.pctCorrect());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		LightSensor sensor = new LightSensor(SensorPort.S1);
		MotorPort rightMotor = MotorPort.A;
		MotorPort leftMotor = MotorPort.C;
		int current = 0;
		int delta = 3;
		while(current < 10000)
		{
			current += delta;
			int val = sensor.getLightValue();
			Instance i = new SparseInstance(2);
			i.setValue(0, val);
			i.setDataset(data);
			try {
				if (smo.classifyInstance(i) == 1.0) {
					leftMotor.controlMotor(100, MotorPort.STOP);
					rightMotor.controlMotor(30, MotorPort.FORWARD);
				} else {
					leftMotor.controlMotor(30, MotorPort.FORWARD);
					rightMotor.controlMotor(100, MotorPort.STOP);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
