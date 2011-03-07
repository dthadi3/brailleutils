package org.daisy.paper;


/**
 * Provides the page format to use when embossing
 * @author Joel Håkansson
 *
 */
public class PageFormat implements Dimensions {
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
	
	private final Paper paper;
	private final Orientation orientation;

	/**
	 * Creates a new page format using the specified paper and orientation
	 * @param paper the paper to use
	 * @param o the orientation of the paper
	 */
	public PageFormat(Paper paper, Orientation o) {
		this.paper = paper;
		this.orientation = o;
	}
	
	/**
	 * Creates a new page format using the specified paper in the default orientation
	 * @param paper the paper to use
	 */
	public PageFormat(Paper paper) {
		this(paper, Orientation.DEFAULT);
	}

	/**
	 * Gets the width of the page, in mm
	 * @return returns the width of the page
	 */
	//jvm1.6@Override
	public double getWidth() {
		switch (orientation) {
			case REVERSED: return paper.getHeight();
			case DEFAULT: default:
				return paper.getWidth();
		}
	}
	
	/**
	 * Gets the height of the page, in mm
	 * @return returns the height of the page
	 */
	//jvm1.6@Override
	public double getHeight() {
		switch (orientation) {
			case REVERSED: return paper.getWidth();
			case DEFAULT: default:
				return paper.getHeight();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orientation == null) ? 0 : orientation.hashCode());
		result = prime * result + ((paper == null) ? 0 : paper.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		PageFormat other = (PageFormat) obj;
		if (orientation != other.orientation) return false;
		if (paper == null) {
			if (other.paper != null) return false;
		} else if (!paper.equals(other.paper)) return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PageFormat [paper=" + paper + ", orientation=" + orientation + "]";
	}
	
}