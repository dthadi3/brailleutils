package org.daisy.braille.embosser;

import java.io.OutputStream;

import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableFilter;
import org.daisy.factory.Factory;


/**
 *
 * @author Bert Frees
 */
public interface FileFormat extends Factory, FileFormatProperties {

    public boolean supportsTable(Table table);
    public TableFilter getTableFilter();
    public EmbosserWriter newEmbosserWriter(OutputStream os);

}
