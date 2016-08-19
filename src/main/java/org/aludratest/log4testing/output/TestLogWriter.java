package org.aludratest.log4testing.output;

import java.util.Properties;

import org.aludratest.log4testing.TestCaseLog;
import org.aludratest.log4testing.TestSuiteLog;
import org.aludratest.log4testing.config.InvalidConfigurationException;

/**
 * Interface for Test Log Writers. Implementations can usually write logs with two general patterns:
 * <ul>
 * <li>Handle each event, and update the log accordingly, or</li>
 * <li>Only handle {@link #finishedTestProcess(TestSuiteLog)}, and write the whole log at once</li>
 * </ul>
 * It mainly depends on underlying log storage and format which approach to use. The two Writers shipped with Log4Testing
 * implement both patterns: The HTML writer updates the log files constantly, while the XML writer writes only one XML file at
 * once when test process is finished.<br>
 * <br>
 * Test Log Writer implementations must consider that test execution may make heavy use of concurrency, so some of its methods
 * could be called by multiple threads in parallel. Implementations must handle this to avoid any kinds of racing conditions or
 * concurrency exceptions.
 * 
 * @author falbrech
 * 
 */
public interface TestLogWriter {

	/**
	 * Initializes this Writer with the given properties.
	 * 
	 * @param properties
	 *            Configuration properties to configure this writer with.
	 * 
	 * @throws InvalidConfigurationException
	 *             If the configuration is invalid for this Writer.
	 */
	void init(Properties properties) throws InvalidConfigurationException;

	/**
	 * Called when the test process starts. A test writer should prepare its output structures here, as far as possible, and
	 * already here signal if writing is not possible (e.g. no write access to file system), throwing a
	 * <code>LogWriterException</code> in this case.
	 * 
	 * @param rootSuite
	 *            Log of the root suite, which may not be filled completely yet.
	 * 
	 * @throws LogWriterException
	 *             If any log write operation fails.
	 */
	void startingTestProcess(TestSuiteLog rootSuite) throws LogWriterException;

	/**
	 * Called when a test suite starts.
	 * 
	 * @param suite
	 *            Log of the suite, which may not be filled completely yet.
	 * 
	 * @throws LogWriterException
	 *             If any log write operation fails.
	 */
	void startingTestSuite(TestSuiteLog suite) throws LogWriterException;

	/**
	 * Called when a test case starts.
	 * 
	 * @param testCase
	 *            Log of the test case, which may not be filled completely yet.
	 * 
	 * @throws LogWriterException
	 *             If any log write operation fails.
	 */
	void startingTestCase(TestCaseLog testCase) throws LogWriterException;

	/**
	 * Called when a test case is finished.
	 * 
	 * @param testCase
	 *            Log of the test case, which is guaranteed to be complete now.
	 * 
	 * @throws LogWriterException
	 *             If any log write operation fails.
	 */
	void finishedTestCase(TestCaseLog testCase) throws LogWriterException;

	/**
	 * Called when a test suite is finished.
	 * 
	 * @param suite
	 *            Log of the suite, which is guaranteed to be complete now.
	 * @throws LogWriterException
	 *             If any log write operation fails.
	 */
	void finishedTestSuite(TestSuiteLog suite) throws LogWriterException;

	/**
	 * Called when the whole test process is finished.
	 * 
	 * @param rootSuite
	 *            Log of the root suite, which is guaranteed to be complete now.
	 * 
	 * @throws LogWriterException
	 *             If any log write operation fails.
	 */
	void finishedTestProcess(TestSuiteLog rootSuite) throws LogWriterException;

}
