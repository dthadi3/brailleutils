package com_indexbraille;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.daisy.braille.embosser.AbstractEmbosserWriter;
import org.daisy.braille.embosser.LineBreaks;
import org.daisy.braille.embosser.EmbosserWriterProperties;
import org.daisy.braille.embosser.StandardLineBreaks;
import org.daisy.braille.table.BrailleConverter;

/**
 * @author Bert Frees
 * @author Joel Håkansson
 */
public class IndexTransparentEmbosserWriter extends AbstractEmbosserWriter {

    private final OutputStream os;
    private final BrailleConverter bc;
    private final List<Byte> buf;
    private int charsOnRow;
    private final byte[] header;
    private final byte[] footer;

    public IndexTransparentEmbosserWriter(OutputStream os,
                                          BrailleConverter bc,
                                          byte[] header,
                                          byte[] footer,
                                          EmbosserWriterProperties props) {
        init(props);
        if (header != null) { this.header = header; }
                       else { this.header = new byte[0]; }
        if (footer != null) { this.footer = footer; }
                       else { this.footer = new byte[0]; }
        this.os = os;
        this.bc = bc;
        this.buf = new ArrayList<Byte>();
        charsOnRow = 0;
    }

    //jvm1.6@Override
    public LineBreaks getLinebreakStyle() {
        return new StandardLineBreaks(StandardLineBreaks.Type.DOS);
    }

    //jvm1.6@Override
    public Padding getPaddingStyle() {
        return Padding.NONE;
    }

    //jvm1.6@Override
    public BrailleConverter getTable() {
        return bc;
    }

    //jvm1.6@Override
    protected void add(byte b) throws IOException {
        buf.add(b);
    }

    //jvm1.6@Override
    protected void addAll(byte[] b) throws IOException {
        for (byte bi : b) {
            add(bi);
        }
    }

    private void flush() throws IOException {
        if (charsOnRow>0) {
            byte[] preamble = new byte[]{0x1b, 0x5c, (byte)charsOnRow, 0x00};
            os.write(preamble);
        }
        for (byte b : buf) {
            os.write(b);
        }
        charsOnRow = 0;
        buf.clear();
    }

    @Override
    protected void lineFeed() throws IOException {
        super.lineFeed();
        flush();
    }

    @Override
    protected void formFeed() throws IOException {
        super.formFeed();
        flush();
    }

    @Override
    public void write(String braille) throws IOException {
        charsOnRow += braille.length();
        super.write(braille);
    }

    @Override
    public void open(boolean duplex) throws IOException {
        super.open(duplex);
        os.write(header);
    }

    @Override
    public void close() throws IOException {
        flush();
        os.write(footer);
        os.close();
        super.close();
    }
}