package org.daisy.braille.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.daisy.braille.pef.PEFFileMerger;
import org.daisy.braille.pef.PEFFileMerger.SortType;


public class MergePEF extends AbstractUI {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		MergePEF ui = new MergePEF();
		if (args.length<3) {
			System.out.println("Expected three arguments.");
			System.out.println();
			ui.displayHelp(System.out);
			System.exit(-ExitCode.MISSING_ARGUMENT.ordinal());
		}
		PEFFileMerger merger = new PEFFileMerger();
		File input = new File(args[0]);
		File output = new File(args[1]);
		SortType sort = SortType.STANDARD;
		
		if (args.length>3) {
			Map<String, String> p = ui.toMap(args);
			String sortString = p.remove("sort");
			if (sortString.equalsIgnoreCase("alpha")) {
				sort = SortType.STANDARD;
			} else if (sortString.equalsIgnoreCase("number")) {
				sort = SortType.NUMERAL_GROUPING;
			} else {
				System.out.println("Illegal value for argument sort: " + sortString);
				System.exit(-ExitCode.ILLEGAL_ARGUMENT_VALUE.ordinal());
			}
		}
		merger.merge(input, new FileOutputStream(output), args[2], sort);
	}

	@Override
	public String getName() {
		return "merge";
	}

	@Override
	public List<Argument> getRequiredArguments() {
		ArrayList<Argument> ret = new ArrayList<Argument>();
		ret.add(new Argument("input_directory", "Path to input directory containing only PEF-files to merge"));
		ret.add(new Argument("ouput_file", "Path to output file"));
		ret.add(new Argument("identifier", "Publication identifier"));
		return ret;
	}

	@Override
	public List<OptionalArgument> getOptionalArguments() {
		ArrayList<OptionalArgument> ret = new ArrayList<OptionalArgument>();
		ArrayList<Definition> values = new ArrayList<Definition>();
		values.add(new Definition("alpha", "Sort in alphabetical order (character by character from left to right)"));
		values.add(new Definition("number", "Sort groups of digits as numbers (from smaller to larger)"));
		ret.add(new OptionalArgument("sort", "Sorting method to use when determining file order based on file name", values, "alpha"));
		return ret;
	}

}