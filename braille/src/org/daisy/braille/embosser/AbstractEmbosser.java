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
package org.daisy.braille.embosser;

import java.util.HashMap;

import org.daisy.braille.table.DefaultTableProvider;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableCatalog;
import org.daisy.factory.AbstractFactory;
import org.daisy.paper.PageFormat;

/**
 * Provides an abstract base for Embossers, implementing basic features
 * such as the ability to set page format, table, and cell height and width.
 * @author Joel Håkansson
 */
public abstract class AbstractEmbosser extends AbstractFactory implements Embosser {
	private final HashMap<String, Object> props;
	private final HashMap<String, String> settings;
	private double cellHeight = 10;
	private double cellWidth = 6;
	protected final Table defaultTable;
	private PageFormat pageFormat;
	protected Table setTable;

	/**
	 * Creates a new AbstractEmbosser with the supplied name, description and identifier
	 * @param name the embosser name
	 * @param desc the embosser description
	 * @param identifier an identifier
	 */
	public AbstractEmbosser(String name, String desc,  Enum<? extends Enum<?>> identifier) {
		super(name, desc, identifier);
		this.props = new HashMap<String, Object>();
		this.settings = new HashMap<String, String>();
		defaultTable = TableCatalog.newInstance().get(DefaultTableProvider.class.getCanonicalName() + ".TableType.EN_US");
		setTable = defaultTable;
	}
	
	/**
	 * Set cell width, in millimeters
	 * @param val the width, in millimeters
	 */
	protected void setCellWidth(double val) {
		cellWidth = val;
	}
	
	/**
	 * Set cell height, in millimeters
	 * @param val the height, in millimeters
	 */
	protected void setCellHeight(double val) {
		cellHeight = val;
	}
	
	/**
	 * Gets the page format
	 * @return returns the page format
	 */
	protected PageFormat getPageFormat() {
		return pageFormat;
	}
	
	/**
	 * Gets cell width, in millimeters
	 * @return returns cell width, in millimeters
	 */
	public double getCellWidth() {
		return cellWidth;
	}
	
	/**
	 * Gets cell height, in millimeters
	 * @return returns cell height, in millimeters
	 */
	public double getCellHeight() {
		return cellHeight;
	}

	//jvm1.6@Override
	public int getMaxHeight(PageFormat pageFormat) {
		return EmbosserTools.getHeight(pageFormat, cellHeight);
	}

	//jvm1.6@Override
	public int getMaxWidth(PageFormat pageFormat) {
		return EmbosserTools.getWidth(pageFormat, cellWidth);
	}

	public Object getFeature(String key) {
		return settings.get(key);
	}

	//jvm1.6@Override
	public Object getProperty(String key) {
		return props.get(key);
	}

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for this key, the old
     * value is replaced.
     *
     * @param key key with which the specified value is to be associated.
     * @param value value to be associated with the specified key.
     */
 	public void setFeature(String key, Object value) {
		if (EmbosserFeatures.PAGE_FORMAT.equals(key)) {
			try {
				pageFormat = (PageFormat)value;
			} catch (ClassCastException e) {
				throw new IllegalArgumentException("Unsupported value for pageFormat: '" + value + "'", e);
			}
		} else if (EmbosserFeatures.TABLE.equals(key)) {
			try {
				setTable = (Table)value;
			} catch (ClassCastException e) {
				setTable = TableCatalog.newInstance().get(value.toString());
				if (setTable == null) {
					throw new IllegalArgumentException("Unsupported value for table: '" + value + "'");
				}
			}
		}
		else {
			if (EmbosserFeatures.CELL_WIDTH.equals(key) && !"6".equals(value.toString())) {
				throw new IllegalArgumentException("Changing cell width has not been implemented.");
			} else if (EmbosserFeatures.CELL_HEIGHT.equals(key) && !"10".equals(value.toString())) {
				throw new IllegalArgumentException("Changing cell height has not been implemented.");
			}
			settings.put(key, value.toString());
		}
	}
 	
 	//jvm1.6@Override
	public boolean supportsTable(Table table) {
		return getTableFilter().accept(table);
	}

}