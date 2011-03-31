package com_indexbraille;

import java.util.Collection;
import java.util.HashMap;

import org.daisy.braille.embosser.Embosser;
import org.daisy.braille.embosser.EmbosserProvider;

public class IndexEmbosserProvider implements EmbosserProvider {
	public static enum EmbosserType {
                INDEX_3_7,
                INDEX_ADVANCED,
                INDEX_BASIC_BLUE_BAR,
                INDEX_CLASSIC,
                INDEX_DOMINO,
                INDEX_EVEREST_S_V1,
                INDEX_EVEREST_D_V1,
                INDEX_BASIC_S_V2,
                INDEX_BASIC_D_V2,
                INDEX_EVEREST_D_V2,
                INDEX_4X4_PRO_V2,
                INDEX_BASIC_S_V3,
                INDEX_BASIC_D_V3,
                INDEX_EVEREST_D_V3,
                INDEX_4X4_PRO_V3,
                INDEX_4WAVES_PRO_V3,
                INDEX_BASIC_D_V4,
                INDEX_EVEREST_D_V4,
                INDEX_BRAILLE_BOX_V4
	};

	private final HashMap<EmbosserType, Embosser> embossers;

	public IndexEmbosserProvider() {
                embossers = new HashMap<EmbosserType, Embosser>();
                embossers.put(EmbosserType.INDEX_BASIC_BLUE_BAR, new BlueBarEmbosser("Index Braille - Basic \"blue bar\"", "Early Index Basic embosser"));
                embossers.put(EmbosserType.INDEX_BASIC_S_V2,     new IndexV2Embosser("Index Braille - Basic-S V2","", EmbosserType.INDEX_BASIC_S_V2));
                embossers.put(EmbosserType.INDEX_BASIC_D_V2,     new IndexV2Embosser("Index Braille - Basic-D V2","", EmbosserType.INDEX_BASIC_D_V2));
                embossers.put(EmbosserType.INDEX_EVEREST_D_V2,   new IndexV2Embosser("Index Braille - Everest-D V2","", EmbosserType.INDEX_EVEREST_D_V2));
                embossers.put(EmbosserType.INDEX_4X4_PRO_V2,     new IndexV2Embosser("Index Braille - 4X4 Pro V2","", EmbosserType.INDEX_4X4_PRO_V2));
                embossers.put(EmbosserType.INDEX_EVEREST_D_V3,   new IndexV3Embosser("Index Braille - Everest-D V3","", EmbosserType.INDEX_EVEREST_D_V3));
                embossers.put(EmbosserType.INDEX_BASIC_S_V3,     new IndexV3Embosser("Index Braille - Basic-S V3","", EmbosserType.INDEX_BASIC_S_V3));
                embossers.put(EmbosserType.INDEX_BASIC_D_V3,     new IndexV3Embosser("Index Braille - Basic-D V3","", EmbosserType.INDEX_BASIC_D_V3));
                embossers.put(EmbosserType.INDEX_4X4_PRO_V3,     new IndexV3Embosser("Index Braille - 4X4 Pro V3","", EmbosserType.INDEX_4X4_PRO_V3));
                embossers.put(EmbosserType.INDEX_4WAVES_PRO_V3,  new IndexV3Embosser("Index Braille - 4Waves Pro","", EmbosserType.INDEX_4WAVES_PRO_V3));
//                embossers.put(EmbosserType.INDEX_BASIC_D_V4,     new IndexV4Embosser("","", EmbosserType. INDEX_BASIC_D_V4));
//                embossers.put(EmbosserType.INDEX_EVEREST_D_V4,   new IndexV4Embosser("","", EmbosserType. INDEX_EVEREST_D_V4));
//                embossers.put(EmbosserType.INDEX_BRAILLE_BOX_V4, new IndexV4Embosser("","", EmbosserType. INDEX_BRAILLE_BOX_V4));
        }

	//jvm1.6@Override
	public Collection<Embosser> list() {
		return embossers.values();
	}

}