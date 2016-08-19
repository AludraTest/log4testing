package org.aludratest.log4testing;

import java.util.List;

/**
 * Interface of a log for a single executed test case. A test case can contain 0 to n Test Step Groups, each containing 0 to n
 * Test Step Logs. Also, a test case can be flagged as ignored and <b>still</b> being executed, as e.g. AludraTest Framework does.
 * 
 * @author falbrech
 *
 */
public interface TestCaseLog extends NamedTestLogElement {

	/**
	 * Returns the test suite log this test case log belongs to.
	 * 
	 * @return The test suite log this test case log belongs to.
	 */
	TestSuiteLog getParent();

	/**
	 * Returns <code>true</code> if the ignored flag is set for this test case. This may or may not imply that the test case has
	 * been skipped (not executed) - depending on the Test Framework. AludraTest also executes ignored test cases, so this flag is
	 * only informational in this case (e.g. to mark the test case results grayed out in the log).
	 * 
	 * @return <code>true</code> if the ignored flag is set for this test case, <code>false</code> otherwise.
	 */
	boolean isIgnored();

	/**
	 * Returns if the test case is failed. This can also be <code>true</code> if the test case is ignored; in this case, the
	 * status will be <code>IGNORED</code> , but as the test may still have been executed (e.g. AludraTest does this), this flag
	 * here can be used to determine the execution success.
	 * 
	 * @return <code>true</code> if the test case execution was unsuccessful, <code>false</code> otherwise.
	 */
	boolean isFailed();

	/**
	 * Returns an optional description why the ignored flag is set for this test case (if it is set). A reason may or may not be
	 * present for an ignored test case.
	 * 
	 * @return An optional description why the ignored flag is set for this test case.
	 */
	String getIgnoredReason();

	/**
	 * Returns all test step groups belonging to this test case, if any.
	 * 
	 * @return All test step groups belonging to this test case, maybe an empty list, but never <code>null</code>.
	 */
	List<? extends TestStepGroupLog> getTestStepGroups();

	/**
	 * Convenience method to directly get the last test step log belonging to this test case and being marked as failed (not as
	 * ignored). A straightforward implementation iterates over all test step groups and checks their test steps, unless no
	 * further test step with a failure status is found.
	 * 
	 * @return The last failed test step log belonging to this test case, or <code>null</code>.
	 */
	TestStepLog getLastFailedStep();

	/**
	 * Counts all test steps in this test case log.
	 * 
	 * @return Number of all test steps in all test step groups of this test case log.
	 */
	int getNumberOfTestSteps();

}
