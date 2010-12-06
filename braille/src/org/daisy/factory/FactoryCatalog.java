package org.daisy.factory;

import java.util.Collection;

/**
 * Provides an interface for factory catalogs.
 * @author Joel Håkansson, TPB
 *
 * @param <T> the type of factory objects that this catalog contains
 */
public interface FactoryCatalog<T extends FactoryObject> extends Provider<T> {

	/**
	 * Gets the value of a feature for this catalog
	 * @param key the feature to get the value for
	 * @return returns the value of a feature for this factory
	 */
	public Object getFeature(String key);
	
	/**
	 * Sets the value of a feature for this catalog
	 * @param key the feature key
	 * @param value the feature value
	 */
	public void setFeature(String key, Object value);
	
	/**
	 * Gets the FactoryObject with this identifier
	 * @param identifier the identifier for the requested FactoryObject
	 * @return returns the FactoryObject with this identifier, or null if none is found
	 */
	public T get(String identifier);
	
	/**
	 * Lists the FactoryObjects available to this catalog that the
	 * supplied FactoryFilter accepts
	 * @param filter the FactoryFilter to use
	 * @return returns a collection of FactoryObjects
	 */
	public Collection<T> list(FactoryFilter<T> filter);

}
