package org.daisy.braille.ui;

public class BasicUI {
	public final static String emboss = "emboss";
	public final static String text2pef = "text2pef";
	public final static String pef2text = "pef2text";
	public final static String validate = "validate";
	public enum Mode {EMBOSS, TEXT2PEF, PEF2TEXT, VALIDATE};
	public enum ExitCode {OK, MISSING_ARGUMENT, UNKNOWN_ARGUMENT, FAILED_TO_READ, MISSING_RESOURCE};
	private final String[] args;
	private final Mode m;
	
	public BasicUI(String[] args) {
		this.args = args;
		if (args.length<1) {
			System.out.println("This is a command line UI wrapper. Append one of the following to begin:");
			displayHelp();
			System.exit(-ExitCode.MISSING_ARGUMENT.ordinal());
		}
		Mode m2;
		try {
			m2 = Mode.valueOf(args[0].toUpperCase());
		} catch (IllegalArgumentException e) {
			m2 = null;
			System.out.println("Unknown argument '" + args[0] + "'");
			displayHelp();
			System.exit(-ExitCode.UNKNOWN_ARGUMENT.ordinal());
		}
		m = m2;
	}
	
	public void run() throws Exception {
		switch (m) {
			case EMBOSS:
				System.out.println("Starting embossing application... ");
				EmbossPEF.main(getArgsSubList(1));
				break;
			case VALIDATE:
				System.out.println("Starting validating application... ");
				ValidatePEF.main(getArgsSubList(1));
				break;
			case PEF2TEXT:
				System.out.println("Starting application to convert from pef to text... ");
				PEFParser.main(getArgsSubList(1));
				break;
			case TEXT2PEF:
				System.out.println("Starting application to convert from text to pef... ");
				TextParser.main(getArgsSubList(1));
				break;
			default:
				throw new RuntimeException("Coding error");
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		BasicUI ui = new BasicUI(args);
		ui.run();
	}

	private void displayHelp() {
		System.out.println("\t" + emboss);
		System.out.println("\t" + text2pef);
		System.out.println("\t" + pef2text);
		System.out.println("\t" + validate);
	}
	
	private String[] getArgsSubList(int offset) {
		int len = args.length-offset;
		if (len==0) {
			// no args left
			return new String[]{};
		} else if (len<0) {
			// too few args
			throw new IllegalArgumentException("New array has a negative size");
		}
		String[] args2 = new String[len];
		System.arraycopy(args, offset, args2, 0, len);
		return args2;
	}

}
