package com_brailler;

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
}
