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
import java.io.IOException;

import org.daisy.braille.facade.PEFConverterFacade;
import org.daisy.braille.facade.PEFValidatorFacade;
import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableCatalog;

/**
 * Reads an ASCII file and parses it into a basic PEF file.
 * 
 * In addition to the 64/256 defined code points defined in translation Mode, the
 * characters 0x0a, 0x0d (new row) and 0x0c (new page) may occur in the file. 
 * 
 * @author  Joel Hakansson, TPB
 * @version 28 aug 2008
 * @since 1.0
 */
//TODO: Add rows and cols params. Implement support for maximum page size. If exceeded, break row or page.

class TextParser {


	/**
	 * Command line entry point.
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length<2) {
			System.out.println("TextParser input output [options ...]");
			System.out.println();
			System.out.println("Arguments");
			System.out.println("  input               path to the input file");
			System.out.println("  output              path to the output file");
			System.out.println();
			System.out.println("Options");
			System.out.println("  -mode value        input braille code (auto detected by default), available values are:");
			TableCatalog tableFactory = TableCatalog.newInstance();
			for (Table t : tableFactory.list()) {
				System.out.println("                          \"" + t.getIdentifier() + "\"");
			}
			System.out.println("  -author value       the author of the publication");
			System.out.println("  -title value        the title of the publication");
			System.out.println("  -identifier value   the publications unique identifier. If no value is supplied, it will be a generated.");
			System.out.println("  -language value     set the document language (as defined by IETF RFC 3066)");
			System.out.println("  -duplex value       set the document's duplex property. Default is \"true\"");
			System.out.println("  -date value         set the publication date using the form \"yyyy-MM-dd\"");
		} else {
			try {
				PEFConverterFacade.parseTextFile(args);
				System.out.println("Validating result...");
				boolean ok = PEFValidatorFacade.validate(new File(args[1]), System.out);
				if (!ok) {
					System.out.println("Warning: Validation failed for " + args[1]);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}