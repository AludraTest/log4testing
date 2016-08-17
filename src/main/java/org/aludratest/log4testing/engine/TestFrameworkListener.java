package org.aludratest.log4testing.engine;

import org.aludratest.log4testing.TestCaseLog;
import org.aludratest.log4testing.TestSuiteLog;

public interface TestFrameworkListener {
	
	void startingTestProcess(TestSuiteLog rootSuite);

	void startingTestSuite(TestSuiteLog suite);

	void startingTestCase(TestCaseLog testCase);

	void finishedTestCase(TestCaseLog testCase);

	void finishedTestSuite(TestSuiteLog suite);

	void finishedTestProcess(TestSuiteLog rootSuite);

}
