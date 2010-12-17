package org.daisy.braille.embosser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.print.PrintException;

import org.daisy.braille.table.BrailleConverter;
import org.daisy.printing.Device;

/**
 * Provides a buffered volume embossers. This is similar to {@link ConfigurableEmbosser},
 * except that it supports writing each volume separately to a PrinterDevice rather than
 * to an OutputStream. 
 * 
 * @author  Joel Håkansson, TPB
 */
public class BufferedVolumeEmbosser extends AbstractEmbosserWriter {

	private LineBreaks breaks;
	private Padding padNewline;
	private Device pd;
	private BrailleConverter bf;
	private Stack<ArrayList<Byte>> pages;
	private VolumeWriter vw;
	private final boolean lineFeedOnEmptySheet;
	
	public static class Builder {
		// required params
		private final Device pd;
		private final BrailleConverter bt;
		private final VolumeWriter vw;
		private final EmbosserWriterProperties ep;
		
		// optional params
		private LineBreaks breaks = new StandardLineBreaks();
		private Padding padNewline = Padding.values()[0];
		private boolean lineFeedOnEmptySheet = false;

		public Builder(Device pd, BrailleConverter bt, VolumeWriter vw, EmbosserWriterProperties ep) {
			this.pd = pd;
			this.bt = bt;
			this.vw = vw;
			this.ep = ep;
		}
		public Builder breaks(String value) { 
			if (value!=null && !"".equals(value)) {
				return breaks(new StandardLineBreaks(StandardLineBreaks.Type.valueOf(value.toUpperCase())));
			}
			return this;
		}
		public Builder breaks(LineBreaks value) {
			breaks = value; return this;
		}
		public Builder padNewline(String value) {
			if (value!=null && !"".equals(value)) {
				return padNewline(Padding.valueOf(value.toUpperCase()));
			}
			return this;
		}
		public Builder padNewline(Padding value) { padNewline = value; return this; }
		public Builder autoLineFeedOnEmptyPage(boolean value) { lineFeedOnEmptySheet = value; return this; }
		
		public BufferedVolumeEmbosser build() {
			return new BufferedVolumeEmbosser(this);
		}
	}
	
	private BufferedVolumeEmbosser(Builder builder) {
		vw = builder.vw;
		bf = builder.bt;
		breaks = builder.breaks;
		padNewline = builder.padNewline;
		lineFeedOnEmptySheet = builder.lineFeedOnEmptySheet;
		pd = builder.pd;
		init(builder.ep);
	}
	
	public void open(boolean duplex) throws IOException {
		super.open(duplex);
		initVolume();
	}
	
	private void initVolume() {
		pages = new Stack<ArrayList<Byte>>();
		pages.add(new ArrayList<Byte>());
	}
	
	public BrailleConverter getTable() {
		return bf;
	}
	
	public LineBreaks getLinebreakStyle() {
		return breaks;
	}
	
	public Padding getPaddingStyle() {
		return padNewline;
	}

	protected void add(byte b) {
		pages.peek().add(b);
	}
	
	protected void addAll(byte[] bytes) {
		ArrayList<Byte> page = pages.peek();
		for (byte b : bytes) {
			page.add(b);
		}
	}

	protected void formFeed() throws IOException {
		if (lineFeedOnEmptySheet && pageIsEmpty()) {
			lineFeed();
		}
		super.formFeed(); // form feed characters belong to the current page
        pages.add(new ArrayList<Byte>()); // start a new page
	}

	public void newVolumeSectionAndPage(boolean duplex) throws IOException {
		super.newVolumeSectionAndPage(duplex);
		finalizeVolume();
		initVolume();
	}
	
	private void finalizeVolume() throws IOException {
		File out = File.createTempFile("emboss", ".tmp");
		pages.pop();
		vw.write(pages, out);
		try {
			pd.transmit(out);
		} catch (PrintException e) {
			IOException e2 = new IOException();
			e2.initCause(e);
			throw e2;
		} finally {
			out.deleteOnExit();
		}
	}
	
	public void close() throws IOException {
		finalizeVolume();
		super.close();
	}

}
