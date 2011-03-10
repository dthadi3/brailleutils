package com_indexbraille;

import java.io.OutputStream;

import org.daisy.braille.embosser.ConfigurableEmbosser;
import org.daisy.braille.embosser.EmbosserFactoryException;
import org.daisy.braille.embosser.EmbosserTools;
import org.daisy.braille.embosser.EmbosserWriter;
import org.daisy.braille.embosser.EmbosserWriterProperties;
import org.daisy.braille.embosser.SimpleEmbosserProperties;
import org.daisy.braille.embosser.StandardLineBreaks;
import org.daisy.braille.embosser.UnsupportedPaperException;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableCatalog;
import org.daisy.braille.table.TableFilter;
import org.daisy.paper.Dimensions;

import com_indexbraille.IndexEmbosserProvider.EmbosserType;

public class IndexV4Embosser extends IndexEmbosser { // COMING SOON
	
    public IndexV4Embosser(String name, String desc,  EmbosserType identifier) {
        super(name, desc, identifier);
    }

    public EmbosserWriter newEmbosserWriter(OutputStream os) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TableFilter getTableFilter() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}