package com_braillo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.daisy.braille.embosser.ConfigurableEmbosser;
import org.daisy.braille.embosser.EmbosserFactoryException;
import org.daisy.braille.embosser.EmbosserTools;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.EmbosserWriterProperties;
import org.daisy.braille.embosser.FileToDeviceEmbosserWriter;
import org.daisy.braille.embosser.SimpleEmbosserProperties;
import org.daisy.braille.embosser.StandardLineBreaks;
import org.daisy.braille.embosser.UnsupportedPaperException;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableCatalog;
import org.daisy.paper.Dimensions;
import org.daisy.paper.PageFormat;
import org.daisy.printing.Device;


/**
 * Provides an Embosser for Braillo 200/270/400 firmware 12-16
 * @author Joel Håkansson
 */
public class Braillo200_270_400_v12_16Embosser extends BrailloEmbosser {
	private final static byte[] halfInchToSheetLength = new byte[]{	'0', '1', '1', '2', '2', '3', '3', '4', '4', '5',
																'5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	@Override
	public boolean supportsDimensions(Dimensions dim) {
		int height = (int)Math.ceil(dim.getHeight()/EmbosserTools.INCH_IN_MM);
		int width = EmbosserTools.getWidth(dim, getCellWidth());
		if (width < 27 || width > 42) {
			return false;
		}
		if (height < 4 || height > 14) { 
			return false; 
		}
		return true;
	}
	
	public Braillo200_270_400_v12_16Embosser(String name, String desc, Enum<? extends Enum<?>> identifier) {
		super(name, desc, identifier);
	}

	@Override
	public EmbosserWriter newEmbosserWriter(OutputStream os) {
		try {
			TableCatalog btb = TableCatalog.newInstance();
			Table tc = btb.get(setTable.getIdentifier());
			tc.setFeature("fallback", getFeature("fallback"));
			tc.setFeature("replacement", getFeature("replacement"));
			EmbosserWriterProperties ep = new SimpleEmbosserProperties(
					EmbosserTools.getWidth(getPageFormat(), getCellWidth()),
					EmbosserTools.getHeight(getPageFormat(), getCellHeight()))
				.supportsDuplex(true)
				.supportsAligning(true);
			ConfigurableEmbosser.Builder b = new ConfigurableEmbosser.Builder(os, tc.newBrailleConverter())
				.breaks(new StandardLineBreaks(StandardLineBreaks.Type.DOS))
				.padNewline(ConfigurableEmbosser.Padding.NONE)
				.embosserProperties(ep)
				.header(getBrailloHeader(ep.getMaxWidth(), getPageFormat()))
				.fillSheet(true)
				.autoLineFeedOnEmptyPage(true);
			return b.build();
		} catch (EmbosserFactoryException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public EmbosserWriter newEmbosserWriter(Device device) {
		if (!supportsDimensions(getPageFormat())) {
			throw new IllegalArgumentException("Unsupported paper for embosser " + getDisplayName());
		}
		try {
			File f = File.createTempFile(this.getClass().getCanonicalName(), ".tmp");
			f.deleteOnExit();
			EmbosserWriter ew = newEmbosserWriter(new FileOutputStream(f));
			return new FileToDeviceEmbosserWriter(ew, f, device);
		} catch (IOException e) {
			// do nothing, fail
		}
		throw new IllegalArgumentException("Embosser does not support this feature.");
	}
	
	// B200, B270, B400
	// Supported paper width (chars): 27 <= width <= 42
	// Supported paper height (inches): 4 <= height <= 14
	private static byte[] getBrailloHeader(int width, PageFormat pageFormat) throws UnsupportedPaperException {
		// Round to the closest possible higher value, so that all characters fit on the page
		int height = (int)Math.ceil(2*pageFormat.getHeight()/EmbosserTools.INCH_IN_MM);
		if (width > 42 || height > 28) { 
			throw new UnsupportedPaperException("Paper too wide or high: " + width + " chars x " + height / 2d + " inches."); 
		}
		if (width < 27 || height < 8) {
			throw new UnsupportedPaperException("Paper too narrow or short: " + width + " chars x " + height / 2d + " inches.");
		}
		return new byte[] {
			0x1b, 'E',					// Normal form feed
			0x1b, 'S', '1',				// Print format interpoint
			0x1b, '6',					// 6 dot
			0x1b, 0x1F,	(byte)Integer.toHexString(width-27).toUpperCase().charAt(0),
										// Line length
			0x1b, 0x1E,	halfInchToSheetLength[height-8],
										// Sheet length
			0x1b, 'A'					// Single line spacing
		};
	}

}
