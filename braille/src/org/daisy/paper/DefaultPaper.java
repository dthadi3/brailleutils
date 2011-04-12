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

import org.daisy.factory.AbstractFactory;

public class DefaultPaper extends AbstractFactory implements Paper {
	private final double width, height;
	private final Shape shape;

	/**
	 * 
	 * @param name
	 * @param desc
	 * @param width width, in mm
	 * @param height height, in mm
	 */
	public DefaultPaper(String name, String desc, Enum<? extends Enum<?>> identifier, double width, double height) {
		super(name, desc, identifier);
		this.width = width;
		this.height = height;
		if (getWidth()<getHeight()) {
			this.shape = Shape.PORTRAIT;
		} else if (getWidth()>getHeight()) {
			this.shape = Shape.LANDSCAPE;
		} else {
			this.shape = Shape.SQUARE;
		}
	}

	//jvm1.6@Override
	public Object getFeature(String key) {
		throw new IllegalArgumentException("Unknown feature: " + key);
	}

	//jvm1.6@Override
	public Object getProperty(String key) {
		return null;
	}

	//jvm1.6@Override
	public void setFeature(String key, Object value) {
		throw new IllegalArgumentException("Unknown feature: " + key);
	}

	//jvm1.6@Override
	public double getHeight() {
		return height;
	}

	//jvm1.6@Override
	public double getWidth() {
		return width;
	}

	public Shape getShape() {
		return shape;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DefaultPaper [width=" + width + ", height=" + height + ", shape=" + shape + "]";
	}

}