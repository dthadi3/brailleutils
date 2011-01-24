package org.daisy.braille.pef;

import java.io.IOException;

public class InputDetectionException extends IOException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6390717033560942932L;

	public InputDetectionException() {
		super();
	}

	public InputDetectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public InputDetectionException(String message) {
		super(message);
	}
	
	public InputDetectionException(Throwable cause) {
		super(cause);
	}
}