package org.daisy.braille.embosser;
/**
 * Provides a usage contract that allows an implementation of 
 * EmbosserWriter to optimize or configure communication based on actual
 * properties of the expected communication.
 * @author Joel Håkansson
 *
 */
public class Contract {
	enum BrailleRange {
		/** 
		 * The braille range in this contract is undefined
		 */
		UNDEFINED,
		/**
		 * Only 6-dot braille characters will be sent to the embosser
		 */
		SIX_DOT,
		/**
		 * At least one 8-dot braille character will be sent to the embosser
		 */
		EIGHT_DOT
	};
	enum PageMode {
		/**
		 * Undefined
		 */
		UNDEFINED,
		/**
		 * Only duplex will be used
		 */
		DUPLEX,
		/**
		 * Only simplex will be used
		 */
		SIMPLEX,
		/**
		 * Both duplex and simplex <em>will</em> be used
		 */
		BOTH
	}
	
	public static class Builder {
		// optional
		private BrailleRange range = BrailleRange.UNDEFINED;
		private Integer pages = null;
		
		public Builder() { }
		
		/**
		 * Creates a new Builder using the specification in the supplied
		 * contract.
		 * @param contract the contract to use
		 */
		public Builder(Contract contract) {
			if (contract.getBrailleRange()!=BrailleRange.UNDEFINED) {
				this.range = contract.getBrailleRange();
			}
			if (contract.getPages()!=null) {
				this.pages = contract.getPages();
			}
		}
		
		public Builder setBrailleRange(BrailleRange value) {
			this.range = value;
			return this;
		}
		
		public Builder setPages(Integer value) {
			this.pages = value;
			return this;
		}
		
		public Contract build() {
			return new Contract(this);
		}
	}

	private final BrailleRange range;
	private final Integer pages;

	private Contract(Builder builder) {
		this.range = builder.range;
		this.pages = builder.pages;
	}

	public BrailleRange getBrailleRange() {
		return range;
	}
	
	public Integer getPages() {
		return pages;
	}

}
