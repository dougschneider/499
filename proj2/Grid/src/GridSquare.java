public class GridSquare {

	// coordinates of bottom right corner on Cartesian plain
	public final int x;
	public final int y;
	
	// edge length in millimeters
	private final int size;
	
	private boolean isBlocked = false;

	/**
	 * A square within a grid.
	 * 
	 * @param x: bottom right corner x coordinate
	 * @param y: bottom right corner x coordinate
	 * @param size: edge length in millimeters
	 */
	public GridSquare(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
	}

	public boolean isBlocked() {
		return this.isBlocked;
	}

	public void block() {
		this.isBlocked = true;
	}

	public void unblock() {
		this.isBlocked = false;
	}

	/**
	 * Return true if the given x and y coordinates are within the square.
	 * The top and left edges are not within the square.
	 * @param x
	 * @param y
	 */
	public boolean surrounds(int x, int y) {
		if ((this.x <= x && (this.x + this.size) > x)
				&& (this.y <= y && (this.y + this.size) > y)) {
			return true;
		}
		return false;
	}
}
