package com_indexbraille;

import java.util.Collection;
import java.util.HashMap;

import org.daisy.braille.embosser.Embosser;
import org.daisy.braille.embosser.EmbosserProvider;

public class IndexEmbosserProvider implements EmbosserProvider {
	public static enum EmbosserType {
		INDEX_EVEREST, 
		INDEX_BASIC, 
		INDEX_BASIC_BLUE_BAR,
		INDEX_EVEREST_V3, 
		INDEX_BASIC_D_V3,
	};
	
	private final HashMap<EmbosserType, Embosser> embossers;
	
	public IndexEmbosserProvider() {
		embossers = new HashMap<EmbosserType, Embosser>();
		embossers.put(EmbosserType.INDEX_EVEREST, new IndexV2Embosser("Index Braille - 9.20 Everest V2", "Index Everest embosser that talks, with serial and parallel connectors only", EmbosserType.INDEX_EVEREST));
		embossers.put(EmbosserType.INDEX_BASIC, new IndexV2Embosser("Index Braille - 3.30 Basic V2", "Index Basic embosser that talks, with serial and parallel connectors only", EmbosserType.INDEX_BASIC));
		embossers.put(EmbosserType.INDEX_BASIC_BLUE_BAR, new BlueBarEmbosser("Index Braille - Basic \"blue bar\"", "Early Index Basic embosser"));
		embossers.put(EmbosserType.INDEX_EVEREST_V3, new IndexEverestV3Embosser("Index Braille - Everest V3", "Index Everest embosser that talks, with serial, parallel, USB and RJ45 connectors", EmbosserType.INDEX_EVEREST_V3));
		embossers.put(EmbosserType.INDEX_BASIC_D_V3, new IndexBasicV3Embosser("Index Braille - Basic-D V3", "Index Basic embosser that talks, with serial, parallel, USB and RJ45 connectors", EmbosserType.INDEX_BASIC_D_V3));
	}

	//jvm1.6@Override
	public Collection<Embosser> list() {
		return embossers.values();
	}
	
}