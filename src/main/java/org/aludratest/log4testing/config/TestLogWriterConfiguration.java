package org.aludratest.log4testing.config;

import java.util.List;
import java.util.Properties;

public interface TestLogWriterConfiguration {

	String getWriterClassName();

	List<? extends TestStepFilterConfiguration> getTestStepFilters();

	Properties getWriterProperties();

}
