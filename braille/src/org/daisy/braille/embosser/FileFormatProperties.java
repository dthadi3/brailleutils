package org.daisy.braille.embosser;

/**
 * @author Bert Frees
 *
 */
public interface FileFormatProperties {

    public boolean supports8dot();

    public boolean supportsDuplex();

    public String getFileExtension();

}
