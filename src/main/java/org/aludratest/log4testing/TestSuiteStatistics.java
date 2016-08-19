package org.aludratest.log4testing;

/**
 * Interface for statistics of a {@link TestSuiteLog}. Implementations should usually hold all values directly. A default
 * implementation is available in Log4Testing (see known implementations).
 * 
 * @author falbrech
 *
 */
public interface TestSuiteStatistics {

	/**
	 * Returns the total number of test cases in the suite and all descendant suites.
	 * 
	 * @return The total number of test cases in the suite and all descendant suites.
	 */
	int getNumberOfTestCases();

	/**
	 * Contrary to its name, this method returns the total number of <code>descendant</code> test suite logs (not only direct
	 * children).
	 * 
	 * @return The total number of descendant test suite logs.
	 */
	int getNumberOfChildSuites();

	/**
	 * Returns the total number of test cases with status {@link TestStatus#PASSED} in the suite and all descendant suites.
	 * 
	 * @return The total number of test cases with status {@link TestStatus#PASSED} in the suite and all descendant suites.
	 */
	int getNumberOfPassedTestCases();

	/**
	 * Returns the total number of test cases with a failed test status in the suite and all descendant suites.
	 * 
	 * @return The total number of test cases with a failed test status in the suite and all descendant suites.
	 */
	int getNumberOfFailedTestCases();

	/**
	 * Returns the total number of test cases with the ignored flag set to <code>true</code> in the suite and all descendant
	 * suites.
	 * 
	 * @return The total number of test cases with the ignored flag set to <code>true</code> in the suite and all descendant
	 *         suites.
	 */
	int getNumberOfIgnoredTestCases();

	/**
	 * Returns the total number of test cases with status {@link TestStatus#FAILED} in the suite and all descendant suites.
	 * 
	 * @return The total number of test cases with status {@link TestStatus#FAILED} in the suite and all descendant suites.
	 */
	int getNumberOfFunctionallyFailedTestCases();

	/**
	 * Returns the total number of test cases with status {@link TestStatus#FAILEDACCESS} in the suite and all descendant suites.
	 * 
	 * @return The total number of test cases with status {@link TestStatus#FAILEDACCESS} in the suite and all descendant suites.
	 */
	int getNumberOfFailedAccessTestCases();

	/**
	 * Returns the total number of test cases with status {@link TestStatus#FAILEDPERFORMANCE} in the suite and all descendant
	 * suites.
	 * 
	 * @return The total number of test cases with status {@link TestStatus#FAILEDPERFORMANCE} in the suite and all descendant
	 *         suites.
	 */
	int getNumberOfFailedPerformanceTestCases();

	/**
	 * Returns the total number of test cases with status {@link TestStatus#FAILEDAUTOMATION} in the suite and all descendant
	 * suites.
	 * 
	 * @return The total number of test cases with status {@link TestStatus#FAILEDAUTOMATION} in the suite and all descendant
	 *         suites.
	 */
	int getNumberOfAutomationFailedTestCases();

	/**
	 * Returns the total number of test cases with status {@link TestStatus#INCONCLUSIVE} in the suite and all descendant suites.
	 * 
	 * @return The total number of test cases with status {@link TestStatus#INCONCLUSIVE} in the suite and all descendant suites.
	 */
	int getNumberOfInconclusiveTestCases();

	/**
	 * Returns the total number of test cases with status {@link TestStatus#PASSED} and their ignored flag set to
	 * <code>true</code> in the suite and all descendant suites.
	 * 
	 * @return The total number of test cases with status {@link TestStatus#PASSED} and their ignored flag set to
	 *         <code>true</code> in the suite and all descendant suites.
	 */
	int getNumberOfIgnoredAndPassedTestCases();

	/**
	 * Returns the total number of test cases with a failed test status and their ignored flag set to <code>true</code> in the
	 * suite and all descendant suites.
	 * 
	 * @return The total number of test cases with a failed test status and their ignored flag set to <code>true</code> in the
	 *         suite and all descendant suites.
	 */
	int getNumberOfIgnoredAndFailedTestCases();

}
