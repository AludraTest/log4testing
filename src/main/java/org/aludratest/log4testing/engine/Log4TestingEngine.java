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

/**
 * Main entry point for Test Frameworks or Adapter packages to use Log4Testing. To use Log4Testing, call one of the
 * <code>newEngine()</code> methods, and call {@link #applyTo(TestFramework)} on the created engine with your Test Framework
 * implementation.
 * 
 * @author falbrech
 *
 */
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
	
	/**
	 * Creates a new engine which looks in the following locations for a <code>log4testing.xml</code> file to take its
	 * configuration from:
	 * <ul>
	 * <li>In the current Context Classloader's root</li>
	 * <li>In the Classloader's root which was used to load Log4Testing</li>
	 * <li>In the current working directory</li>
	 * </ul>
	 * If no configuration file is found, the engine starts up with an "empty" configuration, i.e. no writer configured and thus
	 * no log to be written.
	 * 
	 * @return A newly created Log4Testing engine a Test Framework can be hooked into.
	 */
	public static Log4TestingEngine newEngine() {
		return new Log4TestingEngine(new XmlBasedLog4TestingConfiguration());
	}

	/**
	 * Creates a new engine which first looks in the given location for its configuration, afterwards (if not found there) in its
	 * default locations (see {@link #newEngine()}). <br>
	 * If no configuration file is found, the engine starts up with an "empty" configuration, i.e. no writer configured and thus
	 * no log to be written.
	 * 
	 * @param xmlConfigurationLocation
	 *            Additional location to first scan for a configuration. Must be the full URL to the configuration XML file (not
	 *            to a directory).
	 * @return A newly created Log4Testing engine a Test Framework can be hooked into.
	 */
	public static Log4TestingEngine newEngine(URL xmlConfigurationLocation) {
		return new Log4TestingEngine(new XmlBasedLog4TestingConfiguration(xmlConfigurationLocation));
	}

	/**
	 * Creates a new engine with the given configuration to use.
	 * 
	 * @param configuration
	 *            Configuration to use for the new engine, must not be <code>null</code>.
	 * 
	 * @return A newly created Log4Testing engine a Test Framework can be hooked into.
	 */
	public static Log4TestingEngine newEngine(Log4TestingConfiguration configuration) {
		return new Log4TestingEngine(configuration);
	}

	/**
	 * Hooks this engine into the given Test Framework. The engine will add itself as a {@link TestFrameworkListener} to the given
	 * framework via its {@link TestFramework#addTestFrameworkListener(TestFrameworkListener)} method.
	 * 
	 * @param framework
	 *            Framework to hook the engine into, must not be <code>null</code>.
	 */
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
