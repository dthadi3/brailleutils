/*
 * Braille Utils (C) 2010 Daisy Consortium 
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.daisy.braille.ui;

import java.io.File;

import org.daisy.braille.embosser.Embosser;
import org.daisy.braille.embosser.EmbosserCatalog;
import org.daisy.braille.embosser.LineBreaks;
import org.daisy.braille.facade.PEFConverterFacade;
import org.daisy.braille.facade.PEFValidatorFacade;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableCatalog;
import org.daisy.braille.table.EmbosserBrailleConverter.EightDotFallbackMethod;

/**
 * Reads a PEF-file and outputs a text file.
 * 
 * @author  Joel Hakansson, TPB
 * @version 2 jul 2008
 * @since 1.0
 */
class PEFParser {

	/**
	 * Command line entry point.
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length<2) {
			System.out.println("PEFParser input output [options ...]");
			System.out.println();
			System.out.println("Arguments");
			System.out.println("  input               path to the input file");
			System.out.println("  output              path to the output file");
			System.out.println();
			System.out.println("Options");
			System.out.println("  -embosser value     target embosser, available values are:");
			boolean first=true;
			EmbosserCatalog embf = EmbosserCatalog.newInstance();
			for (Embosser e : embf.list()) {
				System.out.println("                          \"" + e.getIdentifier() + "\"" + (first?" (default)":""));
				first=false;
			}
			System.out.println("  -table value        braille code table, available values are:");
			first=true;
			TableCatalog tablef = TableCatalog.newInstance();
			for (Table t : tablef.list()) {
				System.out.println("                          \"" + t.getIdentifier() + "\"" + (first?" (default)":""));
				first=false;
			}			
			System.out.println("  -breaks value       line break style, available values are:");
			first=true;
			for (LineBreaks.Type b : LineBreaks.Type.values()) {
				System.out.println("                          \"" + b.toString().toLowerCase() + "\"" + (first?" (default)":""));
				first=false;
			}
			System.out.println("  -range from[-to]    output a range of pages");
			System.out.println("  -fallback value     8-dot fallback method, available values are:");
			first=true;
			for (EightDotFallbackMethod f : EightDotFallbackMethod.values()) {
				System.out.println("                          \"" + f.toString().toLowerCase() + "\"" + (first?" (default)":""));
				first=false;
			}
			System.out.println("  -replacement value  replacement pattern, value in range 2800-283F");
			System.out.println("                      (default is 2800)");
			System.out.println();
			System.out.println("Note that the \"table\" and \"breaks\" options depend on target embosser.");

		} else {
			try {
				boolean ok = PEFValidatorFacade.validate(new File(args[0]), System.out);
				if (!ok) {
					System.out.println("Validation failed, exiting...");
					System.exit(-1);
				}
				PEFConverterFacade.parsePefFile(args);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
