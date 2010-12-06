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

/**
 * Provides an interface for common embosser related properties. 
 * @author Joel Håkansson
 *
 */
public interface EmbosserProperties {

	/**
	 * Returns true if this embosser has some method for volume handling
	 * @return returns true if this embosser supports volumes
	 */
	public boolean supportsVolumes();

	/**
	 * Returns true if this embosser supports 8 dot braille
	 * @return returns true if this embosser supports 8 dot braille
	 */
	public boolean supports8dot();
	
	/**
	 * Returns true if this embosser supports duplex printing
	 * @return returns true if this embosser supports duplex printing
	 */
	public boolean supportsDuplex();
	
	/**
	 * Returns true if this embosser supports aligning. This indicates
	 * that rows can be padded with whitespace to move the text block
	 * horizontally using the value returned by <code>getMaxWidth</code>. 
	 * Should return true for all physical embossers, since they all have
	 * a finite row length.
	 * @return returns true if this embosser supports aligning, false otherwise.
	 */
	public boolean supportsAligning();

}
