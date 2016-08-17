package org.aludratest.log4testing.config;

import java.util.List;
import java.util.Properties;

public interface Log4TestingConfiguration {
	
	AbbreviatorConfiguration getAbbreviatorConfiguration();

	List<? extends TestLogWriterConfiguration> getTestLogWriterConfigurations();

	Properties getGlobalProperties();

}
