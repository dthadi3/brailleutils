/*
 * Braille Utils (C) 2010-2011 Daisy Consortium 
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
package org_daisy;

import java.util.ArrayList;
import java.util.Collection;

import org.daisy.paper.DefaultPaper;
import org.daisy.paper.Paper;
import org.daisy.paper.PaperProvider;

public class NorthAmericaPaperProvider implements PaperProvider {
	public static final double INCH_IN_MM = 25.4;
	enum PaperSize {
		LETTER,
		W11500THOU_X_H11INCH
	};

	private final ArrayList<Paper> papers;
	
	public NorthAmericaPaperProvider() {
		papers = new ArrayList<Paper>();
		papers.add(new DefaultPaper("Letter", "8.5 inch x 11 inch", PaperSize.LETTER, 8.5 * INCH_IN_MM, 11 * INCH_IN_MM));
		papers.add(new DefaultPaper("11.5 inch x 11 inch", "11.5 inch wide, 11 inch high", PaperSize.W11500THOU_X_H11INCH, 11.5 * INCH_IN_MM, 11 * INCH_IN_MM));
	}

	//jvm1.6@Override
	public Collection<Paper> list() {
		return papers;
	}
}
