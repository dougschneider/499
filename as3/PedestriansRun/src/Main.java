import java.io.File;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import weka.clusterers.Clusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;


public class Main {
	
	// the speeds our robot can go (in mm/second)
	private static final double VERY_FAST = 250;
	private static final double FAST = 200;
	private static final double SLOW = 150;
	private static final double VERY_SLOW = 100;

	private static Clusterer clusterer;
    private static Instances data;
    private static UltrasonicSensor distanceSensor;

	public static void main(String[] args) {
		buildClusterer();
		distanceSensor = new UltrasonicSensor(SensorPort.S3);

		// create a pilot to move the robot
		DifferentialPilot pilot = new DifferentialPilot(56, 120, Motor.C, Motor.A);
		
		// travel until the program is killed
		while(true)
		{
			double speed = cluster(distanceSensor.getDistance());
			pilot.setTravelSpeed(speed);
			pilot.forward();
		}
	}
	
	/**
	 * Get the cluster that the given distance belongs to.
	 */
	private static double cluster(double distance)
	{
		Instance i = data.firstInstance();
		i.setValue(data.attribute("distance"), distance);
        try {
        	int cluster = clusterer.clusterInstance(i);
        	System.out.println(cluster);
        	// uncomment the lines corresponding to k-means or EM
        	// k-means
        	if(cluster == 0)
        		return VERY_SLOW;
        	else if(cluster == 1)
        		return SLOW;
        	else if(cluster == 2)
        		return FAST;
        	else if(cluster == 3)
        		return VERY_FAST;
        	// EM
//        	if(cluster == 0)
//        		return SLOW;
//        	else if(cluster == 1)
//        		return VERY_FAST;
//        	else if(cluster == 2)
//        		return FAST;
//        	else if(cluster == 3)
//        		return VERY_SLOW;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		System.out.println("oh oh... not a cluster");
		System.exit(-1);
		return -1;
	}
	
	private static void buildClusterer()
	{
		data = null;
        // load in the training data
		try {
			ArffLoader loader = new ArffLoader();
			loader.setSource(new File("../p1data.arff"));
			data = loader.getDataSet();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
        // create the model
    	// uncomment the lines corresponding to k-means or EM
		clusterer = new SimpleKMeans();
//		clusterer = new EM();
    	// uncomment the lines corresponding to k-means or EM
		// k-means
		String[] options = {"-N 4", "-A \"weka.core.EuclideanDistance -R first-last\"", "-I 500", "-S 10"};
		// EM
//		String[] options = {"-I 100", "-N 4", "-M 1.0E-6", "-S 100"};
		try {
        	// uncomment the lines corresponding to k-means or EM
			// k-means
			((SimpleKMeans)clusterer).setOptions(options);
			((SimpleKMeans)clusterer).setNumClusters(4);
			// EM
//			((EM)clusterer).setOptions(options);
//			((EM)clusterer).setNumClusters(4);
            // train the model
			clusterer.buildClusterer(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
