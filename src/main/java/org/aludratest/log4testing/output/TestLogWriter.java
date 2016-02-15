package org.aludratest.log4testing.output;

import java.util.Properties;

import org.aludratest.log4testing.TestCaseLog;
import org.aludratest.log4testing.TestSuiteLog;
import org.aludratest.log4testing.config.InvalidConfigurationException;

/**
 * @author falbrech
 * 
 */
public interface TestLogWriter {

	void init(Properties properties) throws InvalidConfigurationException;

	void startingTestProcess(TestSuiteLog rootSuite) throws LogWriterException;

	void startingTestSuite(TestSuiteLog suite) throws LogWriterException;

	void startingTestCase(TestCaseLog testCase) throws LogWriterException;

	void finishedTestCase(TestCaseLog testCase) throws LogWriterException;

	void finishedTestSuite(TestSuiteLog suite) throws LogWriterException;

	void finishedTestProcess(TestSuiteLog rootSuite) throws LogWriterException;

}
