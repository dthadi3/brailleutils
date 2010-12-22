package org.daisy.validator;

import java.io.InputStream;
import java.net.URL;

import org.daisy.factory.Factory;

/**
 * Simple interface for validators
 * @author Joel Håkansson, TPB
 */
public interface Validator extends Factory {
	
	/**
	 * Validates the resource at the given URL
	 * @param input the resource URL
	 * @return returns true if validation was successful, false otherwise
	 */
	public boolean validate(URL input);
	
	/**
	 * Gets the report for the latest call to validate
	 * @return returns an InputStream for the report
	 */
	public InputStream getReportStream();

}