package org.daisy.braille.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.daisy.braille.pef.PEFFileSplitter;

public class SplitPEF extends AbstractUI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SplitPEF ui = new SplitPEF();
		if (args.length!=2) {
			System.out.println("Expected two arguments.");
			System.out.println();
			ui.displayHelp(System.out);
			System.exit(-ExitCode.MISSING_ARGUMENT.ordinal());
		}
		File input = new File(args[0]);
		File output = new File(args[1]);
		PEFFileSplitter splitter = new PEFFileSplitter();
		splitter.split(input, output);
	}

	@Override
	public String getName() {
		return BasicUI.split;
	}

	@Override
	public List<Argument> getRequiredArguments() {
		ArrayList<Argument> ret = new ArrayList<Argument>();
		ret.add(new Argument("input_file", "Path to the input PEF-file"));
		ret.add(new Argument("output_directory", "Path to the output folder"));
		return ret;
	}

	@Override
	public List<OptionalArgument> getOptionalArguments() {
		return null;
	}

}
