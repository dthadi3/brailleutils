package com_indexbraille;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.daisy.braille.embosser.AbstractEmbosser;
import org.daisy.braille.embosser.EmbosserFeatures;
import org.daisy.braille.embosser.EmbosserTools;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.FileToDeviceEmbosserWriter;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableCatalog;
import org.daisy.braille.table.TableFilter;
import org.daisy.paper.Dimensions;
import org.daisy.printing.Device;

import com_indexbraille.IndexEmbosserProvider.EmbosserType;

public class BlueBarEmbosser extends AbstractEmbosser {
	private final static TableFilter tableFilter;
	private final static String tableId = IndexTableProvider.class.getCanonicalName() + ".TableType.BLUE_BAR";
	static {
		tableFilter = new TableFilter() {
			@Override
			public boolean accept(Table object) {
				return object.getIdentifier().equals(tableId);
			}
		};
	}

	public BlueBarEmbosser(String name, String desc) {
		super(name, desc, EmbosserType.INDEX_BASIC_BLUE_BAR);
		setFeature(EmbosserFeatures.CELL_WIDTH, 6);
		setFeature(EmbosserFeatures.CELL_HEIGHT, 10);
	}

	@Override
	public TableFilter getTableFilter() {
		return tableFilter;
	}

	@Override
	public boolean supportsDimensions(Dimensions dim) {
		//TODO: Verify that this value is correct
		int w = EmbosserTools.getWidth(dim, getCellWidth());
		if (dim!=null && w <= 99) { 
			return true;
		} else { return false; }
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
	
	@Override
	public EmbosserWriter newEmbosserWriter(OutputStream os) {
		if (!supportsDimensions(getPageFormat())) {
			throw new IllegalArgumentException("Unsupported paper for embosser " + getDisplayName());
		}
		TableCatalog btb = TableCatalog.newInstance();
		Table tc = btb.get(tableId);
		tc.setFeature("fallback", getFeature("fallback"));
		tc.setFeature("replacement", getFeature("replacement"));
		return new BlueBarEmbosserWriter(
				os, 
				tc.newBrailleConverter(), 
				EmbosserTools.getWidth(getPageFormat(), getCellWidth()),
				EmbosserTools.getHeight(getPageFormat(), getCellHeight())
			);
	}

}
