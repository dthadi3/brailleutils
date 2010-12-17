package org.daisy.braille.embosser;

import java.io.IOException;
import java.io.OutputStream;

import org.daisy.braille.table.BrailleConverter;

/**
 * 
 * Provides a configurable embosser. Outputs to a single OutputStream. 
 * 
 * @author  Joel Hakansson, TPB
 * @version 22 okt 2008
 * @since 1.0
 */
public class ConfigurableEmbosser extends AbstractEmbosserWriter {

	private final LineBreaks breaks;
	private final Padding padNewline;
	private final OutputStream os;
	private final BrailleConverter bf;
	private final byte[] header;
	private final byte[] footer;
	private final boolean fillSheet;
	private final boolean lineFeedOnEmptySheet;
	
	public static class Builder {
		// required params
		private OutputStream os;
		private BrailleConverter bt;
		
		// optional params
		private LineBreaks breaks = new StandardLineBreaks(StandardLineBreaks.Type.DEFAULT);
		private Padding padNewline = Padding.values()[0];
		private byte[] header = new byte[0];
		private byte[] footer = new byte[0];
		private boolean fillSheet = false;
		private boolean lineFeedOnEmptySheet = false;
		EmbosserWriterProperties props = new SimpleEmbosserProperties(Integer.MAX_VALUE, Integer.MAX_VALUE);
		
		public Builder(OutputStream os, BrailleConverter bt) {
			this.os = os;
			this.bt = bt;
		}
		
		public Builder embosserProperties(EmbosserWriterProperties props) {
			this.props = props;
			return this;
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
		public Builder header(byte[] value) { header = value; return this; }
		public Builder footer(byte[] value) { footer = value; return this; }
		public Builder fillSheet(boolean value) { fillSheet = value; return this; }
		public Builder autoLineFeedOnEmptyPage(boolean value) { lineFeedOnEmptySheet = value; return this; }

		public ConfigurableEmbosser build() {
			return new ConfigurableEmbosser(this);
		}
	}
	
	protected void formFeed() throws IOException {
		if (lineFeedOnEmptySheet && pageIsEmpty()) {
			lineFeed();
		}
		super.formFeed();
	}

	private ConfigurableEmbosser(Builder builder) {
		bf = builder.bt;
		breaks = builder.breaks;
		padNewline = builder.padNewline;
		header = builder.header;
		footer = builder.footer;
		os = builder.os;
		fillSheet = builder.fillSheet;
		lineFeedOnEmptySheet = builder.lineFeedOnEmptySheet;
		init(builder.props);
	}

	protected void add(byte b) throws IOException {
		os.write(b);
	}
	
	protected void addAll(byte[] bytes)  throws IOException {
		os.write(bytes);
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
	
	public void open(boolean duplex) throws IOException {
		super.open(duplex);
		os.write(header);
	}
	
	public void close() throws IOException {
		if (fillSheet && supportsDuplex() && currentPage() % 2 == 0) {
			formFeed();
		}
		os.write(footer);
		os.close();
		super.close();
	}

}
