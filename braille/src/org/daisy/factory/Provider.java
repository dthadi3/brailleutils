package org.daisy.factory;

import java.util.Collection;

/**
 * Provides an interface for a collection of Factories.
 * @author Joel Håkansson, TPB
 *
 * @param <T> the type of Factories handled by this Provider
 */
public interface Provider<T extends Factory> {
	
	/**
	 * Lists all Factories
	 * @return returns a collection of Factories
	 */
	public Collection<T> list();

}
