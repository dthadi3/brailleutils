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
			//jvm1.6@Override
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

	//jvm1.6@Override
	public TableFilter getTableFilter() {
		return tableFilter;
	}

	//jvm1.6@Override
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
