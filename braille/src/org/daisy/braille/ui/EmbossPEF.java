/*
 * Braille Utils (C) 2010-2011 Daisy Consortium 
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
package org.daisy.braille.ui;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.prefs.BackingStoreException;

import javax.print.PrintService;
import javax.xml.parsers.ParserConfigurationException;

import org.daisy.braille.embosser.Embosser;
import org.daisy.braille.embosser.EmbosserCatalog;
import org.daisy.braille.embosser.EmbosserFeatures;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.UnsupportedWidthException;
import org.daisy.braille.facade.PEFConverterFacade;
import org.daisy.braille.facade.PEFValidatorFacade;
import org.daisy.braille.pef.PEFHandler;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableCatalog;
import org.daisy.factory.Factory;
import org.daisy.paper.PageFormat;
import org.daisy.paper.Paper;
import org.daisy.paper.PaperCatalog;
import org.daisy.paper.PaperFilter;
import org.daisy.printing.PrinterDevice;
import org.xml.sax.SAXException;

/**
 * Provides a UI for embossing a PEF-file. 
 * @author Joel Håkansson
 * @deprecated Not for public use.
 *    This class is expected to be retained only as a package
 *    class. Use BasicUI
 */
public class EmbossPEF {
	public static String DEVICE_NAME = "device name";
	public static String EMBOSSER_TYPE = "embosser type";
	public static String TABLE_TYPE = "table type";
	public static String PAPER_SIZE = "paper size";

	private String deviceName;
	private Embosser type;
	private Table table;
	private Paper paper;
	
	public EmbossPEF() {
		
	}
	
	protected void readSetup(boolean verify) {
		// Check setup
		InputHelper input = new InputHelper();
		ArrayList<String> str = new ArrayList<String>();
		for (PrintService ps : PrinterDevice.getDevices()) {
			str.add(ps.getName());
		}
		deviceName = input.select(DEVICE_NAME, str.toArray(new String[0]), "device", verify); 
		System.out.println("Using device: " + deviceName);
		
		EmbosserCatalog ec = EmbosserCatalog.newInstance();
		String embosserType = input.select(EMBOSSER_TYPE, new ArrayList<Factory>(ec.list()), "embosser", verify);
		type = ec.get(embosserType);
		System.out.println("Embosser: " + type.getDisplayName());
		
		TableCatalog tablef = TableCatalog.newInstance();
		Collection<Table> supportedTables = tablef.list(type.getTableFilter());
		if (supportedTables.size()>1) {
			String tableType = input.select(TABLE_TYPE, new ArrayList<Factory>(supportedTables), "table", verify);
			table = tablef.get(tableType);
			System.out.println("Table: " + table.getDisplayName());
		} else {
			table = null;
		}

		PaperCatalog pc = PaperCatalog.newInstance();
		String paperSize = input.select(PAPER_SIZE, new ArrayList<Factory>(
				pc.list(new EmbosserPaperFilter(type))), "paper", verify
			);
		paper = pc.get(paperSize);
		System.out.println("Paper: " + paper.getDisplayName());
	}
	
	public Table getTable() {
		return table;
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	
	public Paper getPaper() {
		return paper;
	}
	
	public Embosser getEmbosser() {
		return type;
	}
	
	public static void main(String[] args) throws BackingStoreException {
		if (args.length!=1) {
			System.out.println("Expected one more argument: path to PEF-file");
			System.out.println(" or -clear to clear settings");
			System.out.println(" or -setup to change setup");
			System.exit(-1);
		}
		
		EmbossPEF obj = new EmbossPEF();

		if ("-clear".equalsIgnoreCase(args[0])) {
			InputHelper h = new InputHelper(obj.getClass());
			h.clearSettings();
			System.out.println("Settings have been cleared.");
			System.exit(0);
		}

		if ("-setup".equalsIgnoreCase(args[0])) {
			obj.readSetup(true);
			obj.listCurrentSettings(System.out);
			System.exit(0);
		} else {
			obj.readSetup(false);
		}

		PrinterDevice device = new PrinterDevice(obj.getDeviceName(), true);

		//TODO: support reverse orientation
		obj.getEmbosser().setFeature(EmbosserFeatures.PAGE_FORMAT, new PageFormat(obj.getPaper()));
		
		if (obj.getTable()!=null) {
			obj.getEmbosser().setFeature(EmbosserFeatures.TABLE, obj.getTable());
		}

		File input = new File(args[0]);
		if (!input.exists()) {
			throw new RuntimeException("Cannot find input file: " + args[0]);
		}
		try {
			boolean ok = PEFValidatorFacade.validate(input, System.out);
			if (!ok) {
				System.out.println("Validation failed, exiting...");
				System.exit(-2);
			}
			EmbosserWriter embosserObj = obj.getEmbosser().newEmbosserWriter(device);
			PEFHandler ph = new PEFHandler.Builder(embosserObj).build();
			PEFConverterFacade.parsePefFile(input, ph);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (UnsupportedWidthException e) {
			e.printStackTrace();
		}
	}
	
	public void listCurrentSettings(PrintStream ps) {
		ps.println("Current settings:");
		ps.println("\tDevice: " + deviceName);
		ps.println("\tEmbosser: " + type.getDisplayName());
		if (table!=null) {
			ps.println("\tTable: " + table.getDisplayName());
		}
		ps.println("\tPaper: " + paper.getDisplayName());
	}
	
	// Accepts papers that are supported by the embosser in the default orientation
	private class EmbosserPaperFilter implements PaperFilter {
			private final Embosser emb;

			public EmbosserPaperFilter(Embosser emb) {
				this.emb = emb;
			}
			
			//jvm1.6@Override
			public boolean accept(Paper object) {
				return emb.supportsDimensions(object);
			}

	}

}
