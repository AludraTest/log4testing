package org.aludratest.log4testing;

import java.util.List;

/**
 * Interface for logs of test suites. Defines the recursive nature of suites (suites can be contained in other suites). <br>
 * Suite logs may contain child suite logs, or test case logs, or both.
 * 
 * @author falbrech
 *
 */
public interface TestSuiteLog extends NamedTestLogElement {

	/**
	 * Returns the parent suite log this test suite log belongs to, or <code>null</code> if this is the root test suite log.
	 * 
	 * @return The parent suite log this test suite log belongs to, or <code>null</code>.
	 */
	TestSuiteLog getParent();

	/**
	 * Returns the list of child suite logs of this suite log.
	 * 
	 * @return The list of child suite logs of this suite log, maybe an empty list, but never <code>null</code>.
	 */
	List<? extends TestSuiteLog> getChildSuites();

	/**
	 * Returns the list of test case logs in this suite log.
	 * 
	 * @return The list of test case logs in this suite log, maybe an empty list, but never <code>null</code>.
	 */
	List<? extends TestCaseLog> getTestCases();

	/**
	 * Gathers the statistics for this test suite log. As this usually includes iteration over all descendant elements, this may
	 * take some time. The returned object should contain all calculated values, and <b>not</b> e.g. only calculate them when its
	 * getters are called.
	 * 
	 * @return The calculated statistics for this test suite log, never <code>null</code>.
	 */
	TestSuiteStatistics gatherStatistics();


}
