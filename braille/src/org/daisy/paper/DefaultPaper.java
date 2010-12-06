package org.daisy.paper;

import org.daisy.factory.AbstractFactoryObject;

public class DefaultPaper extends AbstractFactoryObject implements Paper {
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

	@Override
	public Object getFeature(String key) {
		throw new IllegalArgumentException("Unknown feature: " + key);
	}

	@Override
	public Object getProperty(String key) {
		return null;
	}

	@Override
	public void setFeature(String key, Object value) {
		throw new IllegalArgumentException("Unknown feature: " + key);
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
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