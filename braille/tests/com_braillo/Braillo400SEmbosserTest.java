package com_braillo;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.daisy.braille.embosser.UnsupportedWidthException;
import org.junit.Test;
import org.xml.sax.SAXException;

import com_braillo.BrailloEmbosserProvider.EmbosserType;

public class Braillo400SEmbosserTest extends AbstractTestBraillo200Embosser {
	
	public Braillo400SEmbosserTest() {
		super(new Braillo200Embosser("Braillo 400S", "Firmware 000.17 or later. Embosser table must match hardware setup.", EmbosserType.BRAILLO_400_S));
	}
	
	@Test
	public void testEmbossing() throws IOException, ParserConfigurationException, SAXException, UnsupportedWidthException {
		performTest("resource-files/test-input-1.pef", "resource-files/test-input-1_braillo400S_us_a4.prn");
	}
}