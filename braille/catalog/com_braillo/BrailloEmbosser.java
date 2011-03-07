package com_braillo;

import org.daisy.braille.embosser.AbstractEmbosser;
import org.daisy.braille.embosser.EmbosserFeatures;
import org.daisy.braille.table.DefaultTableProvider;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableFilter;

public abstract class BrailloEmbosser extends AbstractEmbosser {
	private final static TableFilter tableFilter;
	static {
		tableFilter = new TableFilter() {
			public boolean accept(Table object) {
				if (object.getIdentifier().equals(DefaultTableProvider.class.getCanonicalName() + ".TableType.EN_US")) { return true; }
				if (object.getIdentifier().startsWith(BrailloTableProvider.class.getCanonicalName() + ".TableType.")) { return true; }
				return false;
			}
		};
	}

	public BrailloEmbosser(String name, String desc,  Enum<? extends Enum<?>> identifier) {
		super(name, desc, identifier);
		//TODO: fix this, width is 6.0325
		setFeature(EmbosserFeatures.CELL_WIDTH, 6);
		setFeature(EmbosserFeatures.CELL_HEIGHT, 10);
	}

	//jvm1.6@Override
	public TableFilter getTableFilter() {
		return tableFilter;
	}

}