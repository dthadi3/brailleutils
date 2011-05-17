package es_once_cidat;

import org.daisy.braille.embosser.PageBreaks;

/**
 *
 * @author Bert Frees
 */
public class CidatPageBreaks implements PageBreaks {

    public static enum Type { IMPACTO_TRANSPARENT,
                              PORTATHIEL_TRANSPARENT
    };
    private final String newpage;

    public CidatPageBreaks(Type type) {

        switch (type) {
            case PORTATHIEL_TRANSPARENT:
                newpage = "\u00c7";
                break;
            case IMPACTO_TRANSPARENT:
                newpage = "\u001b\u000c";
                break;
            default:
                newpage = "";
                break;
        }
    }

    public String getString() {
        return newpage;
    }
}