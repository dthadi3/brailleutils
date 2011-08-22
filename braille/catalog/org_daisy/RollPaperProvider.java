package org_daisy;

import java.util.ArrayList;
import java.util.Collection;

import org.daisy.braille.tools.Length;
import org.daisy.paper.Paper;
import org.daisy.paper.PaperProvider;
import org.daisy.paper.RollPaper;

public class RollPaperProvider implements PaperProvider {
	enum PaperSize {
		W21CM,
		W24CM,
		W28CM,
		W33CM
	}
	
	private final ArrayList<Paper> papers;
	
	public RollPaperProvider() {
		papers = new ArrayList<Paper>();
		papers.add(new RollPaper("21 cm wide", "", PaperSize.W21CM, Length.newCentimeterValue(21)));
		papers.add(new RollPaper("24 cm wide", "", PaperSize.W24CM, Length.newCentimeterValue(24)));
		papers.add(new RollPaper("28 cm wide", "", PaperSize.W28CM, Length.newCentimeterValue(28)));
		papers.add(new RollPaper("33 cm wide", "", PaperSize.W33CM, Length.newCentimeterValue(33)));
	}

	public Collection<Paper> list() {
		return papers;
	}

}
