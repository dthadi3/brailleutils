package org.daisy.validator;

import java.util.Iterator;

import javax.imageio.spi.ServiceRegistry;

/**
 * Simple factory for instantiating a Validator based on its identifier
 * @author Joel Håkansson, TPB
 */
public class ValidatorFactory {

	protected ValidatorFactory() { }

	/**
	 * Obtains a new instance of a ValidatorFactory.
	 *
	 * @return returns a new ValidatorFactory instance.
	 */
	public static ValidatorFactory newInstance() {
		return new ValidatorFactory();
	}

	/**
	 * Obtains a new instance of a Validator with the given identifier
	 * @param identifier a string that identifies the desired implementation
	 * @return returns a Validator for the given identifier, or null if none is found
	 */
	public Validator newValidator(String identifier) {
		Iterator<Validator> i = ServiceRegistry.lookupProviders(Validator.class);
		while (i.hasNext()) {
			Validator v = i.next();
			if (identifier.equals(v.getIdentifier())) {
				return v;
			}
		}
		return null;
	}

}