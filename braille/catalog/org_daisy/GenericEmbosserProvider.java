package org_daisy;

import java.util.Collection;
import java.util.HashMap;

import org.daisy.braille.embosser.Embosser;
import org.daisy.braille.embosser.EmbosserProvider;

public class GenericEmbosserProvider implements EmbosserProvider {
	public static enum EmbosserType {
		NONE
	};
	
	private final HashMap<EmbosserType, Embosser> embossers;
	
	public GenericEmbosserProvider() {
		embossers = new HashMap<EmbosserType, Embosser>();
		embossers.put(EmbosserType.NONE, new GenericEmbosser("Unspecified", "Limited support for unknown embossers", EmbosserType.NONE));
	}

	//jvm1.6@Override
	public Collection<Embosser> list() {
		return embossers.values();
	}

}
 