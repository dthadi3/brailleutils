package com_indexbraille;

import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;

import org.daisy.braille.embosser.AbstractEmbosser;
import org.daisy.braille.embosser.EmbosserTools;
import org.daisy.braille.embosser.EmbosserFeatures;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.FileToDeviceEmbosserWriter;
import org.daisy.paper.Area;
import org.daisy.paper.PrintPage.PrintDirection;
import org.daisy.paper.PrintPage.PrintMode;
import org.daisy.paper.PageFormat;
import org.daisy.paper.Dimensions;
import org.daisy.paper.PrintPage;
import org.daisy.printing.Device;

import com_indexbraille.IndexEmbosserProvider.EmbosserType;

public abstract class IndexEmbosser extends AbstractEmbosser {

    protected EmbosserType type;

    private double maxPaperWidth = Double.MAX_VALUE;
    private double maxPaperHeight = Double.MAX_VALUE;
    private double minPaperWidth = 50d;
    private double minPaperHeight = 50d;

    protected int numberOfCopies = 1;
    protected boolean zFoldingEnabled = false;
    protected boolean saddleStitchEnabled = false;
    protected boolean duplexEnabled = false;

    protected int maxNumberOfCopies = 1;

    protected double printablePageWidth;
    protected double printablePageHeight;

    protected int marginInner = 0;
    protected int marginOuter = 0;
    protected int marginTop = 0;
    protected int marginBottom = 0;

    protected int minMarginInner = 0;
    protected int minMarginOuter = 0;
    protected int minMarginTop = 0;
    protected int minMarginBottom = 0;

    protected int maxMarginInner = 0;
    protected int maxMarginOuter = 0;
    protected int maxMarginTop = 0;
    protected int maxMarginBottom = 0;
    
    protected int minCellsInWidth = 0;
    protected int minLinesInHeight = 0;
    protected int maxCellsInWidth = Integer.MAX_VALUE;
    protected int maxLinesInHeight = Integer.MAX_VALUE;
  
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
                minPaperWidth = 100d;
                minPaperHeight = Math.max(110d, saddleStitchEnabled?276d:138d); // = 23*6(*2)
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
        return true;
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

    protected abstract boolean supportsZFolding();
    protected abstract boolean supportsSaddleStitch();

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

        if (EmbosserFeatures.NUMBER_OF_COPIES.equals(key) && maxNumberOfCopies > 1) {
            try {
                int copies = (Integer)value;
                if (copies < 1 || copies > maxNumberOfCopies) {
                    throw new IllegalArgumentException("Unsupported value for number of copies.");
                }
                numberOfCopies = copies;
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Unsupported value for number of copies.");
            }
        } else if (EmbosserFeatures.SADDLE_STITCH.equals(key) && supportsSaddleStitch()) {
            try {
                saddleStitchEnabled = (Boolean)value;
                if (type == EmbosserType.INDEX_4X4_PRO_V2) {
                    minPaperHeight = Math.max(110d, saddleStitchEnabled?276d:138d);
                }
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Unsupported value for saddle stitch.");
            }
        } else if (EmbosserFeatures.Z_FOLDING.equals(key) && supportsZFolding()) {
            try {
                zFoldingEnabled = (Boolean)value;
                if (type==EmbosserType.INDEX_BASIC_D_V2) {
                    duplexEnabled = duplexEnabled || zFoldingEnabled;
                }
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Unsupported value for z-folding.");
            }
        } else if (EmbosserFeatures.DUPLEX.equals(key) && supportsDuplex()) {
            try {
                duplexEnabled = (Boolean)value;
                if (type==EmbosserType.INDEX_BASIC_D_V2) {
                    zFoldingEnabled = zFoldingEnabled && duplexEnabled;
                }
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Unsupported value for duplex.");
            }
        } else {
            super.setFeature(key, value);
        }
    }

    @Override
    public Object getFeature(String key) {

        if (EmbosserFeatures.NUMBER_OF_COPIES.equals(key) && maxNumberOfCopies > 1) {
            return numberOfCopies;
        } else if (EmbosserFeatures.SADDLE_STITCH.equals(key) && supportsSaddleStitch()) {
            return saddleStitchEnabled;
        } else if (EmbosserFeatures.Z_FOLDING.equals(key) && supportsZFolding()) {
            return zFoldingEnabled;
        } else if (EmbosserFeatures.DUPLEX.equals(key) && supportsDuplex()) {
            return duplexEnabled;
        } else {
            return super.getFeature(key);
        }
    }

    @Override
    public PrintPage getPrintPage(PageFormat pageFormat) {

        PrintMode mode = saddleStitchEnabled?PrintMode.MAGAZINE:PrintMode.REGULAR;
        PrintDirection direction;

        switch (type) {
            case INDEX_4X4_PRO_V2:
            case INDEX_4X4_PRO_V3:
                direction = PrintDirection.SIDEWAYS;
                break;
            default:
                direction = PrintDirection.UPRIGHT;
        }

        return new PrintPage(pageFormat, direction, mode);
    }

    @Override
    public Area getPrintableArea(PageFormat pageFormat) {

        PrintPage printPage = getPrintPage(pageFormat);

        double cellWidth = getCellWidth();
        double cellHeight = getCellHeight();
        double inputPageWidth = pageFormat.getWidth();

        printablePageWidth = printPage.getWidth();
        printablePageHeight = printPage.getHeight();

        switch (type) {
            case INDEX_4X4_PRO_V2:
                printablePageHeight = Math.min(inputPageWidth, 248.5);
                break;
            case INDEX_EVEREST_D_V2:
            case INDEX_EVEREST_D_V3:
            case INDEX_BASIC_S_V2:
            case INDEX_BASIC_D_V2:
            case INDEX_BASIC_S_V3:
            case INDEX_BASIC_D_V3:
            case INDEX_4WAVES_PRO_V3:
                printablePageWidth = Math.min(inputPageWidth, 248.5);
                break;
        }

        printablePageWidth  = Math.min(printablePageWidth,  maxCellsInWidth  * cellWidth);
        printablePageHeight = Math.min(printablePageHeight, maxLinesInHeight * cellHeight);

        double unprintableInner = 0;
        double unprintableTop = 0;

        switch (type) {
            case INDEX_BASIC_D_V3:
            case INDEX_BASIC_S_V3:
                unprintableInner = Math.max(0, inputPageWidth - 276.4);
                break;
            case INDEX_EVEREST_D_V3:
                unprintableInner = Math.max(0, inputPageWidth - 272.75);
                break;
        }

        marginInner =  Math.min(maxMarginInner,  Math.max(minMarginInner,  marginInner));
        marginOuter =  Math.min(maxMarginOuter,  Math.max(minMarginOuter,  marginOuter));
        marginTop =    Math.min(maxMarginTop,    Math.max(minMarginTop,    marginTop));
        marginBottom = Math.min(maxMarginBottom, Math.max(minMarginBottom, marginBottom));

        return new Area(printablePageWidth - (marginInner + marginOuter) * cellWidth,
                        printablePageHeight - (marginTop + marginBottom) * cellHeight,
                        unprintableInner + marginInner * cellWidth,
                        unprintableTop + marginTop * cellHeight);
    }
}
