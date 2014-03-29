import static org.junit.Assert.*;

import org.junit.Test;


public class DistanceToLineTests {

	@Test
	public void test() {
		double MARGIN = 0.1;
		double dist = 0.0;
		
		// on the line
		dist = Main.distanceToLine(0, 0, -10, -10, 10, 10); 
		assertEquals(0.0, dist, MARGIN);
		dist = Main.distanceToLine(0, 0, 10, 10, -10, -10); 
		assertEquals(0.0, dist, MARGIN);
		
		// line with positive slope on our right
		dist = Main.distanceToLine(0, 0, 5, -5, 10, 5);
		assertEquals(6.7, dist, MARGIN);
		
		// line with positive slope on our left
		dist = Main.distanceToLine(0, 0, 10, 5, 5, -5);
		assertEquals(-6.7, dist, MARGIN);
		
		// line with positive slope on our left
		dist = Main.distanceToLine(10, -5, 5, -5, 10, 5);
		assertEquals(-4.5, dist, MARGIN);
		
		// line with positive slope on our right
		dist = Main.distanceToLine(10, -5, 10, 5, 5, -5);
		assertEquals(4.5, dist, MARGIN);
		
		// line with negative slope on our left
		dist = Main.distanceToLine(10, -5, 0, 5, 10, 0);
		assertEquals(-4.5, dist, MARGIN);
		
		// line with negative slope on our right
		dist = Main.distanceToLine(10, -5, 10, 0, 0, 5);
		assertEquals(4.5, dist, MARGIN);
	}

}
