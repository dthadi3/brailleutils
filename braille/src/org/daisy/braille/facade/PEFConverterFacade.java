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
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

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
	public final static String KEY_EMBOSSER = "embosser";
	public final static String KEY_TABLE = "table";
	public final static String KEY_BREAKS = "breaks";
	public final static String KEY_RANGE = "range";
	public final static String KEY_FALLBACK = "fallback";
	public final static String KEY_REPLACEMENT = "replacement";
	public final static String KEY_PADDING = "pad";
	public final static String KEY_ALIGNMENT_OFFSET = "alignmentOffset";
	public final static String KEY_ALIGN = "align";
	public final static String KEY_PAGE_FORMAT = "papersize";
	public final static String KEY_CELL_WIDTH = "cellWidth";
	public final static String KEY_CELL_HEIGHT = "cellHeight";
	
	public final static String KEY_TITLE = "title";
	public final static String KEY_AUTHOR = "author";
	public final static String KEY_IDENTIFIER = "identifier";
	public final static String KEY_MODE = "mode";
	public final static String KEY_LANGUAGE = "language";
	public final static String KEY_DUPLEX = "duplex";
	public final static String KEY_DATE = "date";

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
			HashMap<String, String> settings = new HashMap<String, String>();
			for (int i=0; i<(args.length-2)/2; i++) {
				settings.put(args[2+i*2].substring(1), args[3+i*2]);
			}
			parsePefFile(new File(args[0]), new FileOutputStream(args[1]), settings);
		}
	}
	
	public static void parsePefFile(File input, OutputStream os, Map<String, String> settings) throws NumberFormatException, ParserConfigurationException, SAXException, IOException, EmbosserFactoryException, UnsupportedWidthException {
		Range range = null;
		EmbosserCatalog ef = EmbosserCatalog.newInstance();
		Alignment align = Alignment.CENTER_OUTER;
		int offset = 0;
		Embosser emb = null;
		emb = ef.get(settings.remove(KEY_EMBOSSER));
		if (emb==null) {
			emb = ef.get("org_daisy.GenericEmbosserProvider.EmbosserType.NONE");
		}
		for (String key : settings.keySet()) {
			String value = settings.get(key);
			if (KEY_TABLE.equals(key)) {
				emb.setFeature(EmbosserFeatures.TABLE, value);
			} else if (KEY_BREAKS.equals(key)) {
				emb.setFeature("breaks", value);
			} else if (KEY_RANGE.equals(key)) {
				range = Range.parseRange(value);
			} else if (KEY_FALLBACK.equals(key)) {
				emb.setFeature(EmbosserFeatures.UNSUPPORTED_CELL_FALLBACK_METHOD, value);
			} else if (KEY_REPLACEMENT.equals(key)) {
				emb.setFeature(EmbosserFeatures.UNSUPPORTED_CELL_REPLACEMENT, value);
			} else if (KEY_PADDING.equals(key)) {
				emb.setFeature("padNewline", value);
			} else if (KEY_ALIGNMENT_OFFSET.equals(key)) {
				offset = Integer.parseInt(value);
			} else if (KEY_ALIGN.equals(key)) {
				if (value.equalsIgnoreCase("center")) {
					value = "CENTER_OUTER";
				}
				try {
					align = Alignment.valueOf(value.toUpperCase());
				} catch (IllegalArgumentException e) {
					System.out.println("Unknown value: " + value);
				}
			} else if (KEY_PAGE_FORMAT.equals(key)) {
				PaperCatalog pc = PaperCatalog.newInstance();
				//TODO: support reverse orientation
				emb.setFeature(EmbosserFeatures.PAGE_FORMAT, new PageFormat(pc.get(value)));
			} else if (KEY_CELL_WIDTH.equals(key)) {
				emb.setFeature(EmbosserFeatures.CELL_WIDTH, value);
			} else if (KEY_CELL_HEIGHT.equals(key)) {
				emb.setFeature(EmbosserFeatures.CELL_HEIGHT, value);
			} else {
				throw new IllegalArgumentException("Unknown option \"" + key + "\"");
			}
		}
		
		EmbosserWriter embosser = emb.newEmbosserWriter(os);
		PEFHandler.Builder builder = 
			new PEFHandler.Builder(embosser).
				range(range).
				align(align).
				offset(offset);
		PEFHandler ph = builder.build();
		parsePefFile(input, ph);
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

	public static void parseTextFile(String[] args) throws IOException {
		if (args.length < 2 || args.length % 2 != 0) {
			throw new IllegalArgumentException("Wrong number of arguments");
		} else {
			HashMap<String, String> settings = new HashMap<String, String>();
			for (int i=0; i<(args.length-2)/2; i++) {
				settings.put(args[2+i*2].substring(1), args[3+i*2]);
			}
			parseTextFile(new File(args[0]), new File(args[1]), settings);
		}
	}

	/**
	 * String based method matching main args
	 * @param args
	 * @throws IOException 
	 */
	public static void parseTextFile(File input, File output, Map<String, String> settings) throws IOException {
		TextHandler.Builder builder = new TextHandler.Builder(input, output);
		for (String key : settings.keySet()) {
			String value = settings.get(key);
			if (KEY_TITLE.equals(key)) {
				builder.title(value);
			} else if (KEY_AUTHOR.equals(key)) {
				builder.author(value);
			} else if (KEY_IDENTIFIER.equals(key)) {
				builder.identifier(value);
			} else if (KEY_MODE.equals(key)) {
				builder.converterId(value);
			} else if (KEY_LANGUAGE.equals(key)) {
				builder.language(value);
			} else if (KEY_DUPLEX.equals(key)) {
				builder.duplex("true".equals(value.toLowerCase()));
			}else if (KEY_DATE.equals(key)) {
				try {
					builder.date(DATE_FORMAT.parse(value));
				} catch (ParseException e) {
					throw new IllegalArgumentException(e);
				}
			} else {
				throw new IllegalArgumentException("Unknown option \"" + key + "\"");
			}
		}
		TextHandler tp = builder.build();
		tp.parse();
	}	

}