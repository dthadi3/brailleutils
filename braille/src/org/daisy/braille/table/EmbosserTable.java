package org.daisy.braille.table;

import java.util.HashMap;

public class EmbosserTable<T> extends AbstractTable {
	private final HashMap<String, Object> props;
	private final T type;
	private final ConfigurableTableProvider<T> provider;
	
	public EmbosserTable(String name, String desc, T type, ConfigurableTableProvider<T> provider) {
		super(name, desc, type.getClass().getCanonicalName() + "." + type.toString());
		this.type = type;
		this.provider = provider;
		props = new HashMap<String, Object>();
		props.put(TableProperties.IS_ONE_TO_ONE, true);
		props.put(TableProperties.IS_DISPLAY_FORMAT, true);
	}
	
	EmbosserTable<T> putProperty(String key, Object value) {
		props.put(key, value);
		return this;
	}

	//jvm1.6@Override
	public BrailleConverter newBrailleConverter() {
		return provider.newTable(type);
	}

	//jvm1.6@Override
	public void setFeature(String key, Object value) {
		provider.setFeature(key, value);
	}

	//jvm1.6@Override
	public Object getProperty(String key) {
		return props.get(key);
	}

	//jvm1.6@Override
	public Object getFeature(String key) {
		return provider.getFeature(key);
	}

}
