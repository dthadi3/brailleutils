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

	@Override
	public Collection<Paper> list() {
		return papers;
	}
}
