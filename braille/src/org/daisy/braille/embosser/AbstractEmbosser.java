package org.daisy.braille.embosser;

import java.util.HashMap;

import org.daisy.braille.table.DefaultTableProvider;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableCatalog;
import org.daisy.factory.AbstractFactoryObject;
import org.daisy.paper.PageFormat;

public abstract class AbstractEmbosser extends AbstractFactoryObject implements Embosser {
	private final HashMap<String, Object> props;
	private final HashMap<String, String> settings;
	private double cellHeight = 10;
	private double cellWidth = 6;
	protected final Table defaultTable;
	private PageFormat pageFormat;
	protected Table setTable;


	public AbstractEmbosser(String name, String desc,  Enum<? extends Enum<?>> identifier) {
		super(name, desc, identifier);
		this.props = new HashMap<String, Object>();
		this.settings = new HashMap<String, String>();
		defaultTable = TableCatalog.newInstance().get(DefaultTableProvider.class.getCanonicalName() + ".TableType.EN_US");
		setTable = defaultTable;
	}
	
	protected void setCellWidth(double val) {
		cellWidth = val;
	}
	
	protected void setCellHeight(double val) {
		cellHeight = val;
	}
	
	protected PageFormat getPageFormat() {
		return pageFormat;
	}
	
	public double getCellWidth() {
		return cellWidth;
	}
	
	public double getCellHeight() {
		return cellHeight;
	}

	@Override
	public int getMaxHeight(PageFormat pageFormat) {
		return EmbosserTools.getHeight(pageFormat, cellHeight);
	}

	@Override
	public int getMaxWidth(PageFormat pageFormat) {
		return EmbosserTools.getWidth(pageFormat, cellWidth);
	}

	public Object getFeature(String key) {
		return settings.get(key);
	}

	@Override
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
 	
	@Override
	public boolean supportsTable(Table table) {
		return getTableFilter().accept(table);
	}

}