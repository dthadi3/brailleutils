package org.daisy.braille.embosser;

import org.daisy.paper.Dimensions;

public class EmbosserTools {
	public static final double INCH_IN_MM = 25.4;

	public static byte[] toBytes(int val, int size) {
		StringBuffer sb = new StringBuffer();
		String s = "" + val;
		if (s.length()>size) {
			throw new IllegalArgumentException("Number is too big.");
		}
		for (int i=0; i<size-s.length(); i++) {
			sb.append('0');
		}
		sb.append(s);
		return sb.toString().getBytes();
	}
	
	/**
	 * Get width, in units
	 * @param unit unit in mm
	 * @return returns width in units
	 */
	public static int getWidth(Dimensions dim, double unit) {
		return (int)Math.floor(dim.getWidth() / unit);
	}
	
	/**
	 * Get height, in units
	 * @param unit unit in mm
	 * @return returns width in units
	 */
	public static int getHeight(Dimensions dim, double unit) {
		return (int)Math.floor(dim.getHeight() / unit);
	}

}
