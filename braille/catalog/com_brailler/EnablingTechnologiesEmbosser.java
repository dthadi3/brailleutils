package com_brailler;

import java.util.Collection;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.File;
import java.io.OutputStream;

import java.io.IOException;

import org.daisy.braille.embosser.AbstractEmbosser;
import org.daisy.braille.embosser.EmbosserTools;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.FileToDeviceEmbosserWriter;
import org.daisy.braille.embosser.ConfigurableEmbosser;
import org.daisy.braille.embosser.SimpleEmbosserProperties;
import org.daisy.braille.embosser.StandardLineBreaks;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableFilter;
import org.daisy.braille.table.TableCatalog;
import org.daisy.paper.PageFormat;
import org.daisy.paper.Dimensions;
import org.daisy.printing.Device;

import com_brailler.EnablingTechnologiesEmbosserProvider.EmbosserType;

/**
 *
 * @author Bert Frees
 */
public abstract class EnablingTechnologiesEmbosser extends AbstractEmbosser {

    protected EmbosserType type;

    private double maxPaperWidth = Double.MAX_VALUE;
    private double maxPaperHeight = Double.MAX_VALUE;
    private double minPaperWidth = 50d;
    private double minPaperHeight = 50d;

    private final static TableFilter tableFilter;
    private final static String table6dot = "org.daisy.braille.table.DefaultTableProvider.TableType.EN_US";
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

    public EnablingTechnologiesEmbosser(String name, String desc, EmbosserType identifier) {

        super(name, desc, identifier);

        type = identifier;

        setCellWidth(0.24*EmbosserTools.INCH_IN_MM);
        setCellHeight(0.4*EmbosserTools.INCH_IN_MM); // 8 dot: 0.6 inch

        minPaperWidth = 1.5*EmbosserTools.INCH_IN_MM;
        minPaperHeight = 3*EmbosserTools.INCH_IN_MM;
        maxPaperHeight = 14*EmbosserTools.INCH_IN_MM;

        switch (type) {
            case ROMEO_ATTACHE:
            case ROMEO_ATTACHE_PRO:
                maxPaperWidth = 8.5*EmbosserTools.INCH_IN_MM;
                break;
            case ROMEO_PRO_LE_NARROW:
                maxPaperWidth = 8.5*EmbosserTools.INCH_IN_MM;
                minPaperHeight = 0.5*EmbosserTools.INCH_IN_MM;
                maxPaperHeight = 4*EmbosserTools.INCH_IN_MM;
                break;
            case ROMEO_25:
            case ROMEO_PRO_50:
            case THOMAS:
            case THOMAS_PRO:
            case MARATHON:
            case ET:
            case JULIET_PRO_60:
            case BOOKMAKER:
            case BRAILLE_EXPRESS_100:
            case BRAILLE_EXPRESS_150:
                maxPaperWidth = 13.25*EmbosserTools.INCH_IN_MM;
                break;
            case ROMEO_PRO_LE_WIDE:
                maxPaperWidth = 13.25*EmbosserTools.INCH_IN_MM;
                minPaperHeight = 0.5*EmbosserTools.INCH_IN_MM;
                maxPaperHeight = 4*EmbosserTools.INCH_IN_MM;
                break;
            case JULIET_PRO:
            case JULIET_CLASSIC:
                maxPaperWidth = 15*EmbosserTools.INCH_IN_MM;
                break;
            case BRAILLE_PLACE:
                minPaperWidth = 11.5*EmbosserTools.INCH_IN_MM;
                maxPaperWidth = 11.5*EmbosserTools.INCH_IN_MM;
                minPaperHeight = 11*EmbosserTools.INCH_IN_MM;
                maxPaperHeight = 11*EmbosserTools.INCH_IN_MM;
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

    public boolean supports8dot() {
        return false;
    }

    public boolean supportsDuplex() {
        return false;
    }

    public boolean supportsAligning() {
        return false;
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

        if (!supportsDimensions(getPageFormat())) {
            throw new IllegalArgumentException("Unsupported paper");
        }

        boolean duplexEnabled = supportsDuplex() && false; // examine PEF file: duplex => Contract ?
        boolean eightDots = supports8dot() && false;       // examine PEF file: rowgap / char > 283F
        int cellsPerLine = 40;                             // examine PEF file: cols
        int linesPerPage = 25;                             // examine PEF file: rows

        byte[] header = getHeader();
        byte[] footer = new byte[0];

        Table table = TableCatalog.newInstance().get(table6dot);

        ConfigurableEmbosser.Builder b = new ConfigurableEmbosser.Builder(os, table.newBrailleConverter())
            .breaks(new StandardLineBreaks(StandardLineBreaks.Type.DOS))
            .padNewline(ConfigurableEmbosser.Padding.NONE)
            .footer(footer)
            .embosserProperties(
                new SimpleEmbosserProperties(cellsPerLine, linesPerPage)
                    .supportsDuplex(duplexEnabled)
                    .supportsAligning(supportsAligning())
                    .supports8dot(eightDots)
            )
            .header(header);
        return b.build();
    }

    private byte[] getHeader() {

        boolean eightDots = supports8dot() && false;     // examine PEF file: rowgap / char > 283F => Contract ?
        boolean duplex = supportsDuplex() && false;      // examine PEF file: duplex
        int cellsPerLine = 40;                           // examine PEF file: cols
        int linesPerPage = 25;                           // examine PEF file: rows
        PageFormat page = getPageFormat();
        double paperLenght = page.getHeight();
        int marginInner = 0;
        int marginTop = 0;

        StringBuffer header = new StringBuffer();

        header.append(new char[]{0x1b,0x00});                                                   // Reset system
        header.append(new char[]{0x1b,0x01}); header.append(new char[]{0x00,0x00});             // Alpha character set
        header.append(new char[]{0x1b,0x0b}); header.append((char)(eightDots?1:0));             // 6/8 dot braille mode
        header.append(new char[]{0x1b,0x0c}); header.append((char)(1+marginInner));             // Left margin
        header.append(new char[]{0x1b,0x0f}); header.append((char)1);                           // Paper sensor = ON
        header.append(new char[]{0x1b,0x11}); header.append((char)linesPerPage);                // Lines per page
        header.append(new char[]{0x1b,0x12}); header.append((char)(cellsPerLine+marginInner));  // Right margin
        header.append(new char[]{0x1b,0x14});
            int pageLenght = (int)Math.ceil(paperLenght/EmbosserTools.INCH_IN_MM-3);
                                              header.append((char)pageLenght);                  // Page length
        header.append(new char[]{0x1b,0x17}); header.append((char)0);                           // Word wrap = OFF
        header.append(new char[]{0x1b,0x23}); header.append((char)0);                           // Horizontal page centering = OFF
        header.append(new char[]{0x1b,0x29}); header.append((char)(duplex?0:1));                // Interpoint mode
        header.append(new char[]{0x1b,0x33}); header.append((char)(eightDots?3:0));             // DBS mode
        header.append(new char[]{0x1b,0x34});
            int topOffFormOffset = (int)Math.ceil(marginTop*getCellHeight()/EmbosserTools.INCH_IN_MM*10);
                                              header.append((char)topOffFormOffset);            // Top of form offset

        return header.toString().getBytes();
    }
}
