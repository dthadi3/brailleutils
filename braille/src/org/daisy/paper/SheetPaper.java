package org.daisy.paper;

import org.daisy.braille.tools.Length;

public class SheetPaper extends AbstractPaper {
	private final Length pageWidth, pageHeight;

	public SheetPaper(String name, String desc, Enum<? extends Enum<?>> identifier, Length across, Length along) {
		super(name, desc, identifier);
		this.pageWidth = across;
		this.pageHeight = along;
	}

	public Type getType() {
		return Type.SHEET;
	}

	public Length getPageWidth() {
		return pageWidth;
	}

	public Length getPageHeight() {
		return pageHeight;
	}

	public SheetPaper asSheetPaper() {
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SheetPaper [pageWidth=" + pageWidth + ", pageHeight="
				+ pageHeight + "]";
	}

}