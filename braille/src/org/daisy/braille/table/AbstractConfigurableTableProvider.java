package org.daisy.braille.table;

import org.daisy.braille.table.EmbosserBrailleConverter.EightDotFallbackMethod;

public abstract class AbstractConfigurableTableProvider<T> implements ConfigurableTableProvider<T> {
	
	protected EightDotFallbackMethod fallback;
	protected char replacement;
	
	public AbstractConfigurableTableProvider(EightDotFallbackMethod fallback, char replacement) {
		this.fallback = fallback;
		this.replacement = replacement;
	}

	private void setFallback(String value) {
		if (value != null && !"".equals(value)) {
			setFallback(EightDotFallbackMethod.valueOf(value.toUpperCase()));
		}
	}

	private void setFallback(EightDotFallbackMethod value) {
		fallback = value;
	}

	/**
	 * hex value between 2800-283F
	 * 
	 * @param value
	 * @return
	 */
	private void setReplacement(String value) {
		if (value != null && !"".equals(value)) {
			setReplacement((char) Integer.parseInt(value, 16));
		}
	}

	private void setReplacement(char value) {
		int val = (value + "").codePointAt(0);
		if (val >= 0x2800 && val <= 0x283F) {
			replacement = value;
		} else {
			throw new IllegalArgumentException("Replacement value out of range");
		}
	}
	
	public void setFeature(String key, Object value) {
		if ("replacement".equals(key)) {
			if (value!=null) {
				setReplacement((String)value);
			}
		} else if ("fallback".equals(key)) {
			if (value!=null) {
				setFallback(value.toString());
			}
		} else {
			throw new IllegalArgumentException("Unknown feature: " + key);
		}
	}
	
	public Object getFeature(String key) {
		if ("replacement".equals(key)) {
			return replacement;
		} else if ("fallback".equals(key)) {
			return fallback;
		} else {
			throw new IllegalArgumentException("Unknown feature: " + key);
		}
	}

}