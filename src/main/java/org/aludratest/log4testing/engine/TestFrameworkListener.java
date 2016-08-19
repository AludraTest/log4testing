package org.aludratest.log4testing.engine;

import org.aludratest.log4testing.TestCaseLog;
import org.aludratest.log4testing.TestSuiteLog;

/**
 * Interface for listeners which want to receive events from a Test Framework.
 * 
 * @author falbrech
 *
 */
public interface TestFrameworkListener {
	
	/**
	 * Called when the whole test process starts. Usually, this should only occur once during the lifetime of a Log4Testing
	 * engine.
	 * 
	 * @param rootSuite
	 *            The root suite's log.
	 */
	void startingTestProcess(TestSuiteLog rootSuite);

	/**
	 * Called when a test suite starts. A test suite usually starts when the first test case in the suite or one of its descendant
	 * suites starts.
	 * 
	 * @param suite
	 *            The suite's log.
	 */
	void startingTestSuite(TestSuiteLog suite);

	/**
	 * Called when a test case starts.
	 * 
	 * @param testCase
	 *            The test case's log.
	 */
	void startingTestCase(TestCaseLog testCase);

	/**
	 * Called when a test case is finished - regardless of its final Test Status (which can be queried from its log).
	 * 
	 * @param testCase
	 *            The test case's log.
	 */
	void finishedTestCase(TestCaseLog testCase);

	/**
	 * Called when a test suite is finished. A test suite is usually finished when all of its test cases and all test cases of all
	 * descendant suites are finished.
	 * 
	 * @param suite
	 *            The suite's log.
	 */
	void finishedTestSuite(TestSuiteLog suite);

	/**
	 * Called when the whole test process is complete. Usually, this is the same moment when the root suite is finished.
	 * 
	 * @param rootSuite
	 *            The root suite's log.
	 */
	void finishedTestProcess(TestSuiteLog rootSuite);

}
