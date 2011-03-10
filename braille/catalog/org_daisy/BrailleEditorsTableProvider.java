package org_daisy;

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
public class BrailleEditorsTableProvider extends AbstractConfigurableTableProvider<BrailleEditorsTableProvider.TableType> {
	enum TableType {
		MICROBRAILLE
	};

	private final ArrayList<Table> tables;
	
	public BrailleEditorsTableProvider() {
		super(EightDotFallbackMethod.values()[0], '\u2800');
		tables = new ArrayList<Table>();
		tables.add(new EmbosserTable<TableType>("MicroBraille BRL-file", "Table for MicroBraille files (.brl)", TableType.MICROBRAILLE, this));
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
                case MICROBRAILLE:
                        return new EmbosserBrailleConverter(new String(" á¬â§ë»ì«ãéæ¯íóð½åºèªï¡òüäêçÁîôñ¾ÜÎÇ­õ¿öûÓ¼ÄÉøÝÛß×®Ï¢ú¨àýÔ÷Å£ùÐÆ"), Charset.forName("UTF-8"), fallback, replacement, false);

                default:
                        throw new IllegalArgumentException("Cannot find table type " + t);
            }
	}

	//jvm1.6@Override
	public Collection<Table> list() {
		return tables;
	}

}
