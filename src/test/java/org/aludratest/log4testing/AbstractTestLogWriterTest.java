package org.aludratest.log4testing;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;

public abstract class AbstractTestLogWriterTest {

	private AtomicInteger objectId = new AtomicInteger();

	@Before
	public final void resetObjectId() {
		objectId.set(0);
	}

	protected final MockTestSuiteLog createSuite(String name) {
		return new MockTestSuiteLog(objectId.incrementAndGet(), name);
	}

	protected final MockTestSuiteLog createSuite(String name, MockTestSuiteLog parent) {
		MockTestSuiteLog suite = new MockTestSuiteLog(objectId.incrementAndGet(), name, parent);
		parent.addTestSuite(suite);
		return suite;
	}

	protected final MockTestCaseLog createTestCase(String name, MockTestSuiteLog parent) {
		MockTestCaseLog testCase = new MockTestCaseLog(objectId.incrementAndGet(), name, parent);
		parent.addTestCase(testCase);
		return testCase;
	}

	protected final MockTestStepGroupLog createStepGroup(String name, MockTestCaseLog parent) {
		MockTestStepGroupLog group = new MockTestStepGroupLog(objectId.incrementAndGet(), name, parent);
		parent.addTestStepGroup(group);
		return group;
	}

	protected final MockTestStepLog createStep(String command, TestStatus status, MockTestStepGroupLog parent) {
		MockTestStepLog step = new MockTestStepLog(objectId.incrementAndGet(), parent);
		step.setStatus(status);
		step.setCommand(command);
		parent.addTestStep(step);
		return step;
	}

}
