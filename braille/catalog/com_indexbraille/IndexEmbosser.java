package com_indexbraille;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.daisy.braille.embosser.AbstractEmbosser;
import org.daisy.braille.embosser.EmbosserFeatures;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.FileToDeviceEmbosserWriter;
import org.daisy.braille.table.DefaultTableProvider;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableFilter;
import org.daisy.printing.Device;

public abstract class IndexEmbosser extends AbstractEmbosser {
	private final static TableFilter tableFilter;
	static {
		tableFilter = new TableFilter() {
			@Override
			public boolean accept(Table object) {
				return object.getIdentifier().equals(DefaultTableProvider.class.getCanonicalName() + ".TableType.EN_US");
			}
		};
	}
	
	public IndexEmbosser(String name, String desc,  Enum<? extends Enum<?>> identifier) {
		super(name, desc, identifier);
		setFeature(EmbosserFeatures.CELL_WIDTH, 6);
		setFeature(EmbosserFeatures.CELL_HEIGHT, 10);
	}

	@Override
	public TableFilter getTableFilter() {
		return tableFilter;
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

}
