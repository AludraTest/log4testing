package org.aludratest.log4testing.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MockTestLogWriterConfiguration implements TestLogWriterConfiguration {

	private String writerClassName;

	private List<TestStepFilterConfiguration> filters = new ArrayList<TestStepFilterConfiguration>();

	private Properties writerProperties = new Properties();

	public MockTestLogWriterConfiguration(String writerClassName) {
		this.writerClassName = writerClassName;
	}

	@Override
	public String getWriterClassName() {
		return writerClassName;
	}

	@Override
	public List<TestStepFilterConfiguration> getTestStepFilters() {
		return filters;
	}

	@Override
	public Properties getWriterProperties() {
		return writerProperties;
	}

}
