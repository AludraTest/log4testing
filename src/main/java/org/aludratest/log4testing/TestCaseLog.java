package org.aludratest.log4testing;

import java.util.List;

public interface TestCaseLog extends NamedTestLogElement {

	TestSuiteLog getParent();

	boolean isIgnored();

	/**
	 * Returns if the test case is failed. This can also be <code>true</code> if the test case is ignored; in this case, the
	 * status will be <code>IGNORED</code> , but as the test may still have been executed (e.g. AludraTest does this), this flag
	 * here can be used to determine the execution success.
	 * 
	 * @return <code>true</code> if the test case execution was unsuccessful, <code>false</code> otherwise.
	 */
	boolean isFailed();

	String getIgnoredReason();

	List<? extends TestStepGroupLog> getTestStepGroups();

	TestStepLog getLastFailedStep();

	int getNumberOfTestSteps();

}
