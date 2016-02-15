package org.aludratest.log4testing;

import java.util.Properties;

import org.aludratest.log4testing.config.InvalidConfigurationException;

public interface TestStepFilter {

	void init(Properties properties) throws InvalidConfigurationException;

	boolean filter(TestStepLog testStep);

}
