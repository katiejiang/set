import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.Test;

public class TableTest {

	@Test
	public void testNewDeck() {
		LinkedList<Card> deckList = Table.newDeck();
		assertEquals(deckList.size(), 81);

		Card[] deck = new Card[Table.NUM_CARDS];
		for (int i = 0; i < Table.NUM_CARDS; i++) {
			deck[i] = deckList.pop();
		}

		// now test all cards are unique
		for (int i = 0; i < Table.NUM_CARDS; i++) {
			for (int j = i + 1; j < Table.NUM_CARDS; j++) {
				assertNotEquals(deck[i], deck[j]);
			}
		}
	}

	@Test
	public void testIsSetNull() {
		Card c1 = new Card(Hue.RED, Fill.OUTLINED, Shape.DIAMOND, 1);
		Card c2 = new Card(Hue.GREEN, Fill.OUTLINED, Shape.DIAMOND, 1);

		assertFalse(Table.isSet(c1, null, c2));
	}

	@Test
	public void testIsSetTrue() {

		// 1 property different --> set
		Card c1 = new Card(Hue.RED, Fill.OUTLINED, Shape.DIAMOND, 1);
		Card c2 = new Card(Hue.GREEN, Fill.OUTLINED, Shape.DIAMOND, 1);
		Card c3 = new Card(Hue.PURPLE, Fill.OUTLINED, Shape.DIAMOND, 1);
		assertTrue(Table.isSet(c1, c2, c3));

		// 2 properties different --> set
		c2 = new Card(Hue.GREEN, Fill.SHADED, Shape.DIAMOND, 1);
		c3 = new Card(Hue.PURPLE, Fill.STRIPED, Shape.DIAMOND, 1);
		assertTrue(Table.isSet(c1, c2, c3));

		// 3 properties different --> set
		c2 = new Card(Hue.GREEN, Fill.SHADED, Shape.OVAL, 1);
		c3 = new Card(Hue.PURPLE, Fill.STRIPED, Shape.SQUIGGLE, 1);
		assertTrue(Table.isSet(c1, c2, c3));

		// 4 properties different --> set
		c2 = new Card(Hue.GREEN, Fill.SHADED, Shape.OVAL, 2);
		c3 = new Card(Hue.PURPLE, Fill.STRIPED, Shape.SQUIGGLE, 3);
		assertTrue(Table.isSet(c1, c2, c3));
	}

	@Test
	public void testIsSetFalse() {

		// two of same color
		Card c1 = new Card(Hue.RED, Fill.OUTLINED, Shape.DIAMOND, 1);
		Card c2 = new Card(Hue.RED, Fill.OUTLINED, Shape.DIAMOND, 1);
		Card c3 = new Card(Hue.PURPLE, Fill.OUTLINED, Shape.DIAMOND, 1);
		assertFalse(Table.isSet(c1, c2, c3));

		// two of same fill
		c2 = new Card(Hue.RED, Fill.OUTLINED, Shape.DIAMOND, 1);
		c3 = new Card(Hue.RED, Fill.STRIPED, Shape.DIAMOND, 1);
		assertFalse(Table.isSet(c1, c2, c3));

		// two of same fill
		c2 = new Card(Hue.RED, Fill.OUTLINED, Shape.DIAMOND, 1);
		c3 = new Card(Hue.RED, Fill.OUTLINED, Shape.OVAL, 1);
		assertFalse(Table.isSet(c1, c2, c3));

		// two of same fill
		c2 = new Card(Hue.RED, Fill.OUTLINED, Shape.DIAMOND, 1);
		c3 = new Card(Hue.RED, Fill.OUTLINED, Shape.DIAMOND, 3);
		assertFalse(Table.isSet(c1, c2, c3));
	}
	
	@Test
	public void testHasSetEmpty() {
		try {
			Table.hasSet(null);
			fail("Did not throw exception");
		} catch (IllegalArgumentException e) {
		}
	}
	
	@Test
	public void testHasSetLessThanThreeElements() {
		Card c1 = new Card(Hue.RED, Fill.OUTLINED, Shape.DIAMOND, 1);
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.add(c1);
		cards.add(c1);
		assertFalse(Table.hasSet(cards));
	}
	
	@Test
	public void testHasSet() {
		
		ArrayList<Card> cards = new ArrayList<Card>();
		Card c1 = new Card(Hue.RED, Fill.OUTLINED, Shape.DIAMOND, 1);
		Card c2 = new Card(Hue.GREEN, Fill.OUTLINED, Shape.DIAMOND, 1);
		Card c3 = new Card(Hue.GREEN, Fill.OUTLINED, Shape.DIAMOND, 1);
		cards.add(c1);
		cards.add(c2);
		cards.add(c3);
		
		assertFalse(Table.hasSet(cards));
		
		Card c4 = new Card(Hue.PURPLE, Fill.OUTLINED, Shape.OVAL, 1);
		cards.add(c4);
		
		assertFalse(Table.hasSet(cards));
		
		Card c5 = new Card(Hue.PURPLE, Fill.OUTLINED, Shape.DIAMOND, 1);
		cards.add(c5);
		
		assertTrue(Table.hasSet(cards));
	}

}
