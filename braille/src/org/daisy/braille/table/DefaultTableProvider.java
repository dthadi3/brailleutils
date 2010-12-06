package org.daisy.braille.table;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import org.daisy.braille.table.EmbosserBrailleConverter.EightDotFallbackMethod;

public class DefaultTableProvider extends AbstractConfigurableTableProvider<DefaultTableProvider.TableType> {
	
	enum TableType {
		EN_US, // US computer braille, compatible with
				// "Braillo USA 6 DOT 001.00"
	};

	private final ArrayList<Table> tables;

	public DefaultTableProvider() {
		super(EightDotFallbackMethod.values()[0], '\u2800');
		tables = new ArrayList<Table>(); 
		tables.add(new EmbosserTable<TableType>("US computer braille", "Commonly used embosser table", TableType.EN_US, this));
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
		case EN_US:
			return new EmbosserBrailleConverter(
					new String(
							" A1B'K2L@CIF/MSP\"E3H9O6R^DJG>NTQ,*5<-U8V.%[$+X!&;:4\\0Z7(_?W]#Y)="),
					Charset.forName("UTF-8"), fallback, replacement, true);
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
