package org.daisy.braille.facade;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.daisy.braille.pef.PEFValidator;
import org.daisy.validator.ValidatorFactory;

public class PEFValidatorFacade {
	
	public static boolean validate(File in) throws IOException {
		return validate(in, null);
	}
	
	public static boolean validate(File in, PrintStream msg) throws IOException {
		if (!in.exists()) {
			throw new FileNotFoundException("File does not exist: " + in);
		}
		ValidatorFactory factory = ValidatorFactory.newInstance();
		org.daisy.validator.Validator pv = factory.newValidator(PEFValidator.class.getCanonicalName());
		if (pv == null) {
			throw new IOException("Could not find validator.");
		}
		if (msg!=null) {
			msg.println("Validating " + in + " using \"" + pv.getDisplayName() + "\": " + pv.getDescription());
		}
		boolean ok = pv.validate(in.toURI().toURL());
		if (msg!=null) {
			msg.println("Validation was " + (ok ? "succcessful" : "unsuccessful"));
		}
		if (!ok && msg!=null) {
			msg.println("Messages returned by the validator:");
			InputStreamReader report = new InputStreamReader(pv.getReportStream());
			int c;
			while ((c = report.read()) != -1) {
				msg.print((char)c);
			}
			report.close();
			return ok;
		}
		return ok;
	}

}
