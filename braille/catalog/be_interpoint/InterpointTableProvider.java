package be_interpoint;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;

import org.daisy.braille.table.AbstractConfigurableTableProvider;
import org.daisy.braille.table.BrailleConverter;
import org.daisy.braille.table.EmbosserBrailleConverter;
import org.daisy.braille.table.EmbosserBrailleConverter.EightDotFallbackMethod;
import org.daisy.braille.table.Table;

/**
 *
 * @author Bert Frees
 * @author Joel Håkansson
 */
public class InterpointTableProvider extends AbstractConfigurableTableProvider<InterpointTableProvider.TableType> {
    
    enum TableType { USA1_8 };

    private final ArrayList<Table> tables;

    public InterpointTableProvider() {
        super(EightDotFallbackMethod.values()[0], '\u2800');
        tables = new ArrayList<Table>();
      //tables.add(new EmbosserTable<TableType>("USA1_8 font", "Interpoint 8-dot table", TableType.USA1_8, this));
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
            case USA1_8: {
                String table = " a,b.k;l\"cif|msp!e:h*o+r>djg`ntq'1?2-u(v$3960x~&<5/8)z={\u007f4w7#y}%"; // TODO: add patterns U+2840 - U+28ff

                StringBuffer sb = new StringBuffer();
                sb.append(table);
                return new EmbosserBrailleConverter(sb.toString(), Charset.forName("ISO-8859-1"), fallback, replacement, true);
            }
            default:
                throw new IllegalArgumentException("Cannot find table type " + t);
        }
    }

    //jvm1.6@Override
    public Collection<Table> list() {
        return tables;
    }
}
