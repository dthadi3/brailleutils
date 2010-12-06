package org.daisy.braille.pef;

import org.junit.Test;

//TODO: more tests
public class RangeTest {
	
	@Test (expected=IllegalArgumentException.class)
	public void testZeroFrom() {
		new Range(0);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testNegativeFrom() {
		new Range(-1);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testNegativeTo() {
		new Range(-3,-1);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testToSmallerThanFrom() {
		new Range(2, 1);
	}
	
	

}
