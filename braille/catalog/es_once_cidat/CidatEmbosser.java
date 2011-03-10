package es_once_cidat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.daisy.braille.embosser.AbstractEmbosser;
import org.daisy.braille.embosser.EmbosserTools;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.FileToDeviceEmbosserWriter;
import org.daisy.paper.Dimensions;
import org.daisy.printing.Device;

import es_once_cidat.CidatEmbosserProvider.EmbosserType;

/**
 *
 * @author Bert Frees
 */
public abstract class CidatEmbosser extends AbstractEmbosser {

    protected EmbosserType type;

    private double maxPaperWidth = Double.MAX_VALUE;
    private double maxPaperHeight = Double.MAX_VALUE;
    private double minPaperWidth = 50d;
    private double minPaperHeight = 50d;

    public CidatEmbosser(String name, String desc, EmbosserType identifier) {

        super(name, desc, identifier);

        type = identifier;

        setCellWidth(0.25*EmbosserTools.INCH_IN_MM);
        setCellHeight(0.4*EmbosserTools.INCH_IN_MM);
        
        switch (type) {
            case IMPACTO_600:
            case IMPACTO_TEXTO:
                maxPaperWidth = 42*getCellWidth();
                maxPaperHeight = 13*EmbosserTools.INCH_IN_MM;
                minPaperWidth = 12*getCellWidth();
                minPaperHeight = 6*EmbosserTools.INCH_IN_MM;
                break;
            case PORTATHIEL_BLUE:
                maxPaperWidth = 42*getCellWidth();
                maxPaperHeight = 13*EmbosserTools.INCH_IN_MM;
                minPaperWidth = 10*getCellWidth();
                minPaperHeight = 8*EmbosserTools.INCH_IN_MM;
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
}
