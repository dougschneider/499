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
		return 0;
	}

	@Override
	public double[] nnCoding() {
		return null;
	}
	
	@Override
	public int hashCode()
	{
		// this is used to make actions equivalent in hashtables
		return bucketDists(this.frontDistance, this.backDistance);
	}
	
	@Override
	public boolean equals(Object other)
	{
		SensorState ss = (SensorState)other;
		
		int dist1 = bucketDists(ss.frontDistance, ss.backDistance);
		int dist2 = bucketDists(this.frontDistance, this.backDistance);
		
		// two states in the same distance bucket are considered equal
		return dist1 == dist2;
	}
	
	public static int bucketDists(int dist1, int dist2) {
		// a new bucket begins every 5 cm
		float distance_from_wall = (dist1 + dist2)/2;
		return (int)(Math.round((distance_from_wall/10) * 2) / 2.0 * 10);
	}
}