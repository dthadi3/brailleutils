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
package org.daisy.paper;

import org.daisy.braille.tools.Length;

public class SheetPaper extends AbstractPaper {
	private final Length pageWidth, pageHeight;

	public SheetPaper(String name, String desc, Enum<? extends Enum<?>> identifier, Length across, Length along) {
		super(name, desc, identifier);
		this.pageWidth = across;
		this.pageHeight = along;
	}

	public Type getType() {
		return Type.SHEET;
	}

	public Length getPageWidth() {
		return pageWidth;
	}

	public Length getPageHeight() {
		return pageHeight;
	}

	public SheetPaper asSheetPaper() {
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SheetPaper [pageWidth=" + getPageWidth() + ", pageHeight="
				+ getPageHeight() + "]";
	}

}
