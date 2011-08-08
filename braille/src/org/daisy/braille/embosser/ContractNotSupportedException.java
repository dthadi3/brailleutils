package org.daisy.braille.embosser;

public class ContractNotSupportedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4350090838306339507L;

	public ContractNotSupportedException() {
	}

	public ContractNotSupportedException(String message) {
		super(message);
	}

	public ContractNotSupportedException(Throwable cause) {
		super(cause);
	}

	public ContractNotSupportedException(String message, Throwable cause) {
		super(message, cause);
	}

}
