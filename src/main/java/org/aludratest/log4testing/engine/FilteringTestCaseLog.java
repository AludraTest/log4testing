package org.aludratest.log4testing.engine;

import java.util.AbstractList;
import java.util.List;

import org.aludratest.log4testing.TestCaseLog;
import org.aludratest.log4testing.TestStepGroupLog;
import org.aludratest.log4testing.TestStepLog;
import org.aludratest.log4testing.TestSuiteLog;

public class FilteringTestCaseLog extends FilteringNamedTestLogElement<TestCaseLog> implements TestCaseLog {

	public FilteringTestCaseLog(TestCaseLog delegate, LogContext logContext) {
		super(delegate, logContext);
	}

	@Override
	public TestSuiteLog getParent() {
		TestSuiteLog parent = getDelegate().getParent();
		return parent == null ? null : new FilteringTestSuiteLog(parent, getLogContext());
	}

	@Override
	public boolean isIgnored() {
		return getDelegate().isIgnored();
	}

	@Override
	public boolean isFailed() {
		return getDelegate().isFailed();
	}

	@Override
	public String getIgnoredReason() {
		return getDelegate().getIgnoredReason();
	}

	@Override
	public List<TestStepGroupLog> getTestStepGroups() {
		return new FilteringTestStepGroupLogList();
	}

	private class FilteringTestStepGroupLogList extends AbstractList<TestStepGroupLog> {

		@Override
		public TestStepGroupLog get(int index) {
			TestStepGroupLog log = getDelegate().getTestStepGroups().get(index);
			return log == null ? null : new FilteringTestStepGroupLog(log, getLogContext());
		}

		@Override
		public int size() {
			return getDelegate().getTestStepGroups().size();
		}

	}

	@Override
	public int getNumberOfTestSteps() {
		int counter = 0;
		for (TestStepGroupLog group : getTestStepGroups()) {
			counter += group.getTestSteps().size();
		}
		return counter;
	}

	@Override
	public TestStepLog getLastFailedStep() {
		TestStepLog lastFailed = null;
		for (TestStepGroupLog group : getTestStepGroups()) {
			for (TestStepLog step : group.getTestSteps()) {
				if (step.getStatus() != null && step.getStatus().isFailure()) {
					lastFailed = step;
				}
			}
		}

		return lastFailed;
	}

}
