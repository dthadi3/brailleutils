/*
 * Braille Utils (C) 2010-2011 Daisy Consortium 
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com_braillo;

import java.util.Collection;
import java.util.HashMap;

import org.daisy.braille.embosser.Embosser;
import org.daisy.braille.embosser.EmbosserProvider;


public class BrailloEmbosserProvider implements EmbosserProvider {
	public static enum EmbosserType {
		BRAILLO_200,
		BRAILLO_270,
		BRAILLO_400_S, 
		BRAILLO_400_SR,
		BRAILLO_440_SW_2P,
		BRAILLO_440_SW_4P,
		BRAILLO_440_SWSF
	};
	
	private final HashMap<EmbosserType, Embosser> embossers;
	
	public BrailloEmbosserProvider() {
		embossers = new HashMap<EmbosserType, Embosser>();
		embossers.put(EmbosserType.BRAILLO_200, new Braillo200Embosser("Braillo 200 (firmware 000.17 or later)", "Embosser table must match hardware setup.", EmbosserType.BRAILLO_200));
		embossers.put(EmbosserType.BRAILLO_270, new Braillo200_270_400_v12_16Embosser("Braillo 270 (firmware 12 to 16)", "Embosser table must match hardware setup.", EmbosserType.BRAILLO_270));
		embossers.put(EmbosserType.BRAILLO_400_S, new Braillo200Embosser("Braillo 400S (firmware 000.17 or later)", "Embosser table must match hardware setup.", EmbosserType.BRAILLO_400_S));
		embossers.put(EmbosserType.BRAILLO_400_SR, new Braillo200Embosser("Braillo 400SR (firmware 000.17 or later)", "Embosser table must match hardware setup.", EmbosserType.BRAILLO_400_SR));
		embossers.put(EmbosserType.BRAILLO_440_SW_2P, new Braillo440Embosser("Braillo 440SW (two page mode)", "Embosser table must match hardware setup.", EmbosserType.BRAILLO_440_SW_2P));
		embossers.put(EmbosserType.BRAILLO_440_SW_4P, new Braillo440Embosser("Braillo 440SW (four page mode)", "Embosser table must match hardware setup.", EmbosserType.BRAILLO_440_SW_4P));
		embossers.put(EmbosserType.BRAILLO_440_SWSF, new Braillo440Embosser("Braillo 440SWSF", "Embosser table must match hardware setup.", EmbosserType.BRAILLO_440_SWSF));
	}

	//jvm1.6@Override
	public Collection<Embosser> list() {
		return embossers.values();
	}

}
