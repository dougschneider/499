import static org.junit.Assert.*;

import org.junit.Test;


public class GridSquareTests {

	@Test
	public void testConstructor() {
		GridSquare square = new GridSquare(0, 0, 10);
		assertEquals(0, square.x);
		assertEquals(0, square.y);
	}
	
	@Test
	public void testIsBlocked() {
		GridSquare square = new GridSquare(0, 0, 10);
		assertFalse(square.isBlocked());
		square.block();
		assertTrue(square.isBlocked());
		square.unblock();
		assertFalse(square.isBlocked());
	}
	
	@Test
	public void testSurrounds() {
		GridSquare square = new GridSquare(0, 0, 100);
		
		// corners
		// bottom right corner is inside, others are outside
		assertTrue(square.surrounds(0, 0));
		assertFalse(square.surrounds(100, 0));
		assertFalse(square.surrounds(100, 100));
		assertFalse(square.surrounds(0, 100));
		
		// edges
		// bottom and left edge are inside, others are outside
		assertTrue(square.surrounds(74, 0));
		assertFalse(square.surrounds(100, 56));
		assertFalse(square.surrounds(98, 100));
		assertTrue(square.surrounds(0, 42));
		
		// inside
		assertTrue(square.surrounds(74, 56));
		assertTrue(square.surrounds(98, 56));
		assertTrue(square.surrounds(24, 1));
		assertTrue(square.surrounds(12, 22));
		
		// outside
		assertFalse(square.surrounds(123, 124));
		assertFalse(square.surrounds(56, 124));
		assertFalse(square.surrounds(123, 0));
		assertFalse(square.surrounds(0, -124));
		assertFalse(square.surrounds(-123, -124));
		assertFalse(square.surrounds(50, -124));
	}

}
