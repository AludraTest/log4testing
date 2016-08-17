package org.aludratest.log4testing.config;

import java.util.Properties;

public class MockTestStepFilterConfiguration implements TestStepFilterConfiguration {

	private String className;

	private Properties properties;

	public MockTestStepFilterConfiguration(String className) {
		this.className = className;
	}

	@Override
	public String getClassName() {
		return className;
	}

	@Override
	public Properties getProperties() {
		return properties;
	}

}
