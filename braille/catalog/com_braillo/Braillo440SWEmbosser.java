package com_braillo;

import org.daisy.braille.embosser.EmbosserFeatures;
import org.daisy.paper.PageFormat;
import org.daisy.paper.Paper;
import org.daisy.paper.Paper.Type;

import com_braillo.BrailloEmbosserProvider.EmbosserType;

public class Braillo440SWEmbosser extends AbstractBraillo440Embosser {

	public Braillo440SWEmbosser(String name, String desc) {
		super(name, desc, EmbosserType.BRAILLO_440_SW);
	}

	@Override
	public void setFeature(String key, Object value) {
		if (EmbosserFeatures.SADDLE_STITCH.equals(key)) {
            try {
            	saddleStitchEnabled = (Boolean)value;
            } catch (ClassCastException e) {
            	throw new IllegalArgumentException("Unsupported value for saddle stitch.");
            }
		} else {
			super.setFeature(key, value);
		}
	}

	public boolean supportsPageFormat(PageFormat pageFormat) {
		return pageFormat.getPageFormatType() == PageFormat.Type.ROLL 
				&& pageFormat.asRollPaperFormat().getLengthAcrossFeed().asMillimeter() <= 330
				//TODO: this is inaccurate for 2 page mode
				&& pageFormat.asRollPaperFormat().getLengthAlongFeed().asMillimeter() <= 585;
	}

	public boolean supportsPaper(Paper paper) {
		return paper.getType() == Type.ROLL
			&& paper.asRollPaper().getLengthAcrossFeed().asMillimeter() <= 330;
	}

}
