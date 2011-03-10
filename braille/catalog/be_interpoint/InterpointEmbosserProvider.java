package be_interpoint;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.daisy.braille.embosser.Embosser;
import org.daisy.braille.embosser.EmbosserProvider;

/**
 *
 * @author Bert Frees
 */
public class InterpointEmbosserProvider implements EmbosserProvider {

    public static enum EmbosserType { INTERPOINT_55 }

    private final Map<EmbosserType, Embosser> embossers;
    
    public InterpointEmbosserProvider() {
        embossers = new HashMap<EmbosserType, Embosser>();
        embossers.put(EmbosserType.INTERPOINT_55, new Interpoint55Embosser());
    }

    public Collection<Embosser> list() {
        return embossers.values();
    }
}
