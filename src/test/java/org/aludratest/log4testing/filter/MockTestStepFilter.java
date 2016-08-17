package org.aludratest.log4testing.filter;

import java.util.Properties;

import org.aludratest.log4testing.TestStepFilter;
import org.aludratest.log4testing.TestStepLog;
import org.aludratest.log4testing.config.InvalidConfigurationException;

public class MockTestStepFilter implements TestStepFilter {

	private Properties properties;

	@Override
	public void init(Properties properties) throws InvalidConfigurationException {
		this.properties = properties;
	}

	public Properties getProperties() {
		return properties;
	}

	@Override
	public boolean filter(TestStepLog testStep) {
		return true;
	}

}
