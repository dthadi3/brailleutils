package org.daisy.factory;

/**
 * Provides an interface for filtering a collection of FactoryObjects.
 * @author Joel Håkansson, TPB
 *
 * @param <T> the type of FactoryObject handled by this FactoryFilter
 */
public interface FactoryFilter<T extends FactoryObject> {
	
	/**
	 * Tests if a specified object should be included in a list.
	 * @param object the Object to test
	 * @return returns true if the specified object should be included in a list, false otherwise
	 */
	public boolean accept(T object);

}
