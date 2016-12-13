import static org.junit.Assert.*;

import org.junit.Test;

public class CardTest {

	@Test
	public void testEqualsSelf() {
		Card c1 = new Card(Hue.RED, Fill.SHADED, Shape.DIAMOND, 1);
		assertEquals(c1, c1);
	}
	
	@Test
	public void testEqualsOther() {
		Card c1 = new Card(Hue.RED, Fill.SHADED, Shape.DIAMOND, 1);
		Card c2 = new Card(Hue.RED, Fill.SHADED, Shape.DIAMOND, 1);
		assertEquals(c1, c2);
	}
	
	@Test
	public void testNotEqualsOther() {
		Card c1 = new Card(Hue.RED, Fill.SHADED, Shape.DIAMOND, 1);
		Card c2 = new Card(Hue.GREEN, Fill.SHADED, Shape.DIAMOND, 1);
		Card c3 = new Card(Hue.RED, Fill.STRIPED, Shape.DIAMOND, 1);
		Card c4 = new Card(Hue.RED, Fill.SHADED, Shape.OVAL, 1);
		Card c5 = new Card(Hue.RED, Fill.SHADED, Shape.DIAMOND, 3);
		assertNotEquals(c1, c2);
		assertNotEquals(c1, c3);
		assertNotEquals(c1, c4);
		assertNotEquals(c1, c5);	
	}
	
	@Test
	public void testGetDifferencesNull() {
		Card c1 = new Card(Hue.RED, Fill.SHADED, Shape.DIAMOND, 1);
		try {
			c1.getDifferences(null);
			fail("Did not throw exception");
		} catch (IllegalArgumentException e) {
		}
	}
	
	@Test
	public void testGetDifferences() {
		Card c1 = new Card(Hue.RED, Fill.SHADED, Shape.DIAMOND, 1);
		Card c2 = new Card(Hue.GREEN, Fill.SHADED, Shape.OVAL, 1);
		
		boolean[] d1 = c1.getDifferences(c1);
		boolean[] e1 = new boolean[]{true, true, true, true};
		for (int i = 0; i < d1.length; i++) {
			assertEquals(d1[i], e1[i]);
		}
		
		boolean[] d2 = c1.getDifferences(c2);
		boolean[] e2 = new boolean[]{false, true, false, true};
		for (int i = 0; i < d2.length; i++) {
			assertEquals(d2[i], e2[i]);
		}
	}

}
