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
package org.daisy.braille.pef;

import java.util.ArrayList;

/**
 * Provides a sorting algorithm that splits groups of digits and sorts these
 * segments as numbers, for example "sample-1, sample-2, sample-10" will be
 * sorted in that order. String sorting would sort this "sample-1, sample-10, sample-2". 
 * @author Joel Håkansson
 *
 */
public class NumeralSortString implements Comparable<NumeralSortString> {
	private ArrayList<Part> parts;
	private String str;
	
	private static class Part implements Comparable<Part> {
		enum Type {STRING, NUMBER}
		Type type;
		Integer intValue;
		String strValue;

		public Part(String str) {
			this.strValue = str;
			try {
				this.intValue = Integer.parseInt(str);
				type = Type.NUMBER;
			} catch (NumberFormatException e) {
				this.intValue = null;
				type = Type.STRING;
			}
		}
		
		public Type getType() {
			return type;
		}
		
		public Integer asNumber() {
			return intValue;
		}

		public String asString() {
			return strValue;
		}

		public int compareTo(Part otherObj) {
			if (otherObj==null) {
				throw new NullPointerException();
			}
			if (this.getType()==otherObj.getType()) {
				switch (this.getType()) {
					case NUMBER:
						return this.asNumber().compareTo(otherObj.asNumber());
					case STRING:
						return this.asString().compareTo(otherObj.asString());
				}
				return 0;
			} else if (this.getType()==Type.NUMBER) {
				return -1;
			} else {
				return 1;
			}
		}
		
		public boolean equals(Part otherObj) {
			if (otherObj==null) {
				return false;
			} else if (this.getType()==otherObj.getType()) {
				switch (this.getType()) {
					case NUMBER:
						return this.asNumber().equals(otherObj.asNumber());
					case STRING:
						return this.asString().equals(otherObj.asString());
				}
			}
			return false;
		}

	}

	public NumeralSortString(String str) {
		parts = new ArrayList<Part>();
		this.str = str;
		String[] partStr = str.split("(?<=\\d)(?=\\D)|(?<=\\D)(?=\\d)");
		for (String part : partStr) {
			parts.add(new Part(part));
		}
	}
	
	public Part getPart(int index) {
		return parts.get(index);
	}
	
	public int getPartCount() {
		return parts.size();
	}
	
	public String getValue() {
		return str;
	}

	public int compareTo(NumeralSortString otherObj) {
		if (otherObj==null) {
			throw new NullPointerException();
		}
		int thisLen = this.getPartCount();
		int otherLen = otherObj.getPartCount();
		int len = Math.min(thisLen, otherLen);
		for (int i=0; i<len; i++) {
			int c = this.getPart(i).compareTo(otherObj.getPart(i));
			if (c!=0) {
				return c;
			}
		}
		if (thisLen==otherLen) {
			return 0;
		} else if (thisLen < otherLen) {
			return -1;
		} else { 
			return 1;
		}
	}

	public boolean equals(NumeralSortString otherObj) {
		if (otherObj==null) {
			return false;
		}
		int thisLen = this.getPartCount();
		int otherLen = otherObj.getPartCount();
		if (thisLen==otherLen) {
			for (int i=0; i<thisLen; i++) {
				if (!this.getPart(i).equals(otherObj.getPart(i))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
