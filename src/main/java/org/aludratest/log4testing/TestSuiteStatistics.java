package org.aludratest.log4testing;

public interface TestSuiteStatistics {

	int getNumberOfTestCases();

	int getNumberOfChildSuites();

	int getNumberOfPassedTestCases();

	int getNumberOfFailedTestCases();

	int getNumberOfIgnoredTestCases();

	int getNumberOfFunctionallyFailedTestCases();

	int getNumberOfFailedAccessTestCases();

	int getNumberOfFailedPerformanceTestCases();

	int getNumberOfAutomationFailedTestCases();

	int getNumberOfInconclusiveTestCases();

	int getNumberOfIgnoredAndPassedTestCases();

	int getNumberOfIgnoredAndFailedTestCases();

}
