
public class Grid {
	// max dimension in millimeters
	private int worldSize = 0;
	
	// size of each grid square in millimeters
	private int squareSize = 0;
	
	// number of squares in a single dimension
	private int numSquares = 0;
	
	// the grid
	private GridSquare[][] grid = null; 
	
	public Grid(int worldSize, int squareSize) throws Exception {
		if (worldSize % squareSize != 0) {
			throw new Exception("squareSize must be multiple of worldSize");
		}
		
		this.worldSize = worldSize;
		this.squareSize = squareSize;
		this.numSquares = this.worldSize / this.squareSize;
		
		this.grid = initGrid();
	}
	
	private GridSquare[][] initGrid() {
		GridSquare[][] newGrid = new GridSquare[this.numSquares][this.numSquares];
		
		for (int x = 0; x < numSquares; x++) {
			for (int y = 0; y < numSquares; y ++) {
				newGrid[x][y] = new GridSquare(x * this.squareSize, y * this.squareSize, squareSize);
			}
		}
		
		return newGrid;
	}
	
	private GridSquare getAffectedSquare (int x, int y) throws Exception {
		GridSquare currSquare = null;
		
		for (int i = 0; i < this.numSquares; i++) {
			for (int k = 0; k < this.numSquares; k++) {
				currSquare = this.grid[i][k];
				if (currSquare.surrounds(x, y)) {
					return currSquare;
				}
			}
		}
		
		throw new Exception("Coordinates to not match a square in the grid.");
	}
	
	public void block(int x, int y) throws Exception {
		GridSquare square = getAffectedSquare(x, y);
		square.block();
	}
	
	public void unblock(int x, int y) throws Exception {
		GridSquare square = getAffectedSquare(x, y);
		square.unblock();
	}
	
	public boolean isBlocked(int x, int y) throws Exception {
		GridSquare square = getAffectedSquare(x, y);
		return square.isBlocked();
	}
}
