package org.aludratest.log4testing.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MockLog4TestingConfiguration implements Log4TestingConfiguration {

	private MockAbbreviatorConfiguration abbreviatorConfiguration = new MockAbbreviatorConfiguration();

	private List<TestLogWriterConfiguration> writerConfigurations = new ArrayList<TestLogWriterConfiguration>();

	private Properties globalProperties = new Properties();

	@Override
	public MockAbbreviatorConfiguration getAbbreviatorConfiguration() {
		return abbreviatorConfiguration;
	}

	@Override
	public List<TestLogWriterConfiguration> getTestLogWriterConfigurations() {
		return writerConfigurations;
	}

	@Override
	public Properties getGlobalProperties() {
		return globalProperties;
	}

}
