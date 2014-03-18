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
		// get the light sensor to record the end of training episodes
		lightSensor = new LightSensor(SensorPort.S1);
		lightSensor.setFloodlight(true);
		
		// get the distance sensor to record data points
		distanceSensor = new UltrasonicSensor(SensorPort.S3);
		
		// use a pilot to travel forward
		DifferentialPilot pilot = new DifferentialPilot(56, 120, Motor.C, Motor.A);
		pilot.setTravelSpeed(120);
	
		FileWriter out = null;
		try {
			out = new FileWriter(new File("../p1data.arff"));
			
			// add the header for the arff file
			out.write("@RELATION distance\n\n");
			out.write("@ATTRIBUTE distance NUMERIC\n\n");
			out.write("@DATA\n");
			
			// run a set of training episodes until termination
			boolean done = false;
			while(true)
			{
				// move forward at a constant speed
				pilot.forward();
				
				// collect sensor data until we reach the tape
				while(!shouldStop())
				{
					// store another data point
					out.write(new Integer(distanceSensor.getDistance()).toString());
					out.write("\n");

					// wait a little before recording another data point
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// stop at the end of the training episode
				pilot.stop();
				
				// wait at the end of an episode for further instructions
				while(true)
				{
					// if right button is down, new episode
					if(Button.RIGHT.isDown())
						break;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					// if left button is down, we're done training
					if(Button.LEFT.isDown())
					{
						done = true;
						break;
					}
				}
				
				// if we're done, break out
				if(done)
					break;
			}

			// flush the written data
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Build the classifier for use in detecting episode termination
	 */
    private static void buildClassifier()
    {
		data = null;
        // load in the training data
		try {
			CSVLoader loader = new CSVLoader();
			loader.setSource(new File("../p1data.csv"));
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
	
    /**
     * Return true if the episode of training should end (if we see tape)
     */
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
