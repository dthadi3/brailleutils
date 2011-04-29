package pl_com_harpo;

import org.daisy.braille.embosser.LineBreaks;

/**
 *
 * @author Bert Frees
 */
public class MountbattenLineBreaks implements LineBreaks {

    private final String newline;

    public MountbattenLineBreaks() {
        newline = "{np}";
    }

    public String getString() {
        return newline;
    }
}