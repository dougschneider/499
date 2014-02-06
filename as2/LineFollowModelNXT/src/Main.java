import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import lejos.nxt.Button;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;


public class Main {

	public static void main(String[] args) {
//		Instances data = null;
//		FastVector attrInfo = new FastVector();
//		attrInfo.addElement(new Attribute("intensity"));
//		attrInfo.addElement(new Attribute("class"));
//		data = new Instances("test", attrInfo, 1000);
		DataSource source;
		Instances data = null;
		try {
			source = new DataSource("log.csv");
			data = source.getDataSet();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		data.setClassIndex(data.numAttributes()-1);
		
		SMO smo = new SMO();
		String[] options = {"-C 1.0", "-L 0.001", "-P 1.0E-12", "-N 0", "-V -1", "-W 1", "-K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\""};
		try {
			smo.setOptions(options);
//			smo.buildClassifier(data);
			Evaluation eval = new Evaluation(data);
			eval.crossValidateModel(smo, data, 10, new Random(1));
			System.out.println(eval.pctCorrect());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Button.waitForAnyPress();
	}

}
