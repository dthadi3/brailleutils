package org.daisy.paper;

import org.daisy.braille.tools.Length;

public class TractorPaperFormat extends AbstractPageFormat {
	private final Length across, along;
	
	public TractorPaperFormat(TractorPaper paper) {
		this.across = paper.getLengthAcrossFeed();
		this.along = paper.getLengthAlongFeed();
	}
	
	public TractorPaperFormat(Length acrossPaperFeed, Length alongPaperFeed) {
		this.across = acrossPaperFeed;
		this.along = alongPaperFeed;
	}

	/**
	 * Gets the length of the paper perpendicular to the direction of the paper feed
	 * @return returns the length, in mm.
	 */
	//jvm1.6@Override
	public Length getLengthAcrossFeed() {
		return across;
	}
	
	/**
	 * Gets the length of the paper along the direction of the paper feed
	 * @return returns the length, in mm.
	 */
	//jvm1.6@Override
	public Length getLengthAlongFeed() {
		return along;
	}

	public Type getPageFormatType() {
		return Type.TRACTOR;
	}
	
	public TractorPaperFormat asTractorPaperFormat() {
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TractorPaperFormat [across=" + across + ", along=" + along
				+ "]";
	}
}