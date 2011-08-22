package com_braillo;

import org.daisy.paper.PageFormat;
import org.daisy.paper.Paper;

import com_braillo.BrailloEmbosserProvider.EmbosserType;

public class Braillo400SREmbosser extends AbstractBraillo200Embosser {

	public Braillo400SREmbosser(String name, String desc) {
		super(name, desc, EmbosserType.BRAILLO_400_SR);
	}

	public boolean supportsPageFormat(PageFormat pageFormat) {
		return pageFormat.getPageFormatType() == PageFormat.Type.ROLL
			&& pageFormat.asRollPaperFormat().getLengthAcrossFeed().asMillimeter() >= 140
			&& pageFormat.asRollPaperFormat().getLengthAcrossFeed().asMillimeter() <= 330
			&& pageFormat.asRollPaperFormat().getLengthAlongFeed().asInches() >= 4
			&& pageFormat.asRollPaperFormat().getLengthAlongFeed().asInches() <= 14;
	}

	public boolean supportsPaper(Paper paper) {
		return paper.getType() == Paper.Type.ROLL 
			&& paper.asRollPaper().getLengthAcrossFeed().asMillimeter() >= 140
			&& paper.asRollPaper().getLengthAcrossFeed().asMillimeter() <= 330;
	}


}
