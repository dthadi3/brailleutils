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
        embossers.put(EmbosserType.IMPACTO_600, new ImpactoEmbosser("", "", EmbosserType.IMPACTO_600));
        embossers.put(EmbosserType.IMPACTO_TEXTO, new ImpactoEmbosser("","", EmbosserType.IMPACTO_TEXTO));
    }

    @Override
    public Collection<Embosser> list() {
        return embossers.values();
    }
}