package com_indexbraille;

import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;

import org.daisy.braille.embosser.AbstractEmbosser;
import org.daisy.braille.embosser.EmbosserTools;
import org.daisy.braille.embosser.EmbosserFeatures;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.FileToDeviceEmbosserWriter;
import org.daisy.paper.Dimensions;
import org.daisy.printing.Device;

import com_indexbraille.IndexEmbosserProvider.EmbosserType;

public abstract class IndexEmbosser extends AbstractEmbosser {

    protected EmbosserType type;

    private double maxPaperWidth = Double.MAX_VALUE;
    private double maxPaperHeight = Double.MAX_VALUE;
    private double minPaperWidth = 50d;
    private double minPaperHeight = 50d;
  
    public IndexEmbosser(String name, String desc, EmbosserType identifier) {

        super(name, desc, identifier);

        type = identifier;

        setCellWidth(6d);
        setCellHeight(10d);

        switch (type) {
            case INDEX_BASIC_BLUE_BAR:
                maxPaperWidth = 280d;
                maxPaperHeight = 12*EmbosserTools.INCH_IN_MM;
                //minPaperWidth = ?
                //minPaperHeight = ?
                break;
            case INDEX_BASIC_S_V2:
            case INDEX_BASIC_D_V2:
                minPaperWidth = 138d; // = 23*6
                minPaperHeight = 1*EmbosserTools.INCH_IN_MM;
                //maxPaperWidth = ?
                maxPaperHeight = (20+2/3)*EmbosserTools.INCH_IN_MM;
                break;
            case INDEX_EVEREST_D_V2:
                minPaperWidth = 138d; // = 23*6
                minPaperHeight = 100d;
                //maxPaperWidth = ?
                maxPaperHeight = 350d;
                break;
            case INDEX_4X4_PRO_V2:
                boolean magazine = false;
                minPaperWidth = 100d;
                minPaperHeight = Math.max(110d, magazine?276d:138d); // = 23*6(*2)
                maxPaperWidth = 297d;
                maxPaperHeight = 500d;
                break;
            case INDEX_BASIC_S_V3:
            case INDEX_BASIC_D_V3:
                minPaperWidth = 90d;
                minPaperHeight = 1*EmbosserTools.INCH_IN_MM;
                maxPaperWidth = 295d;
                maxPaperHeight = 17*EmbosserTools.INCH_IN_MM;
                break;
            case INDEX_EVEREST_D_V3:
            case INDEX_4X4_PRO_V3:
                minPaperWidth = 130d;
                minPaperHeight = 120d;
                maxPaperWidth = 297d;
                maxPaperHeight = 585d;
                break;
            case INDEX_4WAVES_PRO_V3:
                minPaperWidth = 90d;
                minPaperHeight = 11*EmbosserTools.INCH_IN_MM;
                maxPaperWidth = 295d;
                maxPaperHeight = 12*EmbosserTools.INCH_IN_MM;
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

    public boolean supportsAligning() {

        switch (type) {
            case INDEX_BASIC_BLUE_BAR:
            case INDEX_BASIC_S_V2:
            case INDEX_BASIC_D_V2:
            case INDEX_EVEREST_D_V2:
            case INDEX_4X4_PRO_V2:
                return true;
            case INDEX_BASIC_S_V3:
            case INDEX_BASIC_D_V3:
            case INDEX_EVEREST_D_V3:
            case INDEX_4X4_PRO_V3:
            case INDEX_4WAVES_PRO_V3:
            default:
                return false;
        }
    }

    public boolean supports8dot() {
        return false;
    }

    public boolean supportsDuplex() {

        switch (type) {
            case INDEX_BASIC_D_V2:
            case INDEX_EVEREST_D_V2:
            case INDEX_4X4_PRO_V2:
            case INDEX_BASIC_D_V3:
            case INDEX_EVEREST_D_V3:
            case INDEX_4X4_PRO_V3:
            case INDEX_4WAVES_PRO_V3:
                return true;
            case INDEX_BASIC_BLUE_BAR:
            case INDEX_BASIC_S_V2:
            case INDEX_BASIC_S_V3:
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

    protected int getNumberOfCopies() {

        int numberOfCopies = 1;
        Object value = getFeature(EmbosserFeatures.NUMBER_OF_COPIES);
        if (value != null) {
            try {
                numberOfCopies = (Integer)value;
            } catch (ClassCastException e) {
            }
        }
        return numberOfCopies;
    }
}
