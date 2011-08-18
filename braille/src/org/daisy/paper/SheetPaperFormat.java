package org.daisy.paper;

import org.daisy.braille.tools.Length;


public class SheetPaperFormat extends AbstractPageFormat {
	/**
	 * The width/height orientation of the page
	 */
	public enum Orientation {
		/**
		 *  Represents default orientation as defined by the Paper
		 */
		DEFAULT,
		/**
		 *  Represents reversed orientation as defined by the Paper 
		 */
		REVERSED
	}
	private final Orientation o;
	private final Length pageWidth, pageHeight;

	public SheetPaperFormat(SheetPaper paper, Orientation o) {
		this.pageWidth = paper.getPageWidth();
		this.pageHeight = paper.getPageHeight();
		this.o = o;
	}
	
	public SheetPaperFormat(Length pageWidth, Length pageHeight) {
		this(pageWidth, pageHeight, Orientation.DEFAULT);
	}
	
	public SheetPaperFormat(Length pageWidth, Length pageHeight, Orientation o) {
		this.pageWidth = pageWidth;
		this.pageHeight = pageHeight;
		this.o = o;
	}

	public Orientation getOrientation() {
		return o;
	}

	/**
	 * Gets the paper width
	 * @return returns the width.
	 */
	public Length getPageWidth() {
		switch (o) {
			case REVERSED:
				return pageHeight;
			case DEFAULT: default:
				return pageWidth;
		}
	}

	/**
	 * Gets the paper height
	 * @return returns the height.
	 */
	public Length getPageHeight() {
		switch (o) {
			case REVERSED:
				return pageWidth;
			case DEFAULT: default:
				return pageHeight;
		}
	}

	public Type getPageFormatType() {
		return Type.SHEET;
	}

	public SheetPaperFormat asSheetPaperFormat() {
		return this;
	}

}