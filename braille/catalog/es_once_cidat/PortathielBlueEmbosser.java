package es_once_cidat;

import java.io.OutputStream;
import java.util.Collection;
import java.util.ArrayList;

import org.daisy.braille.embosser.EmbosserTools;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.ConfigurableEmbosser;
import org.daisy.braille.embosser.SimpleEmbosserProperties;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableFilter;
import org.daisy.braille.table.TableCatalog;
import org.daisy.paper.PageFormat;

import org.daisy.braille.embosser.EmbosserFactoryException;
import org.daisy.braille.embosser.UnsupportedPaperException;

import es_once_cidat.CidatEmbosserProvider.EmbosserType;

/**
 *
 * @author Bert Frees
 */
public class PortathielBlueEmbosser extends CidatEmbosser { // COMING SOON

    private final static TableFilter tableFilter;
    private final static String table6dot = CidatTableProvider.class.getCanonicalName() + ".TableType.IMPACTO_TRANSPARENT_6DOT";
    private final static String table8dot = CidatTableProvider.class.getCanonicalName() + ".TableType.IMPACTO_TRANSPARENT_8DOT";
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

    public PortathielBlueEmbosser(String name, String desc, EmbosserType identifier) {
        super(name, desc, identifier);
    }

    public TableFilter getTableFilter() {
        return tableFilter;
    }

    public EmbosserWriter newEmbosserWriter(OutputStream os) {

        if (!supportsDimensions(getPageFormat())) {
            throw new IllegalArgumentException("Unsupported paper");
        }

        boolean duplexEnabled = supportsDuplex() && false; // examine PEF file: duplex => Contract ?
        boolean eightDots = supports8dot() && false;

        PageFormat page = getPageFormat();
        int cellsInWidth = EmbosserTools.getWidth(page, getCellWidth());
        int linesInHeight = EmbosserTools.getHeight(page, getCellHeight());

        try {

            byte[] header = getPortathielHeader();
//            byte[] footer = new byte[]{0x1b,0x54};

            Table table = TableCatalog.newInstance().get(eightDots?table8dot:table6dot);

            ConfigurableEmbosser.Builder b = new ConfigurableEmbosser.Builder(os, table.newBrailleConverter())
//                .breaks(new CidatLineBreaks())
                .padNewline(ConfigurableEmbosser.Padding.NONE)
//                .footer(footer)
                .embosserProperties(
                    new SimpleEmbosserProperties(cellsInWidth, linesInHeight)
                        .supportsDuplex(duplexEnabled)
                        .supportsAligning(true)
                        .supports8dot(eightDots)
                )
                .header(header);
            return b.build();

        } catch (EmbosserFactoryException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private byte[] getPortathielHeader() throws EmbosserFactoryException {

        boolean eightDots = supports8dot() && false;     // examine PEF file: rowgap / char > 283F => Contract ?
        boolean duplex = supportsDuplex() && false;      // examine PEF file: duplex

        PageFormat page = getPageFormat();
        int pageLength = (int)Math.ceil(page.getHeight()/EmbosserTools.INCH_IN_MM);
        int charsPerLine = EmbosserTools.getWidth(page, getCellWidth());
        int linesPerPage = EmbosserTools.getHeight(page, getCellHeight());

        if (pageLength   < 8  || pageLength   > 13) { throw new UnsupportedPaperException("Paper height = " + pageLength + " inches, must be in [8,13]"); }
        if (charsPerLine < 12 || charsPerLine > 42) { throw new UnsupportedPaperException("Characters per line = " + charsPerLine + ", must be in [12,42]"); }
        if (linesPerPage < 10 || linesPerPage > 31) { throw new UnsupportedPaperException("Lines per page = " + linesPerPage + ", must be in [10,31]"); }

        StringBuffer header = new StringBuffer();

        header.append(  "\u001b!DT");  header.append(eightDots?'6':'8');                        // 6 or 8 dots
        header.append("\r\u001b!LM0");                                                          // Left margin
        header.append("\r\u001b!SL0");                                                          // Interline space
        header.append("\r\u001b!PL");  header.append(EmbosserTools.toBytes(pageLength, 2));     // Page length in inches
        header.append("\r\u001b!LP");  header.append(EmbosserTools.toBytes(linesPerPage, 2));   // Lines per page
        header.append("\r\u001b!CL");  header.append(EmbosserTools.toBytes(charsPerLine, 2));   // Characters per line
        header.append("\r\u001b!CT1");                                                          // Cut off words
        header.append("\r\u001b!NI1");                                                          // Not indent
        header.append("\r\u001b!TP");                                                           // Transparent mode
        header.append("\r\u001b!");
        header.append("\r\u001b!");
        header.append("\r\u001b!");
        header.append("\r\u001b!");
        header.append("\r\u001b!");
        header.append("\r\u001b!");
        header.append("\r\u001b!");
        header.append("\r\u001b!");
        header.append('\r');


//        header.append((char)0x1b); header.append(')');                          // Transparent mode
//        header.append((char)0x1b); header.append(eightDots?'+':'*');            // 6- or 8-dot matrix
//        header.append((char)0x1b); header.append('.');
//                                   header.append((char)(0x30 + pageLength));    // Page length in inches
//        header.append((char)0x1b); header.append("/1");                         // Line spacing in tenths of an inch
//        header.append((char)0x1b); header.append('0');
//                                   header.append((char)(0x30 + charsPerLine));  // Characters per line
//        header.append((char)0x1b); header.append('1');
//                                   header.append((char)(0x30 + linesPerPage));  // Lines per page
//        header.append((char)0x1b); header.append('3');                          // Cut off words
//        header.append((char)0x1b); header.append(duplex?'Q':'P');               // Front-side of double-sided embossing
//        header.append((char)0x1b); header.append("EP");
//                                   header.append(String.valueOf(pageCount));
//                                   header.append('\n');                         // Number of last page to emboss
//        header.append((char)0x1b); header.append("GU0\n");                      // Gutter (binding margin) = 0
//        header.append((char)0x1b); header.append("IN0\n");                      // Indent first line of paragraph = 0
//        header.append((char)0x1b); header.append("MB0\n");                      // Bottom margin in tenths of an inch = 0
//        header.append((char)0x1b); header.append("ML0\n");                      // Left margin in characters = 0
//        header.append((char)0x1b); header.append("MR0\n");                      // Right margin in characters = 0
//        header.append((char)0x1b); header.append("MT0\n");                      // Top margin in tenths of an inch = 0
//        header.append((char)0x1b); header.append("NC1\n");                      // Number of copies
//        header.append((char)0x1b); header.append("PM0\n");                      // Embossing mode
//        header.append((char)0x1b); header.append("PN0\n");                      // Number pages
//        header.append((char)0x1b); header.append("PI0\n");                      // Parameter influence
//        header.append((char)0x1b); header.append("SP1\n");                      // Number of first page to emboss

        return header.toString().getBytes();
    }
}
