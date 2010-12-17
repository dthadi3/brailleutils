package com_indexbraille;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.daisy.braille.embosser.AbstractEmbosserWriter;
import org.daisy.braille.embosser.LineBreaks;
import org.daisy.braille.embosser.SimpleEmbosserProperties;
import org.daisy.braille.embosser.StandardLineBreaks;
import org.daisy.braille.table.BrailleConverter;

/**
 * @author Bert Frees
 * @author Joel Håkansson
 */
public class BlueBarEmbosserWriter extends AbstractEmbosserWriter {
	private final OutputStream os;
	private final BrailleConverter bc;
	private final List<Byte> buf;
	private int charsOnRow;

	/*
230 	btb = new TableFactory();
231 	btb.setTable(TableFactory.TableType.INDEX_BASIC); //OK
232 	b = new ConfigurableEmbosser.Builder(os, btb.newTable())
233 	.breaks(LineBreaks.Type.DOS) //ok
234 	.padNewline(ConfigurableEmbosser.Padding.BEFORE) //ok
235 	.supportsDuplex(false) //ok
236 	.supportsAligning(true) //ok
237 	.setPaper(paper)
238 	.linePreamble(new LinePreamble() { //ok
239 	public byte[] getBytes(int lineLength) { //ok
240 	if (lineLength == 0) {
241 	return new byte[0];
242 	} else {
243 	return new byte[]{ 0x1b, 0x5c, (byte)lineLength, 0x00 };
244 	}
245 	}
246 	});
247 	return b.build(); 
	 */
	
	public BlueBarEmbosserWriter(OutputStream os,  BrailleConverter bc, int maxWidth, int maxHeight) {
		SimpleEmbosserProperties props = new SimpleEmbosserProperties(maxWidth, maxHeight);
		props
			.supports8dot(false)
			.supportsDuplex(false)
			.supportsAligning(true);
		init(props);
		this.os = os;
		this.bc = bc;
		this.buf = new ArrayList<Byte>();
		charsOnRow = 0;
	}

	@Override
	public LineBreaks getLinebreakStyle() {
		return new StandardLineBreaks(StandardLineBreaks.Type.DOS);
	}

	@Override
	public Padding getPaddingStyle() {
		return Padding.BEFORE;
	}

	@Override
	public BrailleConverter getTable() {
		return bc;
	}

	@Override
	protected void add(byte b) throws IOException {
		buf.add(b);
	}

	@Override
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
	
	public void write(String braille) throws IOException {
		charsOnRow += braille.length();
		super.write(braille);
	}

	@Override
	public void close() throws IOException {
		flush();
		super.close();
	}

}