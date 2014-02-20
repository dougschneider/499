import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;

import weka.classifiers.functions.SMO;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Main {

	public static void main(String[] args) {
		SMO smo = null;
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream("../p1model.ser"));
			smo = (SMO) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Instances predictable = null;
		try {
			DataSource source = new DataSource("../p1predict.arff");
			predictable = source.getDataSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		predictable.setClassIndex(predictable.numAttributes()-1);
		
		Enumeration en = predictable.enumerateInstances();
		while (en.hasMoreElements()) {
			Instance i = (Instance) en.nextElement();
			try {
				i.setClassValue(smo.classifyInstance(i));
				System.out.println(i.stringValue(i.classAttribute()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
