package com_braillo;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.daisy.braille.embosser.UnsupportedWidthException;
import org.daisy.paper.PageFormat;
import org.junit.Test;
import org.xml.sax.SAXException;

import com_braillo.BrailloEmbosserProvider.EmbosserType;

public class Braillo200EmbosserTest extends AbstractTestBraillo200Embosser {
	
	public Braillo200EmbosserTest() {
		super(new Braillo200Embosser("Braillo 200", "Firmware 000.17 or later. Embosser table must match hardware setup.", EmbosserType.BRAILLO_200, PageFormat.Type.TRACTOR));
	}
	
	@Test
	public void testEmbossing() throws IOException, ParserConfigurationException, SAXException, UnsupportedWidthException {
		performTest("resource-files/test-input-1.pef", "resource-files/test-input-1_braillo200_us_a4.prn");
	}
}