package com_braillo;

import org.daisy.paper.PageFormat;
import org.daisy.paper.Paper;

import com_braillo.BrailloEmbosserProvider.EmbosserType;

public class Braillo400SEmbosser extends AbstractBraillo200Embosser {

	public Braillo400SEmbosser(String name, String desc) {
		super(name, desc, EmbosserType.BRAILLO_400_S);
	}

	public boolean supportsPageFormat(PageFormat pageFormat) {
		return pageFormat.getPageFormatType() == PageFormat.Type.TRACTOR
			&& pageFormat.asTractorPaperFormat().getLengthAcrossFeed().asMillimeter() >= 140
			&& pageFormat.asTractorPaperFormat().getLengthAcrossFeed().asMillimeter() <= 330
			&& pageFormat.asTractorPaperFormat().getLengthAlongFeed().asInches() >= 4
			&& pageFormat.asTractorPaperFormat().getLengthAlongFeed().asInches() <= 14;
	}

	public boolean supportsPaper(Paper paper) {
		return paper.getType() == Paper.Type.TRACTOR
			&& paper.asTractorPaper().getLengthAcrossFeed().asMillimeter() >= 140
			&& paper.asTractorPaper().getLengthAcrossFeed().asMillimeter() <= 330
			&& paper.asTractorPaper().getLengthAlongFeed().asInches() >= 4
			&& paper.asTractorPaper().getLengthAlongFeed().asInches() <= 14;
	}
}
