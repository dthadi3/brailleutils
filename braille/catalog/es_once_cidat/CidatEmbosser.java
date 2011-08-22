package es_once_cidat;

import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;

import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableCatalog;
import org.daisy.braille.embosser.AbstractEmbosser;
import org.daisy.braille.embosser.EmbosserTools;
import org.daisy.braille.embosser.EmbosserFeatures;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.FileToDeviceEmbosserWriter;
import org.daisy.paper.PrintPage;
import org.daisy.paper.Paper;
import org.daisy.paper.SheetPaper;
import org.daisy.paper.PageFormat;
import org.daisy.paper.SheetPaperFormat;
import org.daisy.paper.SheetPaperFormat.Orientation;
import org.daisy.printing.Device;

import es_once_cidat.CidatEmbosserProvider.EmbosserType;

/**
 *
 * @author Bert Frees
 */
public abstract class CidatEmbosser extends AbstractEmbosser {

    protected EmbosserType type;

    private double maxPageWidth = Double.MAX_VALUE;
    private double maxPageHeight = Double.MAX_VALUE;
    private double minPageWidth = 50d;
    private double minPageHeight = 50d;

    protected boolean duplexEnabled = true;
    protected boolean eightDotsEnabled = false;

    public CidatEmbosser(String name, String desc, EmbosserType identifier) {

        super(name, desc, identifier);

        type = identifier;

        setCellWidth(0.25*EmbosserTools.INCH_IN_MM);
        setCellHeight((eightDotsEnabled?0.5:0.4)*EmbosserTools.INCH_IN_MM);
        
        switch (type) {
            case IMPACTO_600:
            case IMPACTO_TEXTO:
                maxPageWidth = 42*getCellWidth();
                maxPageHeight = 13*EmbosserTools.INCH_IN_MM;
                minPageWidth = 12*getCellWidth();
                minPageHeight = 6*EmbosserTools.INCH_IN_MM;
                break;
            case PORTATHIEL_BLUE:
                maxPageWidth = 42*getCellWidth();
                maxPageHeight = 13*EmbosserTools.INCH_IN_MM;
                minPageWidth = 10*getCellWidth();
                minPageHeight = 8*EmbosserTools.INCH_IN_MM;
                break;
            default:
                throw new IllegalArgumentException("Unsupported embosser type");
        }
    }

    //jvm1.6@Override
    public boolean supportsPaper(Paper paper) {
        if (paper == null) { return false; }
        try {
            SheetPaper p = paper.asSheetPaper();
            if (supportsPageFormat(new SheetPaperFormat(p, Orientation.DEFAULT))) { return true; }
            if (supportsPageFormat(new SheetPaperFormat(p, Orientation.REVERSED))) { return true; }
        } catch (ClassCastException e) {
        }
        return false;
    }

    //jvw1.6@Override
    public boolean supportsPageFormat(PageFormat format) {
        if (format == null) { return false; }
        try {
            return supportsPrintPage(getPrintPage(format.asSheetPaperFormat()));
        } catch (ClassCastException e) {
            return false;
        }
    }

    public boolean supportsPrintPage(PrintPage dim) {
        if (dim==null) { return false; }
        return (dim.getWidth()  <= maxPageWidth)  &&
               (dim.getWidth()  >= minPageWidth)  &&
               (dim.getHeight() <= maxPageHeight) &&
               (dim.getHeight() >= minPageHeight);
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

    @Override
    public void setFeature(String key, Object value) {

        if (EmbosserFeatures.TABLE.equals(key)) {
            if (value == null) {
                throw new IllegalArgumentException("Unsupported value for table");
            }
            Table t;
            try {
                t = (Table)value;
            } catch (ClassCastException e) {
                t = TableCatalog.newInstance().get(value.toString());
            }
            if (getTableFilter().accept(t)) {
                setTable = t;
              //eightDotsEnabled = supports8dot() && setTable.newBrailleConverter().supportsEightDot();
              //setCellHeight((eightDotsEnabled?0.5:0.4)*EmbosserTools.INCH_IN_MM);
            } else {
                throw new IllegalArgumentException("Unsupported value for table.");
            }
        } else if (EmbosserFeatures.DUPLEX.equals(key) && supportsDuplex()) {
            try {
                duplexEnabled = (Boolean)value;
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Unsupported value for duplex.");
            }
        } else {
            super.setFeature(key, value);
        }
    }

    @Override
    public Object getFeature(String key) {

        if (EmbosserFeatures.TABLE.equals(key)) {
            return setTable;
        } else if (EmbosserFeatures.DUPLEX.equals(key) && supportsDuplex()) {
            return duplexEnabled;
        } else {
            return super.getFeature(key);
        }
    }
}
