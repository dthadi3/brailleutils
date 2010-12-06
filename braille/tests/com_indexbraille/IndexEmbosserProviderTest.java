package com_indexbraille;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class IndexEmbosserProviderTest {
	private static IndexEmbosserProvider ep = new IndexEmbosserProvider();
	
	@Test
	public void testListLength() {
		assertEquals("Assert that all embossers have tests by counting the list length", 4, ep.list().size());
	}

}
