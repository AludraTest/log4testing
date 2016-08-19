package org.aludratest.log4testing.config;

import java.util.List;
import java.util.Properties;

/**
 * Interface of the configuration for a single Writer within a Log4Testing configuration. Writer configurations consist of a
 * Writer's class name, the properties used to configure the writer, and a (possibly empty) list of Filters to apply.
 * 
 * @author falbrech
 *
 */
public interface TestLogWriterConfiguration {

	/**
	 * Returns the name of the Writer's class. The class must implement {@link org.aludratest.log4testing.output.TestLogWriter}.
	 * 
	 * @return The name of the Writer's class, never <code>null</code>.
	 */
	String getWriterClassName();

	/**
	 * Returns the list of test step filters to apply to this writer. A test step must pass each filter to be included in log
	 * objects passed to the Writer.
	 * 
	 * @return The list of test step filters to apply to this writer, maybe an empty list, but never <code>null</code>.
	 */
	List<? extends TestStepFilterConfiguration> getTestStepFilters();

	/**
	 * Returns the properties to configure the Writer with. Valid properties are defined in the Writer's documentation.
	 * 
	 * @return The properties to configure the Writer with, maybe an empty object, but never <code>null</code>.
	 */
	Properties getWriterProperties();

}
