import lejos.nxt.LightSensor;
import lejos.nxt.remote.RemoteMotor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;


public class Racer
{
	private static double wheelDiameter = 56;
	private static double trackWidth = 120;
	
	private LightSensor lightSensor;
	private RemoteMotor rightMotor;
	private RemoteMotor leftMotor;
	private DifferentialPilot pilot;
	private PoseProvider poseProvider;
	private Path path;
	
	public Racer(LightSensor lightSensor, RemoteMotor rightMotor, RemoteMotor leftMotor)
	{
		this.lightSensor = lightSensor;
		this.lightSensor.setFloodlight(true);
		
		this.rightMotor = rightMotor;
		this.leftMotor = leftMotor;
		
		this.pilot = new DifferentialPilot(Racer.wheelDiameter, Racer.trackWidth, leftMotor, rightMotor);
		this.pilot.setRotateSpeed(100);
		this.pilot.setTravelSpeed(100);
		this.poseProvider = new OdometryPoseProvider(this.pilot);
		this.poseProvider.setPose(new Pose(748, 150, 180));
		
		this.path = new Path();
		System.out.println(path.getNextAngle());
		System.out.println(path.getNextDistance());
		path.goToNext();
		System.out.println(path.getNextAngle());
		System.out.println(path.getNextDistance());
		System.exit(0);
	}
	
	public void race()
	{
		int c = 0;
		Pose finalPose = this.poseProvider.getPose();
		System.out.println(finalPose.getX());
		System.out.println(finalPose.getY());
		System.out.println(finalPose.getHeading());
		this.pilot.arc(-300, 90);
		finalPose = this.poseProvider.getPose();
		System.out.println(finalPose.getX());
		System.out.println(finalPose.getY());
		System.out.println(finalPose.getHeading());
		return;
//		while(c < 200)
//		{
//			c++;
//			if(this.lightSensor.getLightValue() > 30)
//			{
//				this.arcLeft();
//			}
//			else
//			{
//				this.arcRight();
//			}
//			Delay.msDelay(20);
//		}
//		this.stop();
//		Pose finalPose = this.poseProvider.getPose();
//		System.out.println(finalPose.getX());
//		System.out.println(finalPose.getY());
//		System.out.println(finalPose.getHeading());
	}
	
	public void stop()
	{
		this.pilot.stop();
	}
	
	private void arcLeft()
	{
		this.pilot.rotateLeft();
	}
	
	private void arcRight()
	{
		this.pilot.rotateRight();
	}
}
