import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import weka.classifiers.functions.SMO;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.CSVLoader;


public class Main {
	
	private static SMO smo;
    private static Instances data;
    private static LightSensor lightSensor;
    private static UltrasonicSensor distanceSensor;

	public static void main(String[] args) {
		
		buildClassifier();
		lightSensor = new LightSensor(SensorPort.S1);
		lightSensor.setFloodlight(true);
		
		distanceSensor = new UltrasonicSensor(SensorPort.S3);
		
		DifferentialPilot pilot = new DifferentialPilot(56, 120, Motor.C, Motor.A);
		pilot.setTravelSpeed(120);
	
		FileWriter out = null;
		try {
			out = new FileWriter(new File("../p1data.arff"));
			
			out.write("@RELATION distance\n\n");
			out.write("@ATTRIBUTE distance NUMERIC\n\n");
			out.write("@DATA\n");
			
			boolean done = false;
			while(true)
			{
				pilot.forward();
				while(!shouldStop())
				{
					out.write(new Integer(distanceSensor.getDistance()).toString());
					out.write("\n");

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				pilot.stop();
				
				while(true)
				{
					if(Button.RIGHT.isDown())
						break;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(Button.LEFT.isDown())
					{
						done = true;
						break;
					}
				}
				if(done)
					break;
			}

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


    private static void buildClassifier()
    {
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
	
	private static boolean shouldStop()
	{
        // set up the light sensor on the NXT
        Instance i = new SparseInstance(2);
        i.setValue(0, lightSensor.getLightValue());
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

}
