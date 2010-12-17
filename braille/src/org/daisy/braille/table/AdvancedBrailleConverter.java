package org.daisy.braille.table;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import org.daisy.braille.table.EmbosserBrailleConverter.EightDotFallbackMethod;
import org.daisy.braille.tools.StringTranslator;
import org.daisy.braille.tools.StringTranslator.MatchMode;

/**
 * Provides an advanced Braille converter mapping each braille character to a string.
 * @author Joel Håkansson
 * @author Bert Frees
 */
public class AdvancedBrailleConverter implements BrailleConverter {
	
	private HashMap<Character, String> b2t;
	private StringTranslator t2b;
	private final Charset charset;
	private final EightDotFallbackMethod fallback;
	private final Character replacement;
	private final boolean ignoreCase;
	private final boolean supports8dot;
	
	/**
	 * Creates a new 6-dot table
	 * @param table
	 * @param charset
	 * @param fallback
	 * @param replacement
	 * @param ignoreCase
	 */
	public AdvancedBrailleConverter(String[] table, Charset charset, EightDotFallbackMethod fallback, char replacement, boolean ignoreCase, MatchMode mode) {
		this(table, charset, fallback, replacement, ignoreCase, false, mode);
	}
	
	/**
	 * Creates a new 8-dot table
	 * @param table
	 * @param charset
	 * @param fallback
	 * @param replacement
	 * @param ignoreCase
	 */
	public AdvancedBrailleConverter(String[] table, Charset charset, boolean ignoreCase, MatchMode mode) {
		this(table, charset, EightDotFallbackMethod.MASK, null, ignoreCase, true, mode);
	}

	private AdvancedBrailleConverter(String[] table, Charset charset, EightDotFallbackMethod fallback, Character replacement, boolean ignoreCase, boolean dot, MatchMode mode) {
		if (table.length!=64 && !dot) {
			throw new IllegalArgumentException("Unsupported table length: " + table.length);
		}
		if (table.length!=256 && dot) {
			throw new IllegalArgumentException("Unsupported table length: " + table.length);
		}
		this.charset = charset;
		this.fallback = fallback;
		this.replacement = replacement;
		this.ignoreCase = ignoreCase;
		this.supports8dot = table.length == 256;
		b2t = new HashMap<Character, String>();
		t2b = new StringTranslator(mode);
		int i = 0;
		char b;
		for (String t : table) {
			b = (char) (0x2800 + i);
			put(b, t);
			i++;
		}
	}

	private void put(char braille, String glyphs) {
		if (ignoreCase) {
			t2b.addToken(glyphs.toLowerCase(), ""+braille);
		} else {
			t2b.addToken(glyphs, ""+braille);
		}
		b2t.put(braille, glyphs);
	}

	public Charset getPreferredCharset() {
		return charset;
	}

	public boolean supportsEightDot() {
		return supports8dot;
	}

	//TODO: remove main and create tests
	public static void main(String[] args) {
		ArrayList<String> a1 = new ArrayList<String>();
		for (int i=0; i<64; i++) {
			a1.add(String.valueOf((char)i));
		}
		String[] a2 = new String[]{
				"a", "ab", "aba", "c", "d", "e", "f", "g", "h", "i", 
				"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1", "i1", "j1",
				"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2", "i2", "j2",
				"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3", "i3", "j3",
				"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4", "i4", "j4",
				"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5", "i5", "j5",
				"a6", "b6", "c6", "d6"
				};
		AdvancedBrailleConverter bc = new AdvancedBrailleConverter(a2, Charset.forName("utf-8"), EightDotFallbackMethod.MASK, '\u2800', true, MatchMode.GREEDY);
		//new AdvancedBrailleConverter(a2, Charset.forName("utf-8"), true, MatchMode.GREEDY);
		System.out.println(bc.toBraille("aAABaaba1"));
	}

	public String toBraille(String text) {
		if (ignoreCase) {
			text = text.toLowerCase();
		}
		String ret = t2b.translate(text);
		return ret;
	}

	private String toText(char braillePattern) {
		if (b2t.get(braillePattern) == null) {
			int val = (braillePattern + "").codePointAt(0);
			if (val >= 0x2840 && val <= 0x28FF) {
				switch (fallback) {
					case MASK:
						return toText((char) (val & 0x283F));
					case REPLACE:
						if (b2t.get(replacement) != null) {
							return toText(replacement);
						} else {
							throw new IllegalArgumentException("Replacement char not found.");
						}
					case REMOVE:
						return null;
				}
			} else {
				throw new IllegalArgumentException("Braille pattern '" + braillePattern + "' not found.");
			}
		}
		return (b2t.get(braillePattern));
	}

	public String toText(String braille) {
		StringBuffer sb = new StringBuffer();
		String t;
		for (char c : braille.toCharArray()) {
			t = toText(c);
			if (t != null) {
				sb.append(t);
			}
		}
		return sb.toString();
	}

}
