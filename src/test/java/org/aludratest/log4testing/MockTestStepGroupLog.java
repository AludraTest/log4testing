package org.aludratest.log4testing;

import java.util.ArrayList;
import java.util.List;

import org.aludratest.log4testing.TestCaseLog;
import org.aludratest.log4testing.TestStatus;
import org.aludratest.log4testing.TestStepGroupLog;
import org.aludratest.log4testing.TestStepLog;

public class MockTestStepGroupLog extends MockNamedTestLogElement implements TestStepGroupLog {

	private TestCaseLog parent;

	private List<TestStepLog> steps = new ArrayList<TestStepLog>();

	public MockTestStepGroupLog(int id, String name, TestCaseLog parent) {
		super(id, name);
		this.parent = parent;
	}

	@Override
	public List<TestStepLog> getTestSteps() {
		return steps;
	}

	public void addTestStep(TestStepLog step) {
		steps.add(step);
	}

	@Override
	public TestCaseLog getParent() {
		return parent;
	}

	@Override
	public TestStatus getStatus() {
		TestStatus status = TestStatus.PASSED;
		for (TestStepLog step : steps) {
			if (step.getStatus().ordinal() < status.ordinal()) {
				status = step.getStatus();
			}
		}

		return status;
	}

}
