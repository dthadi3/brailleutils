package com_brailler;

import java.util.Collection;
import java.util.HashMap;

import org.daisy.braille.embosser.Embosser;
import org.daisy.braille.embosser.EmbosserProvider;

/**
 *
 * @author Bert Frees
 */
public class EnablingTechnologiesEmbosserProvider implements EmbosserProvider {

    public static enum EmbosserType {
        ROMEO_ATTACHE,
        ROMEO_ATTACHE_PRO,
        ROMEO_25,
        ROMEO_PRO_50,
        ROMEO_PRO_LE_NARROW,
        ROMEO_PRO_LE_WIDE,
        THOMAS,
        THOMAS_PRO,
        MARATHON,
        ET,
        JULIET_PRO,
        JULIET_PRO_60,
        JULIET_CLASSIC,
        BOOKMAKER,
        BRAILLE_EXPRESS_100,
        BRAILLE_EXPRESS_150,
        BRAILLE_PLACE
    };

    private final HashMap<EmbosserType, Embosser> embossers;

    public EnablingTechnologiesEmbosserProvider() {
        embossers = new HashMap<EmbosserType, Embosser>();

        // Single sided
        embossers.put(EmbosserType.ROMEO_ATTACHE,       new EnablingTechnologiesSingleSidedEmbosser("Romeo Attache",       "", EmbosserType.ROMEO_ATTACHE));
        embossers.put(EmbosserType.ROMEO_ATTACHE_PRO,   new EnablingTechnologiesSingleSidedEmbosser("Romeo Attache Pro",   "", EmbosserType.ROMEO_ATTACHE_PRO));
        embossers.put(EmbosserType.ROMEO_25,            new EnablingTechnologiesSingleSidedEmbosser("Romeo 25",            "", EmbosserType.ROMEO_25));
        embossers.put(EmbosserType.ROMEO_PRO_50,        new EnablingTechnologiesSingleSidedEmbosser("Romeo Pro 50",        "", EmbosserType.ROMEO_PRO_50));
        embossers.put(EmbosserType.ROMEO_PRO_LE_NARROW, new EnablingTechnologiesSingleSidedEmbosser("Romeo Pro LE Narrow", "", EmbosserType.ROMEO_PRO_LE_NARROW));
        embossers.put(EmbosserType.ROMEO_PRO_LE_WIDE,   new EnablingTechnologiesSingleSidedEmbosser("Romeo Pro LE Wide",   "", EmbosserType.ROMEO_PRO_LE_WIDE));
        embossers.put(EmbosserType.THOMAS,              new EnablingTechnologiesSingleSidedEmbosser("Thomas",              "", EmbosserType.THOMAS));
        embossers.put(EmbosserType.THOMAS_PRO,          new EnablingTechnologiesSingleSidedEmbosser("Thomas Pro",          "", EmbosserType.THOMAS_PRO));
        embossers.put(EmbosserType.MARATHON,            new EnablingTechnologiesSingleSidedEmbosser("Marathon",            "", EmbosserType.MARATHON));

        // Double sided
        embossers.put(EmbosserType.ET,                  new EnablingTechnologiesDoubleSidedEmbosser("ET",                  "", EmbosserType.ET));
        embossers.put(EmbosserType.JULIET_PRO,          new EnablingTechnologiesDoubleSidedEmbosser("Juliet Pro",          "", EmbosserType.JULIET_PRO));
        embossers.put(EmbosserType.JULIET_PRO_60,       new EnablingTechnologiesDoubleSidedEmbosser("Juliet Pro 60",       "", EmbosserType.JULIET_PRO_60));
        embossers.put(EmbosserType.JULIET_CLASSIC,      new EnablingTechnologiesDoubleSidedEmbosser("Juliet Classic",      "", EmbosserType.JULIET_CLASSIC));

        // Production double sided
        embossers.put(EmbosserType.BOOKMAKER,           new EnablingTechnologiesDoubleSidedEmbosser("Bookmaker",           "", EmbosserType.BOOKMAKER));
        embossers.put(EmbosserType.BRAILLE_EXPRESS_100, new EnablingTechnologiesDoubleSidedEmbosser("Braille Express 100", "", EmbosserType.BRAILLE_EXPRESS_100));
        embossers.put(EmbosserType.BRAILLE_EXPRESS_150, new EnablingTechnologiesDoubleSidedEmbosser("Braille Express 150", "", EmbosserType.BRAILLE_EXPRESS_150));
        embossers.put(EmbosserType.BRAILLE_PLACE,       new EnablingTechnologiesDoubleSidedEmbosser("BraillePlace",        "", EmbosserType.BRAILLE_PLACE));
    }

    //jvm1.6@Override
    public Collection<Embosser> list() {
        return embossers.values();
    }
}
