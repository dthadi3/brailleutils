package org.daisy.braille.table;


/**
 * 
 * @author joha
 *
 * @param <T> Identifier type used when creating a new Table
 */
public interface ConfigurableTableProvider<T> extends TableProvider {

	public Object getFeature(String key);
	public void setFeature(String key, Object value);
	/**
	 * 
	 * @param t
	 * @return returns a new BrailleConverter of type T
	 */
	public BrailleConverter newTable(T t);

}