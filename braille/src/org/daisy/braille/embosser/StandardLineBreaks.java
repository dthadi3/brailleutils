package org.daisy.braille.embosser;

public class StandardLineBreaks implements LineBreaks {
	public static enum Type {DOS, UNIX, MAC, DEFAULT};
	private final String newline;
	
	/**
	 * Creates a new object with the system's default line break style.
	 */
	public StandardLineBreaks() {
		this(Type.DEFAULT);
	}

	/**
	 * Creates a new object with the specified line break style
	 * @param t the type of line break
	 */
	public StandardLineBreaks(Type t) {
		newline = getString(t);
	}

	public String getString() {
		return newline;
	}
	
	public static String getString(Type t) {
        switch (t) {
	    	case UNIX: return "\n";
	    	case DOS: return "\r\n";
	    	case MAC: return "\r";
	    	default: return System.getProperty("line.separator", "\r\n");
        }
	}

}
