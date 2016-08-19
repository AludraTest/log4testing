package org.aludratest.log4testing.config;

import java.util.Properties;

/**
 * Interface for configurations of Test Step Filters applied to a Writer.
 * 
 * @author falbrech
 *
 */
public interface TestStepFilterConfiguration {

	/**
	 * Returns the class name of the Filter to use. The class must implement {@link org.aludratest.log4testing.TestStepFilter}.
	 * 
	 * @return The class name of the Filter to use, never <code>null</code>.
	 */
	String getClassName();

	/**
	 * Returns the properties to use to configure the Filter.
	 * 
	 * @return The properties to use to configure the Filter, maybe an empty object, but never <code>null</code>.
	 */
	Properties getProperties();

}
