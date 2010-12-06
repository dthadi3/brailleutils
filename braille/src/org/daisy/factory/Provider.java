package org.daisy.factory;

import java.util.Collection;

/**
 * Provides an interface for a collection of FactoryObjects.
 * @author Joel Håkansson, TPB
 *
 * @param <T> the type of FactoryObjects handled by this Provider
 */
public interface Provider<T extends FactoryObject> {
	
	/**
	 * Lists all FactoryObjects
	 * @return returns a collection of FactoryObjects
	 */
	public Collection<T> list();

}
