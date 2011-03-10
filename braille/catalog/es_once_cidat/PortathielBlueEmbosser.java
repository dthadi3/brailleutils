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
public class PortathielBlueEmbosser extends CidatEmbosser {

    private final static TableFilter tableFilter;
    private final static String table6dot = CidatTableProvider.class.getCanonicalName() + ".TableType.PORTATHIEL_TRANSPARENT_6DOT";
    private final static String table8dot = CidatTableProvider.class.getCanonicalName() + ".TableType.PORTATHIEL_TRANSPARENT_8DOT";
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

            Table table = TableCatalog.newInstance().get(eightDots?table8dot:table6dot);

            ConfigurableEmbosser.Builder b = new ConfigurableEmbosser.Builder(os, table.newBrailleConverter())
                .breaks(new CidatLineBreaks(CidatLineBreaks.Type.PORTATHIEL_TRANSPARENT))
                .padNewline(ConfigurableEmbosser.Padding.NONE)
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

        header.append(  "\u001b!TP");                                                           // Transparent mode ON
        header.append("\r\u001b!DT");  header.append(eightDots?'6':'8');                        // 6 or 8 dots
        header.append("\r\u001b!DS");  header.append(duplex?'1':'0');                           // Front-side or double-sided embossing
        header.append("\r\u001b!LM0");                                                          // Left margin
        header.append("\r\u001b!SL1");                                                          // Interline space = 1/10 inch
        header.append("\r\u001b!PL");  header.append(EmbosserTools.toBytes(pageLength, 2));     // Page length in inches
        header.append("\r\u001b!LP");  header.append(EmbosserTools.toBytes(linesPerPage, 2));   // Lines per page
        header.append("\r\u001b!CL");  header.append(EmbosserTools.toBytes(charsPerLine, 2));   // Characters per line
        header.append("\r\u001b!CT1");                                                          // Cut off words
        header.append("\r\u001b!NI1");                                                          // No indent
        header.append("\r\u001b!JB0");                                                          // Jumbo mode OFF
        header.append('\r');

        return header.toString().getBytes();
    }
}
