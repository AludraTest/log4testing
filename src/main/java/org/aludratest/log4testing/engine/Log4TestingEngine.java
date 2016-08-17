package org.aludratest.log4testing.engine;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.aludratest.log4testing.TestCaseLog;
import org.aludratest.log4testing.TestStepFilter;
import org.aludratest.log4testing.TestSuiteLog;
import org.aludratest.log4testing.config.AbbreviatorConfiguration;
import org.aludratest.log4testing.config.InvalidConfigurationException;
import org.aludratest.log4testing.config.Log4TestingConfiguration;
import org.aludratest.log4testing.config.TestLogWriterConfiguration;
import org.aludratest.log4testing.config.TestStepFilterConfiguration;
import org.aludratest.log4testing.config.impl.XmlBasedLog4TestingConfiguration;
import org.aludratest.log4testing.output.LogWriterException;
import org.aludratest.log4testing.output.TestLogWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Log4TestingEngine implements TestFrameworkListener {
	
	private static final Logger LOG = LoggerFactory.getLogger(Log4TestingEngine.class);

	private List<TestLogWriter> writers = new ArrayList<TestLogWriter>();

	private List<WriterSpecificTestFrameworkListener> writerListeners = new ArrayList<WriterSpecificTestFrameworkListener>();

	private Log4TestingEngine(Log4TestingConfiguration configuration) {
		for (TestLogWriterConfiguration writerConfig : configuration.getTestLogWriterConfigurations()) {
			try {
				Class<?> writerClass = Class.forName(writerConfig.getWriterClassName());
				TestLogWriter writer = (TestLogWriter) writerClass.newInstance();
				writer.init(writerConfig.getWriterProperties());
				writers.add(writer);
				writerListeners.add(new WriterSpecificTestFrameworkListener(writer, configuration.getAbbreviatorConfiguration(),
						writerConfig));
			}
			catch (ClassNotFoundException e) {
				LOG.warn("TestLogWriter class " + writerConfig.getWriterClassName() + " not found", e);
			}
			catch (InstantiationException e) {
				LOG.warn("Could not instantiate TestLogWriter of class " + writerConfig.getWriterClassName(), e);
			}
			catch (IllegalAccessException e) {
				LOG.warn("Could not access empty constructor of TestLogWriter class " + writerConfig.getWriterClassName(), e);
			}
			catch (InvalidConfigurationException e) {
				LOG.warn("Could not configure TestLogWriter of class " + writerConfig.getWriterClassName(), e);
			}
		}
	}
	
	public static Log4TestingEngine newEngine() {
		return new Log4TestingEngine(new XmlBasedLog4TestingConfiguration());
	}

	public static Log4TestingEngine newEngine(URL xmlConfigurationLocation) {
		return new Log4TestingEngine(new XmlBasedLog4TestingConfiguration(xmlConfigurationLocation));
	}

	public static Log4TestingEngine newEngine(Log4TestingConfiguration configuration) {
		return new Log4TestingEngine(configuration);
	}

	public void applyTo(TestFramework framework) {
		framework.addTestFrameworkListener(this);
	}

	@Override
	public void startingTestProcess(TestSuiteLog rootSuite) {
		for (WriterSpecificTestFrameworkListener listener : writerListeners) {
			listener.startingTestProcess(rootSuite);
		}
	}

	@Override
	public void startingTestSuite(TestSuiteLog suite) {
		for (WriterSpecificTestFrameworkListener listener : writerListeners) {
			listener.startingTestSuite(suite);
		}
	}

	@Override
	public void startingTestCase(TestCaseLog testCase) {
		for (WriterSpecificTestFrameworkListener listener : writerListeners) {
			listener.startingTestCase(testCase);
		}
	}

	@Override
	public void finishedTestCase(TestCaseLog testCase) {
		for (WriterSpecificTestFrameworkListener listener : writerListeners) {
			listener.finishedTestCase(testCase);
		}
	}

	@Override
	public void finishedTestSuite(TestSuiteLog suite) {
		for (WriterSpecificTestFrameworkListener listener : writerListeners) {
			listener.finishedTestSuite(suite);
		}
	}

	@Override
	public void finishedTestProcess(TestSuiteLog rootSuite) {
		for (WriterSpecificTestFrameworkListener listener : writerListeners) {
			listener.finishedTestProcess(rootSuite);
		}
	}

	private static class WriterSpecificTestFrameworkListener implements TestFrameworkListener {

		private TestLogWriter writer;

		private LogContext context;

		public WriterSpecificTestFrameworkListener(TestLogWriter writer, AbbreviatorConfiguration abbreviationConfig,
				TestLogWriterConfiguration writerConfig) {
			this.writer = writer;

			// create filters for writer
			List<TestStepFilter> filters;
			if (writerConfig.getTestStepFilters().isEmpty()) {
				filters = Collections.emptyList();
			}
			else {
				filters = new ArrayList<TestStepFilter>();
				for (TestStepFilterConfiguration filterConfig : writerConfig.getTestStepFilters()) {
					try {
						TestStepFilter filter = (TestStepFilter) Class.forName(filterConfig.getClassName()).newInstance();
						filter.init(filterConfig.getProperties());
						filters.add(filter);
					}
					catch (ClassNotFoundException e) {
						LOG.warn("Test Step Filter class not found: " + filterConfig.getClassName(), e);
					}
					catch (InstantiationException e) {
						LOG.warn("Could not instantiate Test Step Filter Class " + filterConfig.getClassName(), e);
					}
					catch (IllegalAccessException e) {
						LOG.warn("Could not access empty constructor of Test Step Filter Class " + filterConfig.getClassName(), e);
					}
					catch (InvalidConfigurationException e) {
						LOG.warn("Invalid configuration for Test Step Filter of Class " + filterConfig.getClassName(), e);
					}
				}
			}

			context = new LogContext(abbreviationConfig.getAbbreviations(), filters);
		}

		@Override
		public void startingTestProcess(TestSuiteLog rootSuite) {
			try {
				writer.startingTestProcess(new FilteringTestSuiteLog(rootSuite, context));
			}
			catch (LogWriterException e) {
				LOG.warn("Exception when logging to log writer", e);
			}
		}

		@Override
		public void startingTestSuite(TestSuiteLog suite) {
			try {
				writer.startingTestSuite(new FilteringTestSuiteLog(suite, context));
			}
			catch (LogWriterException e) {
				LOG.warn("Exception when logging to log writer", e);
			}
		}

		@Override
		public void startingTestCase(TestCaseLog testCase) {
			try {
				writer.startingTestCase(new FilteringTestCaseLog(testCase, context));
			}
			catch (LogWriterException e) {
				LOG.warn("Exception when logging to log writer", e);
			}
		}

		@Override
		public void finishedTestCase(TestCaseLog testCase) {
			try {
				writer.finishedTestCase(new FilteringTestCaseLog(testCase, context));
			}
			catch (LogWriterException e) {
				LOG.warn("Exception when logging to log writer", e);
			}
		}

		@Override
		public void finishedTestSuite(TestSuiteLog suite) {
			try {
				writer.finishedTestSuite(new FilteringTestSuiteLog(suite, context));
			}
			catch (LogWriterException e) {
				LOG.warn("Exception when logging to log writer", e);
			}
		}

		@Override
		public void finishedTestProcess(TestSuiteLog rootSuite) {
			try {
				writer.finishedTestProcess(new FilteringTestSuiteLog(rootSuite, context));
			}
			catch (LogWriterException e) {
				LOG.warn("Exception when logging to log writer", e);
			}
		}
	}

}
