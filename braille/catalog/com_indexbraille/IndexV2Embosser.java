package com_indexbraille;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.daisy.braille.embosser.EmbosserTools;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.EmbosserWriterProperties;
import org.daisy.braille.embosser.SimpleEmbosserProperties;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableCatalog;
import org.daisy.braille.table.TableFilter;
import org.daisy.paper.PageFormat;
import org.daisy.paper.Dimensions;

import com_indexbraille.IndexEmbosserProvider.EmbosserType;

import org.daisy.braille.embosser.UnsupportedPaperException;

public class IndexV2Embosser extends IndexEmbosser {

    private final static TableFilter tableFilter;
    private final static String table6dot = IndexEmbosserProvider.class.getCanonicalName() + ".TableType.INDEX_TRANSPARENT_6DOT";
    private final static String table8dot = IndexEmbosserProvider.class.getCanonicalName() + ".TableType.INDEX_TRANSPARENT_8DOT";
    private final static Collection<String> supportedTableIds = new ArrayList<String>();

    static {
        supportedTableIds.add(table6dot);
        //supportedTableIds.add(table8dot);
        tableFilter = new TableFilter() {
            @Override
            public boolean accept(Table object) {
                return supportedTableIds.contains(object.getIdentifier());
            }
        };
    }

    public IndexV2Embosser(String name, String desc, EmbosserType identifier) {
        super(name, desc, identifier);
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
                                                  || w==240 &&  h==12*EmbosserTools.INCH_IN_MM);
        } else {
            return super.supportsDimensions(dim);
        }
    }

    public EmbosserWriter newEmbosserWriter(OutputStream os) {

        boolean duplexEnabled = supportsDuplex() && false; // ??? examine PEF file: duplex => Contract ?
        int pageCount = 1;                                 // ???
        boolean eightDots = supports8dot() && false;       // ???
        boolean magazine = false;
        int numberOfCopies = getNumberOfCopies();

        if (!supportsDimensions(getPageFormat())) {
            throw new IllegalArgumentException(new UnsupportedPaperException("Unsupported paper"));
        }
        if (numberOfCopies > 999 || numberOfCopies < 1) {
            throw new IllegalArgumentException(new UnsupportedPaperException("Invalid number of copies: " + numberOfCopies + " is not in [1, 999]"));
        }
        if (magazine && pageCount > 200) {
            throw new IllegalArgumentException(new UnsupportedPaperException("Number of pages = " + pageCount +  "; cannot exceed 200 when in magazine style mode"));
        }

        PageFormat page = getPageFormat();
        int cellsInWidth = EmbosserTools.getWidth(page, getCellWidth());
        int linesInHeight = EmbosserTools.getHeight(page, getCellHeight()); // depends on cell heigth -> depends on rowgap

        byte[] header = getIndexV2Header();
        byte[] footer = new byte[]{0x1a};

        Table table = TableCatalog.newInstance().get(eightDots?table8dot:table6dot);

        EmbosserWriterProperties props =
            new SimpleEmbosserProperties(cellsInWidth, linesInHeight)
                .supports8dot(eightDots)
                .supportsDuplex(duplexEnabled)
                .supportsAligning(supportsAligning());

        return new IndexTransparentEmbosserWriter(os,
                                                  table.newBrailleConverter(),
                                                  header,
                                                  footer,
                                                  props);
    }

    private byte[] getIndexV2Header() {

        boolean eightDots = supports8dot() && false;     // examine PEF file: rowgap / char > 283F => Contract ?
        boolean duplex = supportsDuplex() && false;      // examine PEF file: duplex
        boolean saddleStitch = false;
        boolean zFolding = false;
        int cellsInWidth = EmbosserTools.getWidth(getPageFormat(), getCellWidth());
        int numberOfCopies = getNumberOfCopies();

        StringBuffer header = new StringBuffer();

        header.append((char)0x1b);
        header.append((char)0x0f);
        header.append((char)0x02);
        header.append("x,");                            // 0: Activated braille code
        header.append("0,");                            // 1: Type of braille code      = Computer
        header.append("1,");                            // 2: 6/8 dot braille           = 8 dot
        header.append("x,");                            // 3: Capital prefix
        header.append("x,");                            // 4: Baud rate
        header.append("x,");                            // 5: Number of data bits
        header.append("x,");                            // 6: Parity
        header.append("x,");                            // 7: Number of stop bits
        header.append("x,");                            // 8: Handshake
        byte[] w = EmbosserTools.toBytes(cellsInWidth-23, 2);
        header.append((char)w[0]);
        header.append((char)w[1]);                      // 9: Characters per line
        header.append(",");
        header.append("0,");                            // 10: Left margin              = 0 characters
        header.append("0,");                            // 11: Binding margin           = 0 characters
        header.append("0,");                            // 12: Top margin               = 0 lines
        header.append("0,");                            // 13: Bottom margin            = 0 lines
        header.append(eightDots?'4':'0');               // 14: Line spacing             = 2.5 mm (6 dot) or 5 mm (8 dot)
        header.append(",");
        if (saddleStitch) { header.append('4'); } else
        if (zFolding)     { header.append('3'); } else
        if (duplex)       { header.append('2'); } else
                          { header.append('1'); }       // 15: Page mode                = 1,2 or 4 pages per sheet or z-folding (3)
        header.append(",");
        header.append("0,");                            // 16: Print mode               = normal
        header.append("0,");                            // 17: Page number              = off
        header.append("x,");                            // 18: N/A
        header.append("0,");                            // 19: Word wrap                = off
        header.append("1,");                            // 20: Auto line feed           = on
        header.append("x,");                            // 21: Form feed
        header.append("x,");                            // 22: Volume
        header.append("x,");                            // 23: Impact level
        header.append("x,");                            // 24: Delay
        header.append("x,");                            // 25: Print quality
        header.append("x,");                            // 26: Graphic dot distance
        header.append("0,");                            // 27: Text dot distance        = normal (2.5 mm)
        header.append("x,");                            // 28: Setup
        header.append("x,x,x,x,x,x,x,x,x,x,x");         // 29-39: N/A
        header.append((char)0x1b);
        header.append((char)0x0f);

        if (numberOfCopies > 1) {
            header.append((char)0x1b);
            header.append((char)0x12);
            byte[] c = EmbosserTools.toBytes(numberOfCopies, 3);
            header.append((char)c[0]);
            header.append((char)c[1]);
            header.append((char)c[2]);
            header.append((char)0x1b);
            header.append((char)0x12);
        }

        return header.toString().getBytes();
    }
}