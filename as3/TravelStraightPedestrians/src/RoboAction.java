import environment.IAction;

public class RoboAction implements IAction{
	
	public static final int STRAIGHT = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	
	public int direction;
	
	public RoboAction(int direction)
	{
		this.direction = direction;
	}
	
	@Override
	public boolean equals(Object other)
	{
		RoboAction ra = (RoboAction)other;
		return this.direction == ra.direction;
	}
	
	@Override
	public int hashCode()
	{
		return this.direction;
	}

	@Override
	public Object copy() {
		return new RoboAction(this.direction);
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
	public String toString()
	{
		switch (this.direction) {
		case STRAIGHT:
			return "straight";
		case RIGHT:
			return "right";
		case LEFT:
			return "left";
		default:
			return "NOT A DIRECTION";
		}
	}

}
