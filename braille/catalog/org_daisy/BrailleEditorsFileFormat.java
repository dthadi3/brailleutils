package org_daisy;

import java.util.Collection;
import java.util.ArrayList;
import java.io.OutputStream;

import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableCatalog;
import org.daisy.braille.table.TableFilter;
import org.daisy.braille.embosser.FileFormat;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.EmbosserFeatures;
import org.daisy.braille.embosser.StandardLineBreaks;
import org.daisy.braille.embosser.EmbosserWriterProperties;
import org.daisy.braille.embosser.SimpleEmbosserProperties;
import org.daisy.braille.embosser.ConfigurableEmbosser;
import org.daisy.braille.embosser.AbstractEmbosserWriter.Padding;

import org_daisy.BrailleEditorsFileFormatProvider.FileType;


/**
 *
 * @author Bert Frees
 */
public class BrailleEditorsFileFormat implements FileFormat {

    private FileType type;
    private Table table;
    private TableCatalog tableCatalog;
    private TableFilter tableFilter;
    private final Collection<String> supportedTableIds = new ArrayList<String>();

    public BrailleEditorsFileFormat(FileType identifier) {

        switch (identifier) {
            case BRF:
                supportedTableIds.add("org.daisy.braille.table.DefaultTableProvider.TableType.EN_US");
                supportedTableIds.add("org_daisy.EmbosserTableProvider.TableType.EN_GB");
                supportedTableIds.add("org_daisy.EmbosserTableProvider.TableType.NL_NL");
                supportedTableIds.add("org_daisy.EmbosserTableProvider.TableType.EN_GB");
                supportedTableIds.add("org_daisy.EmbosserTableProvider.TableType.DA_DK");
                supportedTableIds.add("org_daisy.EmbosserTableProvider.TableType.DE_DE");
                supportedTableIds.add("org_daisy.EmbosserTableProvider.TableType.IT_IT_FIRENZE");
                supportedTableIds.add("org_daisy.EmbosserTableProvider.TableType.ES_ES");
                supportedTableIds.add("org_daisy.EmbosserTableProvider.TableType.ES_ES_TABLE_2");
                break;
            case BRA:
                supportedTableIds.add("org_daisy.EmbosserTableProvider.TableType.ES_ES");
                supportedTableIds.add("org_daisy.EmbosserTableProvider.TableType.ES_ES_TABLE_2");
                break;
            case BRL:
                supportedTableIds.add("org_daisy.BrailleEditorsTableProvider.TableType.BRL");
                break;
            default:
                throw new IllegalArgumentException("Unsupported filetype");
        }

        tableFilter = new TableFilter() {
            @Override
            public boolean accept(Table object) {
                return supportedTableIds.contains(object.getIdentifier());
            }
        };

        tableCatalog = TableCatalog.newInstance();
        table = tableCatalog.list(tableFilter).iterator().next();
    }

    public TableFilter getTableFilter() {
            return tableFilter;
    }
    
    public boolean supportsTable(Table table) {
        return getTableFilter().accept(table);
    }

    public boolean supportsDuplex() {
        return false;
    }

    public boolean supports8dot() {
        return false;
    }

    public EmbosserWriter newEmbosserWriter(OutputStream os) {

        if (!supportsTable(table)) {
            throw new IllegalArgumentException("Unsupported table: " + table.getDisplayName());
        }

        boolean duplexEnabled = supportsDuplex() && false; // ??? examine PEF file => Contract?
        boolean eightDots = supports8dot() && false;       // ???
        int cols = 25;                                     // ???
        int rows = 40;                                     // ???

        EmbosserWriterProperties props =
            new SimpleEmbosserProperties(cols, rows)
                .supports8dot(eightDots)
                .supportsDuplex(duplexEnabled)
                .supportsAligning(false);

        switch (type) {
            case BRF:
                return new ConfigurableEmbosser.Builder(os, table.newBrailleConverter())
                                    .breaks(new StandardLineBreaks(StandardLineBreaks.Type.DOS))
                                    .padNewline(Padding.BEFORE)
                                    .embosserProperties(props)
                                    .build();
            case BRA:
                return new ConfigurableEmbosser.Builder(os, table.newBrailleConverter())
                                    .breaks(new StandardLineBreaks(StandardLineBreaks.Type.UNIX))
                                    .padNewline(Padding.NO_FF)
                                    .embosserProperties(props)
                                    .build();
            case BRL:
                return new MicroBrailleFileFormatWriter(os);
            default:
                return null;
        }
    }

    public String getIdentifier() {
        return type.getClass().getCanonicalName() + "." + type.toString();
    }
    
    public String getFileExtension() {
        return "." + type.name().toLowerCase();
    }

    public void setFeature(String key, Object value) {

        if (EmbosserFeatures.TABLE.equals(key) && value!=null) {
            Table t;
            try {
                t = (Table)value;
            } catch (ClassCastException e) {
                t = tableCatalog.get(value.toString());
                if (t == null) {
                    throw new IllegalArgumentException("Unsupported value for table: '" + value + "'");
                }
            }
            table = t;
        }
    }

    public Object getFeature(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getDisplayName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getProperty(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
