package com_brailler;

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
    }

    @Override
    public boolean supportsDuplex() {
        return true;
    }
}
