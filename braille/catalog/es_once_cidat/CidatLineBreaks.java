package es_once_cidat;

import org.daisy.braille.embosser.LineBreaks;

/**
 *
 * @author Bert Frees
 */
public class CidatLineBreaks implements LineBreaks {

    public static enum Type { IMPACTO_TRANSPARENT };
    private final String newline;

    public CidatLineBreaks() {
        newline = "\u001b\n";
    }

    public String getString() {
        return newline;
    }
}