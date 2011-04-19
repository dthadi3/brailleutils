package com_indexbraille;

import java.io.OutputStream;

import org.daisy.braille.embosser.EmbosserTools;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.EmbosserWriterProperties;
import org.daisy.braille.embosser.SimpleEmbosserProperties;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableCatalog;
import org.daisy.braille.table.TableFilter;

import com_indexbraille.IndexEmbosserProvider.EmbosserType;

public class BlueBarEmbosser extends IndexEmbosser {

    private final static TableFilter tableFilter;
    private final static String table6dot = IndexTableProvider.class.getCanonicalName() + ".TableType.INDEX_TRANSPARENT_6DOT";
    
    static {
        tableFilter = new TableFilter() {
            //jvm1.6@Override
            public boolean accept(Table object) {
                return false;
            }
        };
    }

    public BlueBarEmbosser(String name, String desc) {
        super(name, desc, EmbosserType.INDEX_BASIC_BLUE_BAR);
    }

    public TableFilter getTableFilter() {
        return tableFilter;
    }

    public EmbosserWriter newEmbosserWriter(OutputStream os) {

        if (!supportsDimensions(getPageFormat())) {
            throw new IllegalArgumentException("Unsupported paper for embosser " + getDisplayName());
        }

        Table table = TableCatalog.newInstance().get(table6dot);

        EmbosserWriterProperties props =
                new SimpleEmbosserProperties(EmbosserTools.getWidth(getPageFormat(), getCellWidth()),
                                             EmbosserTools.getHeight(getPageFormat(), getCellHeight()))
                    .supports8dot(false)
                    .supportsDuplex(false)
                    .supportsAligning(true);

        return new IndexTransparentEmbosserWriter(os,
                                                  table.newBrailleConverter(),
                                                  null,
                                                  null,
                                                  props);
    }
}
