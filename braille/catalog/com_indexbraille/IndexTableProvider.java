package com_indexbraille;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import org.daisy.braille.table.AbstractConfigurableTableProvider;
import org.daisy.braille.table.BrailleConverter;
import org.daisy.braille.table.EmbosserBrailleConverter;
import org.daisy.braille.table.EmbosserBrailleConverter.EightDotFallbackMethod;
import org.daisy.braille.table.EmbosserTable;
import org.daisy.braille.table.Table;

/**
 * 
 * @author Bert Frees
 * @author Joel Håkansson
 */
public class IndexTableProvider extends AbstractConfigurableTableProvider<IndexTableProvider.TableType> {
	enum TableType {BLUE_BAR};

	private final ArrayList<Table> tables;
	
	public IndexTableProvider() {
		super(EightDotFallbackMethod.values()[0], '\u2800');
		tables = new ArrayList<Table>();
		tables.add(new EmbosserTable<TableType>("Index Basic \"blue bar\"", "Table for Index Basic \"blue bar\"", TableType.BLUE_BAR, this));
	}

	/**
	 * Get a new table instance based on the factory's current settings.
	 * 
	 * @param t
	 *            the type of table to return, this will override the factory's
	 *            default table type.
	 * @return returns a new table instance.
	 */
	public BrailleConverter newTable(TableType t) {
		switch (t) {
			case BLUE_BAR:
				StringBuilder sb = new StringBuilder();
				for (int i=0; i<64; i++) {
					sb.append((char)((i & 0x07) + ((i & 0x38) << 1)));
				}
				return new EmbosserBrailleConverter(sb.toString(), Charset.forName("US-ASCII"), fallback, replacement, false); 
			default:
				throw new IllegalArgumentException("Cannot find table type "
						+ t);
		}
	}

	@Override
	public Collection<Table> list() {
		return tables;
	}

}
