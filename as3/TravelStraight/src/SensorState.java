import environment.AbstractState;
import environment.IEnvironment;
import environment.IState;


public class SensorState extends AbstractState
{
	public int frontDistance;// cm
	public int backDistance;// cm
	public int lightValue;
	
	private IEnvironment ct;
	
	public SensorState(IEnvironment ct, int frontDistance, int backDistance, int lightValue) {
		super(ct);
		this.ct = ct;
		this.frontDistance = frontDistance;
		this.backDistance = backDistance;
		this.lightValue = lightValue;
	}

	@Override
	public IState copy() {
		return new SensorState(this.ct, frontDistance, backDistance, lightValue);
	}

	@Override
	public int nnCodingSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double[] nnCoding() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int hashCode()
	{
		return bucketDists(this.frontDistance, this.backDistance);
	}
	
	@Override
	public boolean equals(Object other)
	{
		SensorState ss = (SensorState)other;
		
		int dist1 = bucketDists(ss.frontDistance, ss.backDistance);
		int dist2 = bucketDists(this.frontDistance, this.backDistance);
		
		return dist1 == dist2;
	}
	
	public static int bucketDists(int dist1, int dist2) {
		int distance_from_wall = (dist1 + dist2)/2;
		if (distance_from_wall % 10 < 5) {
			return distance_from_wall - (distance_from_wall % 10);
		} else {
			return distance_from_wall - (distance_from_wall % 10) + 10;
		}
	}
	
//	@Override
//	public String toString()
//	{
//		if(this.frontDistance - this.backDistance < 0)
//			return "too far left";
//		else if(this.frontDistance - this.backDistance > 0)
//			return "too far right";
//		else
//			return "dead on";
//	}
}