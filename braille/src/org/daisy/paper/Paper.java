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

import org.daisy.factory.Factory;

/**
 * Provides an interface for common properties of a Paper. 
 * @author Joel Håkansson
 *
 */
public interface Paper extends Factory, Dimensions  {
	/**
	 * The shape of the paper in the most commonly used orientation
	 */
	public enum Shape {
		/**
		 *  Represents portrait shape, that is to say that getWidth()<getHeight()
		 */
		PORTRAIT,
		/**
		 *  Represents landscape shape, that is to say that getWidth>getHeight()
		 */
		LANDSCAPE,
		/**
		 *  Represents square shape, that is to say that getWidth()==getHeight()
		 */
		SQUARE
	}

	/**
	 * Gets width of the paper in the most commonly used orientation, in mm.
	 * @return returns width in mm.
	 */
	//jvm1.6@Override
	public double getWidth();
	
	/**
	 * Gets height of the paper in the most commonly used orientation, in mm.
	 * @return returns height in mm.
	 */
	//jvm1.6@Override
	public double getHeight();

	/**
	 * Gets the proportional shape of the paper in the most commonly used orientation
	 * @return returns the proportional shape of the paper
	 */
	public Shape getShape();
	
}