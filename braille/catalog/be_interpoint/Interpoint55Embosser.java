package be_interpoint;

import java.io.File;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Properties;

import java.io.FileNotFoundException;
import java.io.IOException;

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
import org.daisy.paper.Area;
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
                return false;
            }
        };
    }
    private double maxPaperWidth = 340d;
    private double maxPaperHeight = Double.MAX_VALUE; // ???
    private double minPaperWidth = 50d;               // ???
    private double minPaperHeight = 50d;              // ???

    private int marginInner = 0;
    private int marginOuter = 0;
    private int marginTop = 0;
    private int marginBottom = 0;

    private boolean saddleStitchEnabled = false;
    private boolean duplexEnabled = true;
    private int maxPagesInQuire = 0;                  // 0 == no quires
    private int numberOfCopies = 1;

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

        boolean eightDots = supports8dot() && false;       // ???
        PageFormat page = getPageFormat();

        if (!supportsDimensions(page)) {
            throw new IllegalArgumentException("Unsupported paper");
        }

        Table table = TableCatalog.newInstance().get(table6dot);

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

    public void loadConfigurationFile(File file)
                               throws FileNotFoundException,
                                      IOException {

        Properties properties = new Properties();
        String property;

        InputStream is = new FileInputStream(file);
        properties.load(is);
        if (is != null) { is.close(); }

        if ((property = properties.getProperty("Mode")) != null) {
            if (property.equals("1")) {
                duplexEnabled = false;
                saddleStitchEnabled = false;
            } else if (property.equals("3")) {
                duplexEnabled = true;
                saddleStitchEnabled = false;
            } else if (property.equals("4")) {
                duplexEnabled = true;
                saddleStitchEnabled = true;
            }
        }
        if ((property = properties.getProperty("Copies")) != null) {
            try {
                setFeature(EmbosserFeatures.NUMBER_OF_COPIES, Integer.parseInt(property));
            } catch (Exception e) {
            }
        }
        if ((property = properties.getProperty("MaxPagesInQuire")) != null) {
            try {
                setFeature(EmbosserFeatures.PAGES_IN_QUIRE, Integer.parseInt(property));
            } catch (Exception e) {
            }
        }
    }

    public void saveConfigurationFile(File file)
                               throws FileNotFoundException,
                                      IOException {

        Properties properties = new Properties();

        InputStream is = new FileInputStream(file);
        properties.load(is);
        if (is != null) { is.close(); }

        PageFormat page = getPageFormat();

        if (!supportsDimensions(page)) {
            throw new IllegalArgumentException("Unsupported paper");
        }

        properties.setProperty("Mode",              saddleStitchEnabled?"4":duplexEnabled?"3":"1");
        properties.setProperty("MirrorMargins",     "1");
        properties.setProperty("CharactersPerLine", String.valueOf(getMaxWidth(page)));
        properties.setProperty("LinesPerPage",      String.valueOf(getMaxHeight(page)));
        properties.setProperty("LeftMargin",        String.valueOf(marginInner));
        properties.setProperty("RightMargin",       String.valueOf(marginOuter));
        properties.setProperty("TopMargin",         String.valueOf(marginTop));
        properties.setProperty("MaxPagesInQuire",   String.valueOf(maxPagesInQuire));
        properties.setProperty("TableName",         "USA1_6");
        properties.setProperty("Copies",            String.valueOf(numberOfCopies));

        OutputStream os = new FileOutputStream(file);
        properties.store(os, null);
        if (os != null) { os.close(); }
    }

    public EmbosserWriter newEmbosserWriter(Device device) {

        throw new IllegalArgumentException(new EmbosserFactoryException(getDisplayName() +
                " does not support printing directly to Device. " +
                "Write the output to a file with newEmbosserWriter(OutputStream), " +
                "save the settings to a file with saveConfigurationFile(File) " +
                "and then use the wprint55 software to emboss."));
    }

    public EmbosserWriter newX55EmbosserWriter(OutputStream os) {
        // write to ".x55" file format (special interpoint xml file)
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFeature(String key, Object value) {

        if (EmbosserFeatures.SADDLE_STITCH.equals(key)) {
            try {
                saddleStitchEnabled = (Boolean)value;
                duplexEnabled = duplexEnabled || saddleStitchEnabled;
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Unsupported value for saddle stitch.");
            }
        } else if (EmbosserFeatures.DUPLEX.equals(key)) {
            try {
                duplexEnabled = (Boolean)value;
                saddleStitchEnabled = saddleStitchEnabled && duplexEnabled;
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Unsupported value for duplex.");
            }
        } else if (EmbosserFeatures.NUMBER_OF_COPIES.equals(key)) {
            try {
                int copies = (Integer)value;
                if (copies < 1) {
                    throw new IllegalArgumentException("Unsupported value for number of copies.");
                }
                numberOfCopies = copies;
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Unsupported value for number of copies.");
            }
        } else if (EmbosserFeatures.PAGES_IN_QUIRE.equals(key)) {
            try {
                int pages = (Integer)value;
                if (pages < 0) {
                    throw new IllegalArgumentException("Unsupported value for maximum pages in quire.");
                }
                maxPagesInQuire = pages;
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Unsupported value for maximum pages in quire.");
            }
        } else {
            super.setFeature(key, value);
        }
    }

    @Override
    public Object getFeature(String key) {

        if (EmbosserFeatures.SADDLE_STITCH.equals(key)) {
            return saddleStitchEnabled;
        } else if (EmbosserFeatures.DUPLEX.equals(key)) {
            return duplexEnabled;
        } else if (EmbosserFeatures.NUMBER_OF_COPIES.equals(key)) {
            return numberOfCopies;
        } else if (EmbosserFeatures.PAGES_IN_QUIRE.equals(key)) {
            return maxPagesInQuire;
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

    @Override
    public Area getPrintableArea(PageFormat pageFormat) {

        PrintPage printPage = getPrintPage(pageFormat);

        double cellWidth = getCellWidth();
        double cellHeight = getCellHeight();

        return new Area(printPage.getWidth() - (marginInner + marginOuter) * cellWidth,
                        printPage.getHeight() - (marginTop + marginBottom) * cellHeight,
                        marginInner * cellWidth,
                        marginTop * cellHeight);
    }
}
