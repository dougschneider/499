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
	}
	
	public void race()
	{
		int c = 0;
		while(c < 10)
		{
			++c;
			Pose currentPose = this.poseProvider.getPose();
			double heading = (currentPose.getHeading() + 360) % 360;
			System.out.println("current: " + heading);
			System.out.println("next: " + this.path.getNextAngle());

			double rotation = 0;
			if(heading - this.path.getNextAngle() > 0)
			{
				rotation = -(heading-this.path.getNextAngle());
				if(this.path.getNextAngle()+360 - heading < rotation)
					rotation = this.path.getNextAngle()+360 - heading;
			}
			else
			{
				rotation = this.path.getNextAngle()-heading;
				if(heading+360 - this.path.getNextAngle() < rotation)
					rotation = -(heading+360 - this.path.getNextAngle());
			}
			this.pilot.rotate(rotation);
			this.pilot.travel(this.path.getNextDistance());
			this.path.goToNext();
		}
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
