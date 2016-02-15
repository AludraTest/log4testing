package org.aludratest.log4testing;

public interface TestSuiteStatistics {

	public int getNumberOfTestCases();

	public int getNumberOfChildSuites();

	public int getNumberOfPassedTestCases();

	public int getNumberOfFailedTestCases();

	public int getNumberOfIgnoredTestCases();

	public int getNumberOfFunctionallyFailedTestCases();

	public int getNumberOfFailedAccessTestCases();

	public int getNumberOfFailedPerformanceTestCases();

	public int getNumberOfAutomationFailedTestCases();

	public int getNumberOfInconclusiveTestCases();

	public int getNumberOfIgnoredAndPassedTestCases();

	public int getNumberOfIgnoredAndFailedTestCases();

}
