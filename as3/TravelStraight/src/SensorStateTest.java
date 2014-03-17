import static org.junit.Assert.*;

import org.junit.Test;


public class SensorStateTest {

	@Test
	public void test() {
		assertEquals(50, SensorState.bucketDists(52, 54));
		assertEquals(30, SensorState.bucketDists(32, 30));
		assertEquals(70, SensorState.bucketDists(75, 68));
		assertEquals(60, SensorState.bucketDists(58, 57));
	}

}
