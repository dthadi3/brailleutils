package com_braillo;

import java.util.Collection;
import java.util.HashMap;

import org.daisy.braille.embosser.Embosser;
import org.daisy.braille.embosser.EmbosserProvider;


public class BrailloEmbosserProvider implements EmbosserProvider {
	public static enum EmbosserType {
		BRAILLO_200,
		BRAILLO_400_S, 
		BRAILLO_400_SR,
		BRAILLO_440_SW_2P,
		BRAILLO_440_SW_4P,
		BRAILLO_440_SWSF
	};
	
	private final HashMap<EmbosserType, Embosser> embossers;
	
	public BrailloEmbosserProvider() {
		embossers = new HashMap<EmbosserType, Embosser>();
		// NONE = Generic embosser, that we do not know much about, supports any paper and any table
		embossers.put(EmbosserType.BRAILLO_200, new Braillo200Embosser("Braillo 200 (firmware 000.17 or later)", "Embosser table must match hardware setup.", EmbosserType.BRAILLO_200));
		embossers.put(EmbosserType.BRAILLO_400_S, new Braillo200Embosser("Braillo 400S (firmware 000.17 or later)", "Embosser table must match hardware setup.", EmbosserType.BRAILLO_400_S));
		embossers.put(EmbosserType.BRAILLO_400_SR, new Braillo200Embosser("Braillo 400SR (firmware 000.17 or later)", "Embosser table must match hardware setup.", EmbosserType.BRAILLO_400_SR));
		embossers.put(EmbosserType.BRAILLO_440_SW_2P, new Braillo440Embosser("Braillo 440SW (two page mode)", "Embosser table must match hardware setup.", EmbosserType.BRAILLO_440_SW_2P));
		embossers.put(EmbosserType.BRAILLO_440_SW_4P, new Braillo440Embosser("Braillo 440SW (four page mode)", "Embosser table must match hardware setup.", EmbosserType.BRAILLO_440_SW_4P));
		embossers.put(EmbosserType.BRAILLO_440_SWSF, new Braillo440Embosser("Braillo 440SWSF", "Embosser table must match hardware setup.", EmbosserType.BRAILLO_440_SWSF));
	}

	@Override
	public Collection<Embosser> list() {
		return embossers.values();
	}

}
