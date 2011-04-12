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
package org.daisy.braille.embosser;

/**
 * Provides common embosser features to be used when configuring an Embosser factory.
 * @author Joel Håkansson
 *
 */
public class EmbosserFeatures {
	public static final String CELL_WIDTH = "cellWidth";
	public static final String CELL_HEIGHT = "cellHeight";
	public static final String UNSUPPORTED_CELL_FALLBACK_METHOD = "fallback";
	public static final String UNSUPPORTED_CELL_REPLACEMENT = "replacement";

	public static final String TABLE = "table";
	public static final String PAGE_FORMAT = "pageFormat";
	
}