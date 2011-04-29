package com_viewplus;

import java.util.Collection;
import java.util.HashMap;

import org.daisy.braille.embosser.Embosser;
import org.daisy.braille.embosser.EmbosserProvider;

public class ViewPlusEmbosserProvider implements EmbosserProvider {
    
    public static enum EmbosserType {
                                        PREMIER_80,
                                        PREMIER_100,
                                        ELITE_150,
                                        ELITE_200,
                                        PRO_GEN_II,
                                        CUB_JR,
                                        CUB,
                                        MAX,
                                        EMFUSE,
                                        EMPRINT_SPOTDOT
    };

    private final HashMap<EmbosserType, Embosser> embossers;

    public ViewPlusEmbosserProvider() {
        embossers = new HashMap<EmbosserType, Embosser>();

        // Elite Braille Printers
        embossers.put(EmbosserType.ELITE_150,       new TigerEmbosser("Elite 150",       "150 CPS",                  EmbosserType.ELITE_150));
        embossers.put(EmbosserType.ELITE_200,       new TigerEmbosser("Elite 200",       "200 CPS",                  EmbosserType.ELITE_200));

        // Premier Braille Printers
        embossers.put(EmbosserType.PREMIER_80,      new TigerEmbosser("Premier 80",      "80 CPS",                   EmbosserType.PREMIER_80));
        embossers.put(EmbosserType.PREMIER_100,     new TigerEmbosser("Premier 100",     "100 CPS",                  EmbosserType.PREMIER_100));

        // Pro Braille Printer
        embossers.put(EmbosserType.PRO_GEN_II,      new TigerEmbosser("Pro Gen II",      "100 CPS",                  EmbosserType.PRO_GEN_II));

        // Desktop Braille Printers
        embossers.put(EmbosserType.CUB_JR,          new TigerEmbosser("Cub Jr.",         "30 CPS",                   EmbosserType.CUB_JR));
        embossers.put(EmbosserType.CUB,             new TigerEmbosser("Cub",             "50 CPS",                   EmbosserType.CUB));
        embossers.put(EmbosserType.MAX,             new TigerEmbosser("Max",             "60 CPS",                   EmbosserType.MAX));

        // EmFuse Color Braille Station
        embossers.put(EmbosserType.EMFUSE,          new TigerEmbosser("EmFuse",          "Large-print, Color, and Braille. " +
                                                                                         "400 CPS",                  EmbosserType.EMFUSE));
        // Emprint Spotdot Ink & Braille Embossers
        embossers.put(EmbosserType.EMPRINT_SPOTDOT, new TigerEmbosser("Emprint SpotDot", "Ink and Braille. " +
                                                                                         "40-50 CPS",                EmbosserType.EMPRINT_SPOTDOT));
    }

    public Collection<Embosser> list() {
        return embossers.values();
    }
}