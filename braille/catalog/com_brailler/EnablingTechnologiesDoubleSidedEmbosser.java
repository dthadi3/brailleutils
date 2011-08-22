package com_brailler;

import org.daisy.braille.embosser.EmbosserFeatures;
import org.daisy.paper.PageFormat;
import org.daisy.paper.PrintPage;

import com_brailler.EnablingTechnologiesEmbosserProvider.EmbosserType;

/**
 *
 * @author Bert Frees
 */
public class EnablingTechnologiesDoubleSidedEmbosser extends EnablingTechnologiesEmbosser {

    public EnablingTechnologiesDoubleSidedEmbosser(String name, String desc, EmbosserType identifier) {

        super(name, desc, identifier);

        switch (type) {
            case ET:
            case JULIET_PRO:
            case JULIET_PRO_60:
            case JULIET_CLASSIC:
            case BOOKMAKER:
            case BRAILLE_EXPRESS_100:
            case BRAILLE_EXPRESS_150:
            case BRAILLE_PLACE:
                break;
            default:
                throw new IllegalArgumentException("Unsupported embosser type");
        }

        duplexEnabled = true;
    }

    //jvm1.6@Override
    public boolean supportsDuplex() {
        return true;
    }

    @Override
    public Object getFeature(String key) {

        if (EmbosserFeatures.DUPLEX.equals(key)) {
            return duplexEnabled;
        } else {
            return super.getFeature(key);
        }
    }

    @Override
    public void setFeature(String key, Object value) {

        if (EmbosserFeatures.DUPLEX.equals(key)) {
            try {
                duplexEnabled = (Boolean)value;
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Unsupported value for duplex.");
            }
        } else {
            super.setFeature(key, value);
        }
    }

	public boolean supportsZFolding() {
		return false;
	}
	
	//jvm1.6Override
	public boolean supportsPrintMode(PrintMode mode) {
		return PrintMode.REGULAR == mode;
	}
	
	//jvm1.6Override	
	public PrintPage getPrintPage(PageFormat pageFormat) {
		return new PrintPage(pageFormat);
	}
}
