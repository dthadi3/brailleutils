/*
 * Braille Utils (C) 2010 Daisy Consortium 
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.daisy.braille.facade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.daisy.braille.embosser.Embosser;
import org.daisy.braille.embosser.EmbosserCatalog;
import org.daisy.braille.embosser.EmbosserFactoryException;
import org.daisy.braille.embosser.EmbosserFeatures;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.UnsupportedWidthException;
import org.daisy.braille.pef.PEFHandler;
import org.daisy.braille.pef.PEFHandler.Alignment;
import org.daisy.braille.pef.Range;
import org.daisy.braille.pef.TextHandler;
import org.daisy.paper.PageFormat;
import org.daisy.paper.PaperCatalog;
import org.xml.sax.SAXException;

/**
 * Provides a facade for PEFHandler
 * @author Joel Håkansson, TPB
 *
 */
public class PEFConverterFacade {
	public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * String based method matching main args
	 * @param args
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws NumberFormatException 
	 * @throws EmbosserFactoryException 
	 */
	public static void parsePefFile(String[] args) throws NumberFormatException, ParserConfigurationException, SAXException, IOException, EmbosserFactoryException, UnsupportedWidthException {
		if (args.length < 2 || args.length % 2 != 0) {
			throw new IllegalArgumentException("Wrong number of arguments");
		} else {
			Range range = null;
			EmbosserCatalog ef = EmbosserCatalog.newInstance();
			Alignment align = Alignment.CENTER_OUTER;
			int offset = 0;
			Embosser emb = null;
			HashMap<String, String> settings = new HashMap<String, String>();
			for (int i=0; i<(args.length-2)/2; i++) {
				settings.put(args[2+i*2], args[3+i*2]);
			}
			emb = ef.get(settings.remove("-embosser"));
			for (String key : settings.keySet()) {
				String value = settings.get(key);
				if ("-table".equals(key)) {
					emb.setFeature(EmbosserFeatures.TABLE, value);
				} else if ("-breaks".equals(key)) {
					emb.setFeature("breaks", value);
				} else if ("-range".equals(key)) {
					range = Range.parseRange(value);
				} else if ("-fallback".equals(key)) {
					emb.setFeature(EmbosserFeatures.UNSUPPORTED_CELL_FALLBACK_METHOD, value);
				} else if ("-replacement".equals(key)) {
					emb.setFeature(EmbosserFeatures.UNSUPPORTED_CELL_REPLACEMENT, value);
				} else if ("-pad".equals(key)) {
					emb.setFeature("padNewline", value);
				} else if ("-alignmentOffset".equals(key)) {
					offset = Integer.parseInt(value);
				} else if ("-align".equals(key)) {
					if (value.equalsIgnoreCase("center")) {
						value = "CENTER_OUTER";
					}
					try {
						align = Alignment.valueOf(value.toUpperCase());
					} catch (IllegalArgumentException e) {
						System.out.println("Unknown value: " + value);
					}
				} else if ("-papersize".equals(key)) {
					PaperCatalog pc = PaperCatalog.newInstance();
					//TODO: support reverse orientation
					emb.setFeature(EmbosserFeatures.PAGE_FORMAT, new PageFormat(pc.get(value)));
				} else if ("-cellWidth".equals(key)) {
					emb.setFeature(EmbosserFeatures.CELL_WIDTH, value);
				} else if ("-cellHeight".equals(key)) {
					emb.setFeature(EmbosserFeatures.CELL_HEIGHT, value);
				} else {
					throw new IllegalArgumentException("Unknown option \"" + key + "\"");
				}
			}
			
			EmbosserWriter embosser = emb.newEmbosserWriter(new FileOutputStream(args[1]));
			PEFHandler.Builder builder = 
				new PEFHandler.Builder(embosser).
					range(range).
					align(align).
					offset(offset);
			PEFHandler ph = builder.build();
			parsePefFile(new File(args[0]), ph);
		}
	}

	/**
	 * Parses the given input using the supplied PEFHandler.
	 * @param input the input PEF file
	 * @param ph the PEFHandler to use
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws UnsupportedWidthException
	 */
	public static void parsePefFile(File input, PEFHandler ph) throws ParserConfigurationException, SAXException, IOException, UnsupportedWidthException {
		if (!input.exists()) {
			throw new IllegalArgumentException("Input does not exist");
		}
		FileInputStream is = new FileInputStream(input);
		parsePefFile(is, ph);
	}
	
	/**
	 * Parses the given input stream using the supplied PEFHandler.
	 * @param is the input stream
	 * @param ph the PEFHandler
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws UnsupportedWidthException
	 */
	public static void parsePefFile(InputStream is, PEFHandler ph) throws ParserConfigurationException, SAXException, IOException, UnsupportedWidthException {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		SAXParser sp = spf.newSAXParser();
		try {
			sp.parse(is, ph);
		} catch (SAXException e) {
			if (ph.hasWidthError()) {
				throw new UnsupportedWidthException(e);
			} else {
				throw e;
			}
		}		
	}


	/**
	 * String based method matching main args
	 * @param args
	 * @throws IOException 
	 */
	public static void parseTextFile(String args[]) throws IOException {
		if (args.length < 2 || args.length % 2 != 0) {
			throw new IllegalArgumentException("Wrong number of arguments");
		} else {
			TextHandler.Builder builder = new TextHandler.Builder(new File(args[0]), new File(args[1]));
			for (int i=0; i<(args.length-2)/2; i++) {
				if ("-title".equals(args[2+i*2])) {
					builder.title(args[3+i*2]);
				} else if ("-author".equals(args[2+i*2])) {
					builder.author(args[3+i*2]);
				} else if ("-identifier".equals(args[2+i*2])) {
					builder.identifier(args[3+i*2]);
				} else if ("-mode".equals(args[2+i*2])) {
					builder.converterId(args[3+i*2]);
				} else if ("-language".equals(args[2+i*2])) {
					builder.language(args[3+i*2]);
				} else if ("-duplex".equals(args[2+i*2])) {
					builder.duplex("true".equals(args[3+i*2].toLowerCase()));
				}else if ("-date".equals(args[2+i*2])) {
					try {
						builder.date(DATE_FORMAT.parse(args[3+i*2]));
					} catch (ParseException e) {
						throw new IllegalArgumentException(e);
					}
				} else {
					throw new IllegalArgumentException("Unknown option \"" + args[2+i*2] + "\"");
				}
			}
			TextHandler tp = builder.build();
			tp.parse();
		}
	}	

}