package models;

import static org.junit.Assert.*;
import org.junit.Test;
import models.Term;

public class TermTest {
	
	@Test
	public void testTerm() {
		Term term = new Term(99l, "Lost", 1);
		
		assertEquals(99l, term.getWeight(), 0.0);
		assertEquals("lost", term.getTerm());
	}
	
	// Negative weight
	@Test (expected = IllegalArgumentException.class) 
	public void testNegativeWeight() throws Exception {
		Term term = new Term(-99l, "Paradise", 1);
	}
	
	// Null weight
	@Test (expected = NullPointerException.class)
	public void testNullWeight() {
		Term term = new Term(null, "Thrill", 1);
	}
	
	// Null string
	@Test (expected = NullPointerException.class)
	public void testNullString() {
		Term term = new Term(99l, null, 1);
	}
	
}
