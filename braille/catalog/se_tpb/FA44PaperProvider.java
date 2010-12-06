package se_tpb;

import java.util.ArrayList;
import java.util.Collection;

import org.daisy.paper.DefaultPaper;
import org.daisy.paper.Paper;
import org.daisy.paper.PaperProvider;

public class FA44PaperProvider implements PaperProvider {
	public static final double INCH_IN_MM = 25.4;
	enum PaperSize {
		FA44,
		//FA44_LEGACY
	};

	private final ArrayList<Paper> papers;
	
	public FA44PaperProvider() {
		papers = new ArrayList<Paper>();
		papers.add(new DefaultPaper("FA44", "261 mm x 297 mm", PaperSize.FA44, 261d, 297d));
		//papers.add(new DefaultPaper("FA44 (legacy)", "252 mm x 297 mm", PaperSize.FA44_LEGACY, 252d, 297d));
	}

	@Override
	public Collection<Paper> list() {
		return papers;
	}

}