package com_indexbraille;

import java.io.OutputStream;

import org.daisy.braille.embosser.ConfigurableEmbosser;
import org.daisy.braille.embosser.EmbosserFactoryException;
import org.daisy.braille.embosser.EmbosserTools;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.SimpleEmbosserProperties;
import org.daisy.braille.embosser.StandardLineBreaks;
import org.daisy.braille.embosser.UnsupportedPaperException;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableCatalog;
import org.daisy.paper.Dimensions;
import org.daisy.paper.PageFormat;

public class IndexEverestV3Embosser extends IndexEmbosserOLD { // CAN BE REMOVED

	public IndexEverestV3Embosser(String name, String desc, Enum<? extends Enum<?>> identifier) {
		super(name, desc, identifier);
	}

	//jvm1.6@Override
	public boolean supportsDimensions(Dimensions dim) {
		// Supports paper formats smaller than 100 cm wide and 58.5 cm high
		int width = (int)Math.round(dim.getWidth());
		int height = (int)Math.round(dim.getHeight());
		if (width > 999 || height > 585 || height < 100) {
			return false;
		}
		return true;
	}

	//jvm1.6@Override
	public EmbosserWriter newEmbosserWriter(OutputStream os) {
		if (!supportsDimensions(getPageFormat())) {
			throw new IllegalArgumentException("Unsupported paper for embosser " + getDisplayName());
		}
		try {
			byte[] header = getEverestV3Header(getPageFormat());
			TableCatalog btb = TableCatalog.newInstance();
			Table tc = btb.get(defaultTable.getIdentifier());
			tc.setFeature("fallback", getFeature("fallback"));
			tc.setFeature("replacement", getFeature("replacement"));
			ConfigurableEmbosser.Builder b = new ConfigurableEmbosser.Builder(os, tc.newBrailleConverter())
				.breaks(new StandardLineBreaks(StandardLineBreaks.Type.DOS))
				.padNewline(ConfigurableEmbosser.Padding.NONE)
				.footer(new byte[]{0x1a})
				.embosserProperties(
						new SimpleEmbosserProperties(EmbosserTools.getWidth(getPageFormat(), getCellWidth()),
								EmbosserTools.getHeight(getPageFormat(), getCellHeight()))
						.supportsDuplex(true)
						.supportsAligning(true)
						);
			b.header(header);
			return b.build();
		} catch (EmbosserFactoryException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private byte[] getEverestV3Header(PageFormat pageFormat) throws UnsupportedPaperException {
		int width = (int)Math.round(pageFormat.getWidth());
		int height = (int)Math.round(pageFormat.getHeight());
		if (!supportsDimensions(pageFormat)) {
			throw new UnsupportedPaperException("Unsupported paper: " + width + " x " + height);
		}
		byte[] w = EmbosserTools.toBytes(width, 3);
		byte[] h = EmbosserTools.toBytes(height, 3);
		return new byte[]{
			0x1b, 0x44, 'T', 'D', '0', ';',
			0x1b, 0x44, 'P', 'L', h[0], h[1], h[2], ';',
			0x1b, 0x44, 'P', 'W', w[0], w[1], w[2], ';',
			0x1b, 0x44, 'D', 'P', '2', ';',
		};
	}

    public boolean supportsVolumes() {
        return false;
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
}
