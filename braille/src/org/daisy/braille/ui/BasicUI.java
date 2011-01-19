package org.daisy.braille.ui;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.daisy.braille.tools.FileTools;

public class BasicUI extends AbstractUI {
	public final static String emboss = "emboss";
	public final static String text2pef = "text2pef";
	public final static String pef2text = "pef2text";
	public final static String validate = "validate";
	public final static String split = "split";
	public final static String merge = "merge";
	public final static String generate = "generate";
	public enum Mode {EMBOSS, TEXT2PEF, PEF2TEXT, VALIDATE, SPLIT, MERGE, GENERATE};

	private final String[] args;
	private final Mode m;
	private final static Logger logger = Logger.getLogger(BasicUI.class.getCanonicalName());
	
	public BasicUI(String[] args) {
		this.args = args;
		if (args.length<1) {
			System.out.println("Expected at least one argument.");
			System.out.println();
			displayHelp(System.out);
			System.exit(-ExitCode.MISSING_ARGUMENT.ordinal());
		}
		Mode m2;
		try {
			m2 = Mode.valueOf(args[0].toUpperCase());
		} catch (IllegalArgumentException e) {
			m2 = null;
			System.out.println("Unknown argument '" + args[0] + "'");
			displayHelp(System.out);
			System.exit(-ExitCode.UNKNOWN_ARGUMENT.ordinal());
		}
		m = m2;
	}
	
	/**
	 * Sets the context class loader to an URLClassLoader containing the jars found in
	 * the specified path. 
	 * @param dir the directory to search for jar-files.
	 */
	public static void setPluginsDir(File dir) {
		// list jars and convert to URL's
		URL[] jars = FileTools.toURL(FileTools.listFiles(dir, ".jar"));
		for (URL u : jars) {
			logger.info("Found jar " + u);
		}
		// set context class loader
		if (jars.length>0) {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(jars));
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void run() throws Exception {
		//setPluginsDir(new File("plugins"));
		switch (m) {
			case EMBOSS:
				System.out.println("Starting embossing application...");
				EmbossPEF.main(getArgsSubList(1));
				break;
			case VALIDATE:
				System.out.println("Starting validating application...");
				ValidatePEF.main(getArgsSubList(1));
				break;
			case PEF2TEXT:
				System.out.println("Starting pef to text application...");
				PEFParser.main(getArgsSubList(1));
				break;
			case TEXT2PEF:
				System.out.println("Starting text to pef application...");
				TextParser.main(getArgsSubList(1));
				break;
			case SPLIT:
				System.out.println("Starting file splitter application...");
				SplitPEF.main(getArgsSubList(1));
				break;
			case MERGE:
				System.out.println("Starting file merger application...");
				MergePEF.main(getArgsSubList(1));
				break;
			case GENERATE:
				System.out.println("Starting generator application...");
				GeneratePEF.main(getArgsSubList(1));
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

	@Override
	public String getName() {
		return "BasicUI";
	}

	@Override
	public List<Argument> getRequiredArguments() {
		ArrayList<Argument> ret = new ArrayList<Argument>();
		ArrayList<Definition> values = new ArrayList<Definition>();
		values.add(new Definition(emboss, "emboss a PEF-file"));
		values.add(new Definition(text2pef, "convert text to pef"));
		values.add(new Definition(pef2text, "convert pef to text"));
		values.add(new Definition(validate, "validate a PEF-file"));
		values.add(new Definition(split, "split a PEF-file into several single volume files"));
		values.add(new Definition(merge, "merge several single volume PEF-files into one"));
		values.add(new Definition(generate, "generate a random PEF-file for testing"));
		ret.add(new Argument("app_to_run", "the application to run", values));
		return ret;
	}

	@Override
	public List<OptionalArgument> getOptionalArguments() {
		return null;
	}

}
