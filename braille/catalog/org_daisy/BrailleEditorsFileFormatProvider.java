package org_daisy;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import org.daisy.braille.embosser.FileFormat;
import org.daisy.braille.embosser.FileFormatProvider;

/**
 *
 * @author Bert Frees
 */
public class BrailleEditorsFileFormatProvider implements FileFormatProvider {

    public static enum FileType { BRF, BRL, BRA };

    private final Map<FileType,FileFormat> map;

    public BrailleEditorsFileFormatProvider() {
            map = new HashMap<FileType,FileFormat>();
            map.put(FileType.BRF, new BrailleEditorsFileFormat(FileType.BRF));
            map.put(FileType.BRA, new BrailleEditorsFileFormat(FileType.BRA));
            //map.put(FileType.BRL, new BrailleEditorsFileFormat(FileType.BRL));
    }

    public Collection<FileFormat> list() {
        return map.values();
    }
}
