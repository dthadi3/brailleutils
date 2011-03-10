/*
 * Braille Utils (C) 2010 Daisy Consortium 
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

import java.io.OutputStream;

import org.daisy.braille.table.Table;
import org.daisy.braille.table.TableFilter;
import org.daisy.factory.Factory;
import org.daisy.paper.Dimensions;
import org.daisy.paper.PageFormat;
import org.daisy.printing.Device;

/**
 * Provides an interface for common properties of an Embosser.
 * @author Joel Håkansson
 *
 */
public interface Embosser extends Factory, EmbosserProperties {

	public boolean supportsDimensions(Dimensions dim);
	public boolean supportsTable(Table table);
	public TableFilter getTableFilter();
	public EmbosserWriter newEmbosserWriter(OutputStream os);
	public EmbosserWriter newEmbosserWriter(Device device);

	/**
	 * Gets the max width for the specified page format
	 * @param pageFormat the page format
	 * @return returns the max width for the specified page format
	 */
	public int getMaxWidth(PageFormat pageFormat);
	
	/**
	 * Gets the max height for the specified page format
	 * @param pageFormat the page format
	 * @return returns the max height for the specified page format
	 */
	public int getMaxHeight(PageFormat pageFormat);

}