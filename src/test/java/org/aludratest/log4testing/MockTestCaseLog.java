package org.aludratest.log4testing;

import java.util.ArrayList;
import java.util.List;

public class MockTestCaseLog extends MockNamedTestLogElement implements TestCaseLog {

	private TestSuiteLog parent;

	private boolean ignored;

	private String ignoredReason;

	private List<TestStepGroupLog> testStepGroups = new ArrayList<TestStepGroupLog>();

	public MockTestCaseLog(int id, String name, TestSuiteLog parent) {
		super(id, name);
		this.parent = parent;
	}

	@Override
	public TestSuiteLog getParent() {
		return parent;
	}

	@Override
	public boolean isIgnored() {
		return ignored;
	}

	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}

	@Override
	public boolean isFailed() {
		return getLastFailedStep() != null;
	}

	@Override
	public String getIgnoredReason() {
		return ignoredReason;
	}

	public void setIgnoredReason(String ignoredReason) {
		this.ignoredReason = ignoredReason;
	}

	@Override
	public List<TestStepGroupLog> getTestStepGroups() {
		return testStepGroups;
	}

	public void addTestStepGroup(TestStepGroupLog group) {
		testStepGroups.add(group);
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
