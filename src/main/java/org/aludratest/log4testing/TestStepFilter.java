package org.aludratest.log4testing;

import java.util.Properties;

import org.aludratest.log4testing.config.InvalidConfigurationException;

/**
 * Interface for filters for test steps. Instances of classes implementing this interface can be attached to writers configured
 * for the Log4Testing engine. See configuration documentation of Log4Testing for details.
 * 
 * @author falbrech
 *
 */
public interface TestStepFilter {

	/**
	 * Initializes the Filter with the given properties which have been configured for the filter.
	 * 
	 * @param properties
	 *            Properties configured for the filter.
	 * 
	 * @throws InvalidConfigurationException
	 *             If the configuration is partly or completely invalid.
	 */
	void init(Properties properties) throws InvalidConfigurationException;

	/**
	 * Checks if the given test step log is approved by this filter.
	 * 
	 * @param testStep
	 *            Log of the test step to check.
	 * 
	 * @return <code>true</code> to approve this test step log (e.g. have it being included in the log), <code>false</code>
	 *         otherwise.
	 */
	boolean filter(TestStepLog testStep);

}
