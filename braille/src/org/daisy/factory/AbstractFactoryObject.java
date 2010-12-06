package org.daisy.factory;

/**
 * Provides an abstract class for FactoryObjects.
 * @author Joel Håkansson, TPB
 *
 */
public abstract class AbstractFactoryObject implements FactoryObject {
	private final String name;
	private final String desc;
	private final String identifier;
	
	public AbstractFactoryObject(String name, String desc, String identifier) {
		this.name = name;
		this.desc = desc;
		if (identifier==null) {
			this.identifier = this.toString();
		} else {
			this.identifier = identifier;
		}
	}
	
	public AbstractFactoryObject(String name, String desc, Enum<? extends Enum<?>> identifier) {
		this(name, desc, identifier.getClass().getCanonicalName() + "." + identifier.toString());
	}
	
	@Override
	public String getDescription() {
		return desc;
	}

	@Override
	public String getDisplayName() {
		return name;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}
}
