package be_interpoint;

import java.io.OutputStream;

import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableCatalog;
import org.daisy.braille.table.TableFilter;
import org.daisy.braille.embosser.AbstractEmbosser;
import org.daisy.braille.embosser.EmbosserFeatures;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.EmbosserWriterProperties;
import org.daisy.braille.embosser.SimpleEmbosserProperties;
import org.daisy.braille.embosser.ConfigurableEmbosser;
import org.daisy.braille.embosser.StandardLineBreaks;
import org.daisy.braille.embosser.AbstractEmbosserWriter.Padding;
import org.daisy.paper.Dimensions;
import org.daisy.paper.PageFormat;
import org.daisy.paper.PrintPage;
import org.daisy.paper.PrintPage.PrintDirection;
import org.daisy.paper.PrintPage.PrintMode;
import org.daisy.printing.Device;

import be_interpoint.InterpointEmbosserProvider.EmbosserType;

import org.daisy.braille.embosser.EmbosserFactoryException;

/**
 *
 * @author Bert Frees
 */
public class Interpoint55Embosser extends AbstractEmbosser {

    private final static TableFilter tableFilter;
    private final static String table6dot = "org.daisy.braille.table.DefaultTableProvider.TableType.EN_US";
    static {
        tableFilter = new TableFilter() {
            //jvm1.6@Override
            public boolean accept(Table object) {
                return object.getIdentifier().equals(table6dot);
            }
        };
    }
    private double maxPaperWidth = 340d;
    private double maxPaperHeight = Double.MAX_VALUE; // ???
    private double minPaperWidth = 50d;               // ???
    private double minPaperHeight = 50d;              // ???

    private boolean saddleStitchEnabled = false;

    public Interpoint55Embosser(String name, String desc) {

        super(name, desc, EmbosserType.INTERPOINT_55);

        setCellWidth(6d);
        setCellHeight(10d);
    }

    public TableFilter getTableFilter() {
        return tableFilter;
    }

    public boolean supportsDimensions(Dimensions dim) {

        if (dim==null) { return false; }

        return (dim.getWidth()  <= maxPaperWidth)  &&
               (dim.getWidth()  >= minPaperWidth)  &&
               (dim.getHeight() <= maxPaperHeight) &&
               (dim.getHeight() >= minPaperHeight);
    }

    public boolean supportsVolumes() {
        return false;
    }

    public boolean supports8dot() {
        return false;
    }

    public boolean supportsDuplex() {
        return true;
    }

    public boolean supportsAligning() {
        return true;
    }

    public EmbosserWriter newEmbosserWriter(OutputStream os) {

        boolean duplexEnabled = supportsDuplex();          // ??? examine PEF file => Contract?
        boolean eightDots = supports8dot() && false;       // ???
        PageFormat page = getPageFormat();

        if (!supportsDimensions(page)) {
            throw new IllegalArgumentException("Unsupported paper");
        }

        Table table = TableCatalog.newInstance().list(tableFilter).iterator().next();

        EmbosserWriterProperties props =
            new SimpleEmbosserProperties(getMaxWidth(page), getMaxHeight(page))
                .supports8dot(eightDots)
                .supportsDuplex(duplexEnabled)
                .supportsAligning(supportsAligning());

        return new ConfigurableEmbosser.Builder(os, table.newBrailleConverter())
                            .breaks(new StandardLineBreaks(StandardLineBreaks.Type.DOS))
                            .padNewline(Padding.NONE)
                            .embosserProperties(props)
                            .build();
    }

    public EmbosserWriter newEmbosserWriter(Device device) {

        throw new IllegalArgumentException(new EmbosserFactoryException(getDisplayName() + " does not support printing to Device. " +
                "Output should be written to a file and then opened with wprint55."));

    }

    public EmbosserWriter newX55EmbosserWriter(OutputStream os) {
        // write to ".x55" file format (special interpoint xml file)
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void writeConfigurationFile() {
        // write embosser settings to configuration (.ini) file that is used as input for wprint55
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFeature(String key, Object value) {

        if (EmbosserFeatures.SADDLE_STITCH.equals(key)) {
            try {
                saddleStitchEnabled = (Boolean)value;
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Unsupported value for saddle stitch.");
            }
        } else {
            super.setFeature(key, value);
        }
    }

    @Override
    public Object getFeature(String key) {

        if (EmbosserFeatures.SADDLE_STITCH.equals(key)) {
            return saddleStitchEnabled;
        } else {
            return super.getFeature(key);
        }
    }

    @Override
    public PrintPage getPrintPage(PageFormat pageFormat) {

        PrintDirection direction = PrintDirection.SIDEWAYS;
        PrintMode mode = saddleStitchEnabled?PrintMode.MAGAZINE:PrintMode.REGULAR;
        return new PrintPage(pageFormat, direction, mode);
    }
}
