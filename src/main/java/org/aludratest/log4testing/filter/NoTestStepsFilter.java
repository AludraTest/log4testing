package org.aludratest.log4testing.filter;

import java.util.Properties;

import org.aludratest.log4testing.TestStepFilter;
import org.aludratest.log4testing.TestStepLog;
import org.aludratest.log4testing.config.InvalidConfigurationException;

/**
 * A very simple test step filter implementation which excludes ALL test steps from being logged. This is useful e.g. for XML
 * generation where only statistics down to test case level are required.
 * 
 * @author falbrech
 * 
 */
public class NoTestStepsFilter implements TestStepFilter {

	@Override
	public void init(Properties properties) throws InvalidConfigurationException {
		// nothing do do for a no-sayer
	}

	@Override
	public boolean filter(TestStepLog testStep) {
		// NO steps, please
		return false;
	}

}
