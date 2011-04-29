package com_viewplus;

import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;

import java.io.IOException;

import org.daisy.braille.embosser.AbstractEmbosser;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.EmbosserTools;
import org.daisy.braille.embosser.FileToDeviceEmbosserWriter;
import org.daisy.braille.embosser.ConfigurableEmbosser;
import org.daisy.braille.embosser.SimpleEmbosserProperties;
import org.daisy.braille.embosser.StandardLineBreaks;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableFilter;
import org.daisy.braille.table.TableCatalog;
import org.daisy.paper.Area;
import org.daisy.paper.PageFormat;
import org.daisy.paper.Dimensions;
import org.daisy.paper.PrintPage;
import org.daisy.printing.Device;

import com_viewplus.ViewPlusEmbosserProvider.EmbosserType;

import org.daisy.braille.embosser.EmbosserFactoryException;
import org.daisy.braille.embosser.UnsupportedPaperException;

public class TigerEmbosser extends AbstractEmbosser {

    protected EmbosserType type;

    private double maxPaperWidth = Double.MAX_VALUE;
    private double maxPaperHeight = Double.MAX_VALUE;
    private double minPaperWidth = 50d;
    private double minPaperHeight = 50d;

    private final static TableFilter tableFilter;
    private final static String table6dot = "org.daisy.braille.table.DefaultTableProvider.TableType.EN_US";
    private final static String table8dot = ViewPlusTableProvider.class.getCanonicalName() + ".TableType.TIGER_INLINE_SUBSTITUTION_8DOT";

    static {
        tableFilter = new TableFilter() {
            //jvm1.6@Override
            public boolean accept(Table object) {
                return false;
            }
        };
    }

    private final static int maxLinesInHeight = 42;

    private int marginInner = 0;
    private int marginOuter = 0;
    private int marginTop = 0;
    private int marginBottom = 0;

    public TigerEmbosser(String name, String desc, EmbosserType identifier) {

        super(name, desc, identifier);

        type = identifier;

        setCellWidth(0.25*EmbosserTools.INCH_IN_MM);
        setCellHeight(0.4*EmbosserTools.INCH_IN_MM);

        minPaperWidth = 176d;  // B5
        minPaperHeight = 250d;

        switch (type) {
            case PREMIER_80:
            case PREMIER_100:
            case ELITE_150:
            case ELITE_200:
                maxPaperWidth = 12*EmbosserTools.INCH_IN_MM;
                maxPaperHeight = 22*EmbosserTools.INCH_IN_MM;
                break;
            case PRO_GEN_II:
                maxPaperWidth = 16*EmbosserTools.INCH_IN_MM;
                maxPaperHeight = 22*EmbosserTools.INCH_IN_MM;
                break;
            case CUB:
            case CUB_JR:
            case EMPRINT_SPOTDOT:
                maxPaperWidth = 8.5*EmbosserTools.INCH_IN_MM;
                maxPaperHeight = 14*EmbosserTools.INCH_IN_MM;
                break;
            case MAX:
                maxPaperWidth = 14*EmbosserTools.INCH_IN_MM;
                maxPaperHeight = 22*EmbosserTools.INCH_IN_MM;
                break;
            case EMFUSE:
                maxPaperWidth =  Math.max(297d, 11*EmbosserTools.INCH_IN_MM);   // A3, Tabloid
                maxPaperHeight = Math.max(420d, 17*EmbosserTools.INCH_IN_MM);
                break;
            default:
                throw new IllegalArgumentException("Unsupported embosser type");

        }
    }

    public boolean supportsDimensions(Dimensions dim) {

        if (dim==null) { return false; }

        return (dim.getWidth()  <= maxPaperWidth)  &&
               (dim.getWidth()  >= minPaperWidth)  &&
               (dim.getHeight() <= maxPaperHeight) &&
               (dim.getHeight() >= minPaperHeight);
    }
    public TableFilter getTableFilter() {
        return tableFilter;
    }

    public boolean supportsVolumes() {
        return false;
    }

    public boolean supportsAligning() {
        return true;
    }

    public boolean supports8dot() {

        switch (type) {
            default:
                return false;
        }
    }

    public boolean supportsDuplex() {

        switch (type) {
            case PREMIER_80:
            case PREMIER_100:
            case ELITE_150:
            case ELITE_200:
            case EMFUSE:
                return true;
            case PRO_GEN_II:
            case CUB:
            case CUB_JR:
            case MAX:
            case EMPRINT_SPOTDOT:
            default:
                return false;
        }
    }

    public EmbosserWriter newEmbosserWriter(Device device) {

        try {
            File f = File.createTempFile(this.getClass().getCanonicalName(), ".tmp");
            f.deleteOnExit();
            EmbosserWriter ew = newEmbosserWriter(new FileOutputStream(f));
            return new FileToDeviceEmbosserWriter(ew, f, device);
        } catch (IOException e) {
        }
        throw new IllegalArgumentException("Embosser does not support this feature.");
    }

    public EmbosserWriter newEmbosserWriter(OutputStream os) {

        boolean duplexEnabled = supportsDuplex() && false; // examine PEF file: duplex => Contract ?
        boolean eightDots = supports8dot() && false;       // examine PEF file: rowgap / char > 283F
        PageFormat page = getPageFormat();

        if (!supportsDimensions(page)) {
            throw new IllegalArgumentException(new UnsupportedPaperException("Unsupported paper"));
        }

        try {

            byte[] header = getHeader(duplexEnabled, eightDots);
            byte[] footer = new byte[0];

            Table table = TableCatalog.newInstance().get(eightDots?table8dot:table6dot);

            ConfigurableEmbosser.Builder b = new ConfigurableEmbosser.Builder(os, table.newBrailleConverter())
                .breaks(new StandardLineBreaks(StandardLineBreaks.Type.DOS))
                .padNewline(ConfigurableEmbosser.Padding.NONE)
                .footer(footer)
                .embosserProperties(
                    new SimpleEmbosserProperties(getMaxWidth(page), getMaxHeight(page))
                        .supportsDuplex(duplexEnabled)
                        .supportsAligning(supportsAligning())
                        .supports8dot(eightDots)
                )
                .header(header);
            return b.build();
            
        } catch (EmbosserFactoryException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private byte[] getHeader(boolean duplex,
                             boolean eightDots)
                      throws EmbosserFactoryException {

        // Legacy printing mode

        PageFormat page = getPageFormat();
        int linesPerPage = getMaxHeight(page);

        int formWidth = (int)Math.ceil(page.getWidth()/EmbosserTools.INCH_IN_MM*2);
        int formLength = (int)Math.ceil(page.getHeight()/EmbosserTools.INCH_IN_MM*2);
        int topOffset = (int)Math.floor(getPrintableArea(page).getOffsetY()/EmbosserTools.INCH_IN_MM*20);

        if (formWidth > 42)  { throw new UnsupportedPaperException("Form width cannot > 21 inch"); }
        if (formLength > 42) { throw new UnsupportedPaperException("Form lenght cannot > 21 inch"); }

        StringBuffer header = new StringBuffer();

        header.append((char)0x1b); header.append('@');                              // System reset
        header.append((char)0x1b); header.append("W@");                             // Word wrap = OFF
        header.append((char)0x1b); header.append('K');
                                   header.append((char)(40+topOffset));             // Top margin
        header.append((char)0x1b); header.append('L');
                                   header.append((char)(40+marginInner));           // Left margin
        header.append((char)0x1b); header.append('Q');
                                   header.append((char)(40+linesPerPage));          // Lines per page
        header.append((char)0x1b); header.append('S');            
                                   header.append((char)(40+formWidth));             // Form width
        header.append((char)0x1b); header.append('T');
                                   header.append((char)(40+formLength));            // Form length
        header.append((char)0x1b); header.append('F');
                                   header.append(eightDots?'B':'@');                // 6/8 dot
        if (supportsDuplex()) {
        header.append((char)0x1b); header.append('I');
                                   header.append(duplex?'A':'@');                   // Interpoint
        }
        header.append((char)0x1b); header.append("AA");                             // US Braille table
//      header.append((char)0x1b); header.append("M@");                             // Media type = Braille paper
//      header.append((char)0x1b); header.append("BA");                             // Dot height = NORMAL
//      header.append((char)0x1b); header.append("C@");                             // Auto perforation = OFF
//      header.append((char)0x1b); header.append("J@");                             // Standard Braille dot quality
//      header.append((char)0x1b); header.append("H@");                             // Standard Ink text quality

        return header.toString().getBytes();
    }

    @Override
    public Area getPrintableArea(PageFormat pageFormat) {

        PrintPage printPage = getPrintPage(pageFormat);

        double cellWidth = getCellWidth();
        double cellHeight = getCellHeight();
        double printablePageHeight = Math.min(printPage.getHeight(), maxLinesInHeight * getCellHeight());

        return new Area(printPage.getWidth() - (marginInner + marginOuter) * cellWidth,
                        printablePageHeight - (marginTop + marginBottom) * cellHeight,
                        marginInner * cellWidth,
                        marginTop * cellHeight);
    }
}
