package es_once_cidat;

import org.daisy.braille.embosser.LineBreaks;

/**
 *
 * @author Bert Frees
 */
public class CidatLineBreaks implements LineBreaks {

    public static enum Type { IMPACTO_TRANSPARENT,
                              PORTATHIEL_TRANSPARENT
    };
    private final String newline;

    public CidatLineBreaks(Type type) {

        switch (type) {
            case IMPACTO_TRANSPARENT:
                newline = "\u001b\n";
                break;
            case PORTATHIEL_TRANSPARENT:
                newline = "\u00cd\u00da";
                break;
            default:
                newline = "";
        }
    }

    public String getString() {
        return newline;
    }
}