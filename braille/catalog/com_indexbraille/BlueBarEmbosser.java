package com_indexbraille;

import java.io.OutputStream;

import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.EmbosserTools;
import org.daisy.braille.embosser.EmbosserWriterProperties;
import org.daisy.braille.embosser.SimpleEmbosserProperties;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableCatalog;
import org.daisy.braille.table.TableFilter;
import org.daisy.paper.PageFormat;
import org.daisy.paper.Dimensions;

import com_indexbraille.IndexEmbosserProvider.EmbosserType;

public class BlueBarEmbosser extends IndexEmbosser {

    private final static TableFilter tableFilter;
    private final static String table6dot = IndexTableProvider.class.getCanonicalName() + ".TableType.INDEX_TRANSPARENT_6DOT";
    
    static {
        tableFilter = new TableFilter() {
            //jvm1.6@Override
            public boolean accept(Table object) {
                if (object == null) { return false; }
                String tableID = object.getIdentifier();
                if (tableID.equals(table6dot)) { return true; }
                return false;
            }
        };
    }

    public BlueBarEmbosser(String name, String desc) {
        
        super(name, desc, EmbosserType.INDEX_BASIC_BLUE_BAR);
        setTable = TableCatalog.newInstance().get(table6dot);
    }

    public TableFilter getTableFilter() {
        return tableFilter;
    }

    @Override
    public boolean supportsDimensions(Dimensions dim) {

        if (type==EmbosserType.INDEX_BASIC_D_V2 ||
            type==EmbosserType.INDEX_BASIC_S_V2) {
            double w = dim.getWidth();
            double h = dim.getHeight();
            return super.supportsDimensions(dim) && (w==210 && (h==10*EmbosserTools.INCH_IN_MM ||
                                                                h==11*EmbosserTools.INCH_IN_MM ||
                                                                h==12*EmbosserTools.INCH_IN_MM)
                                                  || w==240 &&  h==12*EmbosserTools.INCH_IN_MM
                                                  || w==280 &&  h==12*EmbosserTools.INCH_IN_MM);
        } else {
            return super.supportsDimensions(dim);
        }
    }

    protected boolean supportsZFolding() {
        return false;
    }

    protected boolean supportsSaddleStitch() {
        return false;
    }

    public EmbosserWriter newEmbosserWriter(OutputStream os) {

        PageFormat page = getPageFormat();

        if (!supportsDimensions(page)) {
            throw new IllegalArgumentException("Unsupported paper for embosser " + getDisplayName());
        }

        EmbosserWriterProperties props =
                new SimpleEmbosserProperties(getMaxWidth(page), getMaxHeight(page))
                    .supports8dot(eightDotsEnabled)
                    .supportsDuplex(duplexEnabled)
                    .supportsAligning(supportsAligning());

        return new IndexTransparentEmbosserWriter(os,
                                                  setTable.newBrailleConverter(),
                                                  null,
                                                  null,
                                                  props);
    }
}
