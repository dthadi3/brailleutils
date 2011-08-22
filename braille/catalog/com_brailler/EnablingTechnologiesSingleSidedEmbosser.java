package com_brailler;

import org.daisy.paper.PageFormat;
import org.daisy.paper.PrintPage;

import com_brailler.EnablingTechnologiesEmbosserProvider.EmbosserType;

/**
 *
 * @author Bert Frees
 */
public class EnablingTechnologiesSingleSidedEmbosser extends EnablingTechnologiesEmbosser {

    public EnablingTechnologiesSingleSidedEmbosser(String name, String desc, EmbosserType identifier) {

        super(name, desc, identifier);

        switch (type) {
            case ROMEO_ATTACHE:
            case ROMEO_ATTACHE_PRO:
            case ROMEO_25:
            case ROMEO_PRO_50:
            case ROMEO_PRO_LE_NARROW:
            case ROMEO_PRO_LE_WIDE:
            case THOMAS:
            case THOMAS_PRO:
            case MARATHON:
                break;
            default:
                throw new IllegalArgumentException("Unsupported embosser type");
        }

        duplexEnabled = false;
    }

    public boolean supportsDuplex() {
        return false;
    }

	public boolean supportsZFolding() {
		return false;
	}

	//jvm1.6@Override
	public boolean supportsPrintMode(PrintMode mode) {
		return PrintMode.REGULAR == mode;
	}

	//jvm1.6Override	
	public PrintPage getPrintPage(PageFormat pageFormat) {
		return new PrintPage(pageFormat);
	}

}
