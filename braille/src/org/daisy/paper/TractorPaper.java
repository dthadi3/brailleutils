package org.daisy.paper;

import org.daisy.braille.tools.Length;

/**
 * Perforated paper with paper guides.
 * @author Joel Håkansson
 *
 */
public class TractorPaper extends AbstractPaper {
	private final Length across, along;
	
	public TractorPaper(String name, String desc, Enum<? extends Enum<?>> identifier, Length across, Length along) {
		super(name, desc, identifier);
		this.across = across;
		this.along = along;
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

	public Type getType() {
		return Type.TRACTOR;
	}

	public TractorPaper asTractorPaper() {
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TractorPaper [lengthAcrossFeed=" + getLengthAcrossFeed() +
                                   ", lengthAlongFeed=" + getLengthAlongFeed() + "]";
	}
}