import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import weka.classifiers.functions.SMO;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;


public class Learner {

	/**
	 * Learn and save a model from the given data
	 * 
	 * @param arffFileName: the arff file with input data
	 * @param modelFileName: the filename to which to save the generated model
	 */
	public static void main(String[] args) {
		DataSource source;
		Instances data = null;
		String arffFileName = args[0];
		String modelFileNameame = args[1];
        // load in the training data
		try {
			source = new DataSource(arffFileName);
			data = source.getDataSet();
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
		
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new FileOutputStream(modelFileNameame));
			out.writeObject(smo);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
