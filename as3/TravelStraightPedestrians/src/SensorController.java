import java.io.File;

import weka.clusterers.Clusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.GyroDirectionFinder;
import lejos.nxt.addon.GyroSensor;
import lejos.nxt.addon.OpticalDistanceSensor;
import lejos.robotics.navigation.CompassPilot;
import lejos.robotics.navigation.DifferentialPilot;


public class SensorController {
	
	private static DifferentialPilot pilot = null;
	
	// the speeds our robot can go (in mm/second)
	private static final double VERY_FAST = 190;
	private static final double FAST = 160;
	private static final double SLOW = 130;
	private static final double VERY_SLOW = 100;

	private static Clusterer clusterer = null;
    private static Instances data;
    private static UltrasonicSensor distanceSensor;

	public static int getFrontDist()
	{
		int distance = new OpticalDistanceSensor(SensorPort.S3).getDistance();
		//System.out.println("Front: " + distance/10);
		return distance/10;
	}

	public static int getBackDist()
	{
		//int distance = new OpticalDistanceSensor(SensorPort.S2).getDistance() + 50;
		int distance = new OpticalDistanceSensor(SensorPort.S2).getDistance();
		//System.out.println("Back: " + distance/10);
		return distance/10;
	}
	
	public static int getPedestrianDistance()
	{
		return new UltrasonicSensor(SensorPort.S4).getDistance();
	}
	
	public static int getLightValue()
	{
		return new LightSensor(SensorPort.S1).getLightValue();
	}
	
	public static void controlRightMotor(int power, int mode)
	{
		MotorPort.A.controlMotor(power, mode);
	}
	
	public static void controlLeftMotor(int power, int mode)
	{
		MotorPort.C.controlMotor(power, mode);
	}
	
	public static void rotateLeft()
	{
		// adjust this for battery life
		getPilot().rotate(44);
	}
	
	public static void rotateRight()
	{
    	getPilot().rotate(-45);
	}
	
	public static void goStraight()
	{
		double speed = cluster(getPedestrianDistance());
		getPilot().setTravelSpeed(speed);
    	getPilot().travel(40, false);
	}
	
	public static DifferentialPilot getPilot()
	{
		
		if(pilot == null)
		{
//			pilot = new CompassPilot(new GyroDirectionFinder(new GyroSensor(SensorPort.S4)), 56, 120, Motor.C, Motor.A);
			pilot = new DifferentialPilot(56, 120, Motor.C, Motor.A);
			pilot.setRotateSpeed(60);
			pilot.setTravelSpeed(120);
		}
		
		return pilot;
	}
	
	/**
	 * Get the cluster that the given distance belongs to.
	 */
	private static double cluster(double distance)
	{
		if(clusterer == null)
			buildClusterer();
		
		Instance i = data.firstInstance();
		i.setValue(data.attribute("distance"), distance);
        try {
        	int cluster = clusterer.clusterInstance(i);
        	System.out.println(cluster);
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
		clusterer = new SimpleKMeans();
//		clusterer = new EM();
		// k-means
		String[] options = {"-N 4", "-A \"weka.core.EuclideanDistance -R first-last\"", "-I 500", "-S 10"};
		// EM
//		String[] options = {"-I 100", "-N 4", "-M 1.0E-6", "-S 100"};
		try {
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
