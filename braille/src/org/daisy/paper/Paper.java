package org.daisy.paper;

import org.daisy.factory.Factory;

/**
 * Provides an interface for common properties of a Paper. 
 * @author Joel Håkansson, TPB
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