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

public class ISO216PaperProvider implements PaperProvider {
	public static final double INCH_IN_MM = 25.4;
	enum PaperSize {
		A4,
		A3
	};

	private final ArrayList<Paper> papers;
	
	public ISO216PaperProvider() {
		papers = new ArrayList<Paper>();
		papers.add(new DefaultPaper("A4", "210 mm x 297 mm", PaperSize.A4, 210d, 297d));
		papers.add(new DefaultPaper("A3", "297 mm x 420 mm", PaperSize.A3, 297d, 420d));
	}

	//jvm1.6@Override
	public Collection<Paper> list() {
		return papers;
	}
}
