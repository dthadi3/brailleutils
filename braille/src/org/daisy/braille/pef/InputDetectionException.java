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
		//jvm1.6super(message, cause);
		//jvm1.5
		super(message);
		this.initCause(cause);
	}

	public InputDetectionException(String message) {
		super(message);
	}
	
	public InputDetectionException(Throwable cause) {
		//jvm1.6super(cause);
		//jvm1.5
		super();
		this.initCause(cause);
	}
}