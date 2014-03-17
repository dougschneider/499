import static org.junit.Assert.*;

import org.junit.Test;


public class SensorStateTest {

	@Test
	public void test() {
		assertEquals(55, SensorState.bucketDists(52, 54));
		assertEquals(30, SensorState.bucketDists(32, 30));
		assertEquals(70, SensorState.bucketDists(75, 68));
		assertEquals(55, SensorState.bucketDists(58, 57));
		assertEquals(60, SensorState.bucketDists(58, 58));
		assertEquals(55, SensorState.bucketDists(57, 57));
		assertEquals(55, SensorState.bucketDists(56, 57));
	}

}
