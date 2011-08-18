package org.daisy.paper;

import org.daisy.braille.tools.Length;


public class RollPaperFormat extends AbstractPageFormat {
	private final Length across, along;
	
	public RollPaperFormat(RollPaper paper, Length length) {
		this.across = paper.getLengthAcrossFeed();
		this.along = length;
	}
	
	public RollPaperFormat(Length acrossPaperFeed, Length alongPaperFeed) {
		this.across = acrossPaperFeed;
		this.along = alongPaperFeed;
	}

	/**
	 * Gets the length of the paper perpendicular to the direction of the paper feed
	 * @return returns the length.
	 */
	public Length getLengthAcrossFeed() {
		return across;
	}
	
	/**
	 * Gets the length of the paper along the direction of the paper feed
	 * @return returns the length.
	 */
	public Length getLengthAlongFeed() {
		return along;
	}
	
	public Type getPageFormatType() {
		return Type.ROLL;
	}
	
	public RollPaperFormat asRollPaperFormat() {
		return this;
	}

}