package org.daisy.paper;

public abstract class AbstractPageFormat implements PageFormat {

	public SheetPaperFormat asSheetPaperFormat() {
		throw new ClassCastException();
	}
	
	public TractorPaperFormat asTractorPaperFormat() {
		throw new ClassCastException();
	}
	
	public RollPaperFormat asRollPaperFormat() {
		throw new ClassCastException();
	}

}
