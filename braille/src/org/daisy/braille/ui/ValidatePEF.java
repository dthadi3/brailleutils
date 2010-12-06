package org.daisy.braille.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.daisy.validator.ValidatorFactory;

class ValidatePEF {
	public enum Mode {LIGHT, FULL};

	public static void main(String[] args) throws IOException {
		if (args.length!=1) {
			System.out.println("Expected one more argument: input [options ...]");
			System.out.println("\tmode value\tvalidation mode, available values are:");
			boolean first=true;
			for (Mode b : Mode.values()) {
				System.out.println("\t\t\"" + b.toString().toLowerCase() + "\"" + (first?" (default)":""));
				first=false;
			}
			System.exit(0);
		}
		File in = new File(args[0]);
		if (!in.exists()) {
			System.out.println("File does not exist: " + in);
			System.exit(-1);
		}
		ValidatorFactory factory = ValidatorFactory.newInstance();
		org.daisy.validator.Validator pv = factory.newValidator("org.daisy.braille.pef.PEFValidator");
		if (pv == null) {
			System.out.println("Could not find validator.");
			System.exit(-2);
		}
		System.out.println("Validating " + in + " using \"" + pv.getDisplayName() + "\": " + pv.getDescription());
		boolean ok = pv.validate(in.toURI().toURL());
		System.out.println("Validation was " + (ok ? "succcessful" : "unsuccessful"));
		if (!ok) {
			System.out.println("Messages returned by the validator:");
			InputStreamReader report = new InputStreamReader(pv.getReportStream());
			int c;
			while ((c = report.read()) != -1) {
				System.out.print((char)c);
			}
			report.close();
		}
	}

}
