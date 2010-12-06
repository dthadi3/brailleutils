package com_indexbraille;

import java.io.OutputStream;

import org.daisy.braille.embosser.ConfigurableEmbosser;
import org.daisy.braille.embosser.EmbosserTools;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.LineBreaks;
import org.daisy.braille.embosser.SimpleEmbosserProperties;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableCatalog;
import org.daisy.paper.Dimensions;

public class IndexBasicV3Embosser extends IndexEmbosser {

	public IndexBasicV3Embosser(String name, String desc, Enum<? extends Enum<?>> identifier) {
		super(name, desc, identifier);
	}

	@Override
	public boolean supportsDimensions(Dimensions dim) {
		// Supports the following
		double pw = dim.getWidth();
		double ph = dim.getHeight();
		if (pw==210 && (ph==10*EmbosserTools.INCH_IN_MM ||
				ph==11*EmbosserTools.INCH_IN_MM ||
				ph==12*EmbosserTools.INCH_IN_MM) ||
				pw==240 && ph==12*EmbosserTools.INCH_IN_MM) {
			return true;
		}
		return false;
	}

	@Override
	public EmbosserWriter newEmbosserWriter(OutputStream os) {
		if (!supportsDimensions(getPageFormat())) {
			throw new IllegalArgumentException("Unsupported paper for embosser " + getDisplayName());
		}
		String unsupportedPaperFormat = "Unsupported paper size for " + getDisplayName();
		TableCatalog btb = TableCatalog.newInstance();
		Table tc = btb.get(defaultTable.getIdentifier());
		tc.setFeature("fallback", getFeature("fallback"));
		tc.setFeature("replacement", getFeature("replacement"));
		ConfigurableEmbosser.Builder b = new ConfigurableEmbosser.Builder(os, tc.newBrailleConverter())
			.breaks(LineBreaks.Type.DOS)
			.padNewline(ConfigurableEmbosser.Padding.NONE)
			.footer(new byte[]{0x1a})
			.embosserProperties(
					new SimpleEmbosserProperties(EmbosserTools.getWidth(getPageFormat(), getCellWidth()),
							EmbosserTools.getHeight(getPageFormat(), getCellHeight()))
					.supportsDuplex(true)
					.supportsAligning(true)
					);
		double pw = getPageFormat().getWidth();
		double ph = getPageFormat().getHeight();
		//System.err.println(pw + " " + ph);
		if (pw>999 || ph>999) {
			throw new IllegalArgumentException(unsupportedPaperFormat);
		}
		if (pw==210 && ph==10*EmbosserTools.INCH_IN_MM) {
			b.header(new byte[]{
					0x1b, 0x44, 'T', 'D', '0', ';',
					0x1b, 0x44, 'P', 'L', '1', '0', '0', ';', // 10 inch
					0x1b, 0x44, 'P', 'W', '0', '8', '2', ';', // Rounding to the next larger fraction which is 8,333 inch, because 21,0 cm is exactly 35 * 6 mm 
					0x1b, 0x44, 'D', 'P', '2', ';',
					});
		} else if (pw==210 && ph==11*EmbosserTools.INCH_IN_MM) {
			b.header(new byte[]{
					0x1b, 0x44, 'T', 'D', '0', ';',
					0x1b, 0x44, 'P', 'L', '1', '1', '0', ';', // 11 inch
					0x1b, 0x44, 'P', 'W', '0', '8', '2', ';', // Rounding to the next larger fraction which is 8,333 inch, because 21,0 cm is exactly 35 * 6 mm 
					0x1b, 0x44, 'D', 'P', '2', ';',
					});
		} else if (pw==210 && ph==12*EmbosserTools.INCH_IN_MM) {
			b.header(new byte[]{
					0x1b, 0x44, 'T', 'D', '0', ';',
					0x1b, 0x44, 'P', 'L', '1', '2', '0', ';', // 12 inch
					0x1b, 0x44, 'P', 'W', '0', '8', '2', ';', // Rounding to the next larger fraction which is 8,333 inch, because 21,0 cm is exactly 35 * 6 mm 
					0x1b, 0x44, 'D', 'P', '2', ';',
					});
			
		} else if (pw==240 && ph==12*EmbosserTools.INCH_IN_MM) {
			b.header(new byte[]{
					0x1b, 0x44, 'T', 'D', '0', ';',
					0x1b, 0x44, 'P', 'L', '1', '2', '0', ';', // 12 inch
					0x1b, 0x44, 'P', 'W', '0', '9', '3', ';', // Rounding to the next larger fraction which is 8,333 inch, because 21,0 cm is exactly 35 * 6 mm 
					0x1b, 0x44, 'D', 'P', '2', ';',
					});						
		} else {
			throw new IllegalArgumentException(unsupportedPaperFormat);
		}
		return b.build();
	}
}