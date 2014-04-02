import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class GridTests {
	
	/**
	 * Make a new 30mm*30mm grid with 10mm*10mm squares
	 */
	private Grid makeGrid() {
		try {
			return new Grid(30, 10);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Test
	public void testConstructor() {
		@SuppressWarnings("unused")
		Grid grid = null;
		
		// valid arguments should be fine
		try {
			grid = new Grid(30, 10);
			grid = new Grid(100, 10);
			grid = new Grid(30, 30);
			grid = new Grid(30, 5);
			grid = new Grid(30, 1);
			grid = new Grid(30, 3);
		} catch (Exception e) {
			fail("Should not throw exception.");
		}
		
		// invalid arguments should throw exception		
		try {
			grid = new Grid(30, 8);
			fail("Should throw exception.");
		} catch (Exception e) {
			// pass
		}
		try {
			grid = new Grid(30, 45);
			fail("Should throw exception.");
		} catch (Exception e) {
			// pass
		}
		try {
			grid = new Grid(30, 60);
			fail("Should throw exception.");
		} catch (Exception e) {
			// pass
		}
	}
	
	@Test
	public void testGridEdges() {
		Grid grid = makeGrid();
		ArrayList<Tuple<Integer, Integer>> goodPoints= new ArrayList<Tuple<Integer, Integer>>();
		ArrayList<Tuple<Integer, Integer>> badPoints= new ArrayList<Tuple<Integer, Integer>>();
		
		// these points are in the grid
		goodPoints.add(new Tuple<Integer, Integer>(20,22));
		goodPoints.add(new Tuple<Integer, Integer>(10,29));
		goodPoints.add(new Tuple<Integer, Integer>(12,0));
		goodPoints.add(new Tuple<Integer, Integer>(1,11));
		goodPoints.add(new Tuple<Integer, Integer>(10,16));
		goodPoints.add(new Tuple<Integer, Integer>(20,8));
		goodPoints.add(new Tuple<Integer, Integer>(5,19));
		goodPoints.add(new Tuple<Integer, Integer>(10,20));
		goodPoints.add(new Tuple<Integer, Integer>(20,20));
		
		// assert that each point can be accessed
		for(int i = 0; i < goodPoints.size(); i++) {
			int x = goodPoints.get(i).x;
			int y = goodPoints.get(i).y;
			try {
				assertFalse(grid.isBlocked(x, y));
			} catch (Exception e) {
				fail("Should not throw exception.");
			}
		}
		
		// these points are not in the grid
		badPoints.add(new Tuple<Integer, Integer>(-1,22));
		badPoints.add(new Tuple<Integer, Integer>(10,129));
		badPoints.add(new Tuple<Integer, Integer>(-12,0));
		badPoints.add(new Tuple<Integer, Integer>(1,31));
		badPoints.add(new Tuple<Integer, Integer>(10,30));
		badPoints.add(new Tuple<Integer, Integer>(30,8));
		badPoints.add(new Tuple<Integer, Integer>(-5,19));
		badPoints.add(new Tuple<Integer, Integer>(10,30));
		badPoints.add(new Tuple<Integer, Integer>(30,20));
		
		// assert that each point throws an exception
		for(int i = 0; i < badPoints.size(); i++) {
			int x = badPoints.get(i).x;
			int y = badPoints.get(i).y;
			try {
				assertFalse(grid.isBlocked(x, y));
				fail(x + " " + y + " should throw exception.");
			} catch (Exception e) {
				// pass
			}
		}
		
	}
	
	@Test
	public void testBlockUnblock() throws Exception {
		Grid grid = makeGrid();
		ArrayList<Tuple<Integer, Integer>> points= new ArrayList<Tuple<Integer, Integer>>();
		
		// add some arbitrary points (that are valid)
		points.add(new Tuple<Integer, Integer>(20,22));
		points.add(new Tuple<Integer, Integer>(10,29));
		points.add(new Tuple<Integer, Integer>(12,0));
		points.add(new Tuple<Integer, Integer>(1,11));
		points.add(new Tuple<Integer, Integer>(10,16));
		points.add(new Tuple<Integer, Integer>(20,8));
		points.add(new Tuple<Integer, Integer>(5,19));
		points.add(new Tuple<Integer, Integer>(10,20));
		points.add(new Tuple<Integer, Integer>(20,20));
		
		// assert that each point is blockable
		for(int i = 0; i < points.size(); i++) {
			int x = points.get(i).x;
			int y = points.get(i).y;
			grid.unblock(x, y);
			assertFalse(grid.isBlocked(x, y));
			grid.block(x, y);
			assertTrue(grid.isBlocked(x, y));
		}
	}
	
	@Test
	public void testSquareCorners() throws Exception {
		Grid grid = makeGrid();
		ArrayList<Tuple<Integer, Integer>> points= new ArrayList<Tuple<Integer, Integer>>();
		
		// each corner should be in a different square
		points.add(new Tuple<Integer, Integer>(0,0));
		points.add(new Tuple<Integer, Integer>(10,0));
		points.add(new Tuple<Integer, Integer>(20,0));
		points.add(new Tuple<Integer, Integer>(0,10));
		points.add(new Tuple<Integer, Integer>(10,10));
		points.add(new Tuple<Integer, Integer>(20,10));
		points.add(new Tuple<Integer, Integer>(0,20));
		points.add(new Tuple<Integer, Integer>(10,20));
		points.add(new Tuple<Integer, Integer>(20,20));
		
		// assert that each point is in a different square
		// by marking each square as blocked
		for(int i = 0; i < points.size(); i++) {
			int x = points.get(i).x;
			int y = points.get(i).y;
			assertFalse(grid.isBlocked(x, y));
			grid.block(x,y);
		}
	}

}
