package se_tpb;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import org.daisy.braille.table.AbstractConfigurableTableProvider;
import org.daisy.braille.table.BrailleConverter;
import org.daisy.braille.table.EmbosserBrailleConverter;
import org.daisy.braille.table.EmbosserBrailleConverter.EightDotFallbackMethod;
import org.daisy.braille.table.EmbosserTable;
import org.daisy.braille.table.Table;

public class CXTableProvider extends AbstractConfigurableTableProvider<CXTableProvider.TableType> {
//	public final static String IS_ONE_TO_ONE = "is one-to-one";
//	public final static String IS_DISPLAY_FORMAT = "is display format";
	
	enum TableType {
		SV_SE_CX 
	};

	private final ArrayList<Table> tables;

	public CXTableProvider() {
		super(EightDotFallbackMethod.values()[0], '\u2800');
		tables = new ArrayList<Table>(); 
		tables.add(new EmbosserTable<TableType>("Swedish CX", "Matches the Swedish representation in CX", TableType.SV_SE_CX, this));
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
		case SV_SE_CX:
			return new EmbosserBrailleConverter(
					new String(
							" a,b.k;l^cif/msp'e:h*o!r~djgäntq_å?ê-u(v@îöë§xèç\"û+ü)z=à|ôwï#yùé"),
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
