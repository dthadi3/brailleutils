package es_once_cidat;

import java.util.Collection;
import java.util.HashMap;

import org.daisy.braille.embosser.Embosser;
import org.daisy.braille.embosser.EmbosserProvider;

/**
 *
 * @author Bert Frees
 */
public class CidatEmbosserProvider implements EmbosserProvider {

    public static enum EmbosserType {
        IMPACTO_600,
        IMPACTO_TEXTO,
        PORTATHIEL_BLUE
    };

    private final HashMap<EmbosserType, Embosser> embossers;

    public CidatEmbosserProvider() {
        embossers = new HashMap<EmbosserType, Embosser>();
        embossers.put(EmbosserType.IMPACTO_600, new ImpactoEmbosser("Impacto 600", "High-quality, high-speed (600 pages per hour) double-sided embosser", EmbosserType.IMPACTO_600));
        embossers.put(EmbosserType.IMPACTO_TEXTO, new ImpactoEmbosser("Impacto Texto","High-quality, high-speed (800 pages per hour) double-sided embosser", EmbosserType.IMPACTO_TEXTO));
        embossers.put(EmbosserType.PORTATHIEL_BLUE, new PortathielBlueEmbosser("Portathiel Blue", "Small, lightweight, portable double-sided embosser", EmbosserType.PORTATHIEL_BLUE));
    }

    //jvm1.6@Override
    public Collection<Embosser> list() {
        return embossers.values();
    }
}