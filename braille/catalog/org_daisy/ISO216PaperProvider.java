package org_daisy;

import java.util.ArrayList;
import java.util.Collection;

import org.daisy.paper.DefaultPaper;
import org.daisy.paper.Paper;
import org.daisy.paper.PaperProvider;

public class ISO216PaperProvider implements PaperProvider {
	public static final double INCH_IN_MM = 25.4;
	enum PaperSize {
		A4
	};

	private final ArrayList<Paper> papers;
	
	public ISO216PaperProvider() {
		papers = new ArrayList<Paper>();
		papers.add(new DefaultPaper("A4", "210 mm x 297 mm", PaperSize.A4, 210d, 297d));
	}

	@Override
	public Collection<Paper> list() {
		return papers;
	}
}
