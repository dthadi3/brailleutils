package org.daisy.paper;

import org.daisy.braille.tools.Length;

public class RollPaper extends AbstractPaper {
	private final Length across;
	
	public RollPaper(String name, String desc, Enum<? extends Enum<?>> identifier, Length across) {
		super(name, desc, identifier);
		this.across = across;
	}
	
	/**
	 * Gets the length of the paper perpendicular to the direction of the paper feed
	 * @return returns the length, in mm.
	 */
	public Length getLengthAcrossFeed() {
		return across;
	}

	public Type getType() {
		return Type.ROLL;
	}
	
	public RollPaper asRollPaper() {
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RollPaper [lengthAcrossFeed=" + getLengthAcrossFeed() + "]";
	}
}
