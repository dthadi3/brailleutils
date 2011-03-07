package com_yourdolphin;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import org.daisy.braille.table.AbstractConfigurableTableProvider;
import org.daisy.braille.table.BrailleConverter;
import org.daisy.braille.table.EmbosserBrailleConverter;
import org.daisy.braille.table.EmbosserBrailleConverter.EightDotFallbackMethod;
import org.daisy.braille.table.EmbosserTable;
import org.daisy.braille.table.Table;

public class SupernovaTableProvider extends AbstractConfigurableTableProvider<SupernovaTableProvider.TableType> {
	enum TableType {SV_SE_6DOT};

	private final ArrayList<Table> tables;
	
	public SupernovaTableProvider() {
		super(EightDotFallbackMethod.values()[0], '\u2800');
		tables = new ArrayList<Table>();
		tables.add(new EmbosserTable<TableType>("Swedish - Supernova 6 dot", "Table for Supernova, using 6 dot", TableType.SV_SE_6DOT, this));
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
		case SV_SE_6DOT:
			return new EmbosserBrailleConverter(
					new String(" a,b.k;l@cif/msp'e:h*o!rødjgäntq_å?ê-u(v`îöë§xèç\"û+ü)z=àœôwï#yùé"), 
					Charset.forName("UTF-8"), fallback, replacement, true);
		default:
			throw new IllegalArgumentException("Cannot find table type "
					+ t);
		}
	}
	
	//jvm1.6@Override
	public Collection<Table> list() {
		return tables;
	}

}
