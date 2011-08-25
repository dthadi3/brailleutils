package org.daisy.braille.tools;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Provides a utility to map eight dot patterns to six dot patterns. If the resulting
 * patterns are aligned without any row spacing, the patterns will appear the same as the 
 * original 8-dot patterns. This can be useful when printing 8-dot files to an embosser 
 * using a 6-dot table.
 * 
 * @author Joel Håkansson
 *
 */
public class EightToSixDotMapper {
	private final static int[] bitMap = {0x01, 0x08, 0x02, 0x10, 0x04, 0x20, 0x40, 0x80};
	private final int width;
	private ArrayList<BitSet> bs;
	private StringBuilder sb;
	
	/**
	 * Creates a new SixDotMapper with the specified line length
	 * @param width the length of the lines, in characters
	 */
	public EightToSixDotMapper(int width) {
		this.width = width;
		this.bs = new ArrayList<BitSet>();
		new BitSet(width*2);
		sb = new StringBuilder();
	}
	
	
	/**
	 * Writes a string of braille. Values must be between 0x2800 and 0x28FF.
	 * @param braille characters in the range 0x2800 to 0x28FF
	 * @throws IllegalArgumentException if the number of characters exceeds the line width
	 */
	public void write(String braille) {
		if (sb.length()+braille.length()>width) {
			throw new IllegalArgumentException("The maximum number of characters on a line was exceeded.");
		}
		sb.append(braille);
	}
	
	/**
	 * Starts a new line
	 * @param rowgap the row gap following the line currently in the buffer
	 */
	public void newLine(int rowgap) {
		flush();
		for (int i=0; i<rowgap; i++) {
			bs.add(new BitSet(width*2));
		}
	}

	/**
	 * Flushes the last line of characters. This will empty the buffer.
	 */
	public void flush() {
		flushToBitSet();
	}
	
	private void flushToBitSet() {
		String t = sb.toString();
		for (int i=0; i<4; i++) {
			BitSet s = new BitSet(width*2);
			int j=0;
			for (char c : t.toCharArray()) {
				s.set(j, (c&bitMap[i*2])==bitMap[i*2]);
				s.set(j+1, (c&bitMap[i*2+1])==bitMap[i*2+1]);
				j=j+2;
			}
			//System.err.println(s);
			bs.add(s);
		}
		sb = new StringBuilder();
	}
	
	public boolean hasMoreFullLines() {
		return bs.size()>=3;
	}
	
	public boolean hasMoreLines() {
		return bs.size()>0;
	}

	/**
	 * Reads a line from the output buffer. When the last line is read, the grid alignment resets (the
	 * characters are padded to their full 6-dot height).
	 * @return returns the line or null if the buffer is empty
	 */
	public String readLine() {
		if (bs.size()==0) {
			return null;
		}
		StringBuilder res = new StringBuilder();
		BitSet s;
		for (int j=0; j<width; j++) {
			char c = 0x2800;
			for (int i=0; i<3; i++) {
				if (bs.size()>i) {
					s = bs.get(i);
					if (s.get(j*2)) {
						c |= bitMap[i*2];
					}
					if (s.get(j*2+1)) {
						c |= bitMap[i*2+1];
					}
				}
			}
			res.append(c);
		}
		//remove first three lines
		for (int i=0; i<3; i++) {
			if (bs.size()>0) {
				bs.remove(0);
			}
		}
		return res.toString();
	}

}
