package pl_com_harpo;

import java.util.Collection;
import java.util.HashMap;

import org.daisy.braille.embosser.Embosser;
import org.daisy.braille.embosser.EmbosserProvider;

public class HarpoEmbosserProvider implements EmbosserProvider {

    public static enum EmbosserType {
        MOUNTBATTEN_LS,
        MOUNTBATTEN_PRO,
        MOUNTBATTEN_WRITER_PLUS
    };

    private final HashMap<EmbosserType, Embosser> embossers;

    public HarpoEmbosserProvider() {

        embossers = new HashMap<EmbosserType, Embosser>();
        embossers.put(EmbosserType.MOUNTBATTEN_LS,          new MountbattenEmbosser("Mountbatten LS", "",      EmbosserType.MOUNTBATTEN_LS));
        embossers.put(EmbosserType.MOUNTBATTEN_PRO,         new MountbattenEmbosser("Mountbatten Pro", "",     EmbosserType.MOUNTBATTEN_PRO));
        embossers.put(EmbosserType.MOUNTBATTEN_WRITER_PLUS, new MountbattenEmbosser("Mountbatten Writer+", "", EmbosserType.MOUNTBATTEN_WRITER_PLUS));
    }

    public Collection<Embosser> list() {
        return embossers.values();
    }
}