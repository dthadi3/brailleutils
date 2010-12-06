package org.daisy.braille.embosser;


public class SimpleEmbosserProperties implements EmbosserWriterProperties {
	
	private double cellWidth = 6;
	private double cellHeight = 10;
	private boolean supports8dot=false;
	private boolean supportsDuplex=false;
	private boolean supportsAligning=false;
	private boolean supportsVolumes=false;
	private final int maxHeight;
	private final int maxWidth;

	public SimpleEmbosserProperties(int maxWidth, int maxHeight) {
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
	}
	
	public SimpleEmbosserProperties supports8dot(boolean val) { supports8dot = val; return this; }
	public SimpleEmbosserProperties supportsDuplex(boolean val) { supportsDuplex = val; return this; }
	public SimpleEmbosserProperties supportsAligning(boolean val) { supportsAligning = val; return this; }
	public SimpleEmbosserProperties supportsVolumes(boolean val) { supportsVolumes = val; return this; }
	public SimpleEmbosserProperties cellWidth(double val) { cellWidth = val; return this; }
	public SimpleEmbosserProperties cellHeight(double val) { cellHeight = val; return this; }
	
/*	
	public SimpleEmbosserProperties paper(Paper paper) {
		if (paper!=null) {
			maxWidth = EmbosserTools.getWidth(paper, cellWidth);
			maxHeight = EmbosserTools.getHeight(paper, cellHeight);
		}
		return this;
	}
*/
	public int getMaxHeight() {
		return maxHeight;
	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public boolean supports8dot() {
		return supports8dot;
	}

	public boolean supportsAligning() {
		return supportsAligning;
	}

	public boolean supportsDuplex() {
		return supportsDuplex;
	}

	public boolean supportsVolumes() {
		return supportsVolumes;
	}

	public double getCellWidth() {
		return cellWidth;
	}
	
	public double getCellHeight() {
		return cellHeight;
	}
}