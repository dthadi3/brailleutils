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
import org.daisy.paper.PrintPage;
import org.daisy.printing.Device;


/**
 * Provides an Embosser for Braillo 200/400S/400SR
 * @author Joel Håkansson
 */
public class Braillo200Embosser extends BrailloEmbosser {
	private final PageFormat.Type pageFormatType;

	//jvm1.6@Override
	public boolean supportsPrintPage(PrintPage dim) {
		int height = (int)Math.ceil(2*dim.getHeight()/EmbosserTools.INCH_IN_MM);
		int width = EmbosserTools.getWidth(dim, 6);
		if (width > 42 || height > 28) { 
			return false; 
		}
		if (width < 10 || height < 8) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean supportsPageFormat(PageFormat dim) {
		return dim.getPageFormatType()==pageFormatType && super.supportsPageFormat(dim);
	}

	public Braillo200Embosser(String name, String desc, Enum<? extends Enum<?>> identifier, PageFormat.Type pageFormatType) {
		super(name, desc, identifier);
		this.pageFormatType = pageFormatType;
	}

	//jvm1.6@Override
	public EmbosserWriter newEmbosserWriter(OutputStream os) {
		try {
			TableCatalog btb = TableCatalog.newInstance();
			Table tc = btb.get(setTable.getIdentifier());
			tc.setFeature("fallback", getFeature("fallback"));
			tc.setFeature("replacement", getFeature("replacement"));
			PrintPage printPage = getPrintPage(getPageFormat());
			EmbosserWriterProperties ep = new SimpleEmbosserProperties(
					EmbosserTools.getWidth(printPage, getCellWidth()),
					EmbosserTools.getHeight(printPage, getCellHeight()))
				.supportsDuplex(true)
				.supportsAligning(true);
			ConfigurableEmbosser.Builder b = new ConfigurableEmbosser.Builder(os, tc.newBrailleConverter())
				.breaks(new StandardLineBreaks(StandardLineBreaks.Type.DOS))
				.padNewline(ConfigurableEmbosser.Padding.NONE) // JH100408: changed from BEFORE
				.embosserProperties(ep)
				.header(getBrailloHeader(ep.getMaxWidth(), printPage))
				.fillSheet(true)
				.autoLineFeedOnEmptyPage(true);
			return b.build();
		} catch (EmbosserFactoryException e) {
			throw new IllegalArgumentException(e);
		}
	}

	//jvm1.6@Override
	public EmbosserWriter newEmbosserWriter(Device device) {
		if (!supportsPrintPage(getPrintPage(getPageFormat()))) {
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
	
	// B200, B400S, B400SR
	// Supported paper width (chars): 10 <= width <= 42
	// Supported paper height (inches): 4 <= height <= 14
	private static byte[] getBrailloHeader(int width, Dimensions pageFormat) throws UnsupportedPaperException {
		// Round to the closest possible higher value, so that all characters fit on the page
		int height = (int)Math.ceil(2*pageFormat.getHeight()/EmbosserTools.INCH_IN_MM);
		if (width > 42 || height > 28) { 
			throw new UnsupportedPaperException("Paper too wide or high: " + width + " chars x " + height / 2d + " inches."); 
		}
		if (width < 10 || height < 8) {
			throw new UnsupportedPaperException("Paper too narrow or short: " + width + " chars x " + height / 2d + " inches.");
		}
		byte[] w = EmbosserTools.toBytes(width, 2);
		byte[] h = EmbosserTools.toBytes(height, 2);
		return new byte[] {
			0x1b, 'S', '1',
			0x1b, 'C', '1',
			0x1b, 'J', '0',
			0x1b, 'A', h[0], h[1],
			0x1b, 'B', w[0], w[1],
			0x1b, 'N', '0',
			0x1b, 'R', '0'
		};
	}

    public boolean supports8dot() {
        return false;
    }

    public boolean supportsDuplex() {
        return true;
    }

    public boolean supportsAligning() {
        return true;
    }

    public boolean supportsVolumes() {
        return false;
    }
}
