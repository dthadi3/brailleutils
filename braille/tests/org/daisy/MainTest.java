package org.daisy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.daisy.braille.embosser.Embosser;
import org.daisy.braille.embosser.EmbosserCatalog;
import org.daisy.braille.table.TableCatalog;
import org.daisy.paper.PaperCatalog;
import org.junit.Test;

public class MainTest {
	
	@Test
	public void countTables() {
		TableCatalog tc = TableCatalog.newInstance();
		assertEquals("Verify the number of table implementations", 13, tc.list().size());
	}
	
	@Test
	public void countEmbossers() {
		EmbosserCatalog ec = EmbosserCatalog.newInstance();
		assertEquals("Verify the number of embosser implementations", 11, ec.list().size());
	}
	
	@Test
	public void countPapers() {
		PaperCatalog pc = PaperCatalog.newInstance();
		assertEquals("Verify the number of paper implementations", 7, pc.list().size());
	}
	
	@Test
	public void supportsTables() {
		TableCatalog tc = TableCatalog.newInstance();
		EmbosserCatalog ec = EmbosserCatalog.newInstance();
		for (Embosser e : ec.list()) {
			assertTrue("Assert that embosser supports a least one table: " + e.getDisplayName(), tc.list(e.getTableFilter()).size()>0);
		}
	}

}
