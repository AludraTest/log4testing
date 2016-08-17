package org.aludratest.log4testing;

import java.util.ArrayList;
import java.util.List;

import org.aludratest.log4testing.TestCaseLog;
import org.aludratest.log4testing.TestStatus;
import org.aludratest.log4testing.TestSuiteLog;
import org.aludratest.log4testing.TestSuiteStatistics;
import org.aludratest.log4testing.util.DefaultTestSuiteStatistics;

public class MockTestSuiteLog extends MockNamedTestLogElement implements TestSuiteLog {

	private TestSuiteLog parent;

	private List<TestSuiteLog> childSuites = new ArrayList<TestSuiteLog>();

	private List<TestCaseLog> testCases = new ArrayList<TestCaseLog>();

	public MockTestSuiteLog(int id, String name) {
		super(id, name);
	}

	public MockTestSuiteLog(int id, String name, TestSuiteLog parent) {
		super(id, name);
		this.parent = parent;
	}

	@Override
	public TestSuiteLog getParent() {
		return parent;
	}

	@Override
	public List<? extends TestSuiteLog> getChildSuites() {
		return childSuites;
	}

	@Override
	public List<? extends TestCaseLog> getTestCases() {
		return testCases;
	}

	@Override
	public TestSuiteStatistics gatherStatistics() {
		return DefaultTestSuiteStatistics.create(this);
	}

	public void addTestSuite(TestSuiteLog suite) {
		childSuites.add(suite);
	}

	public void addTestCase(TestCaseLog testCase) {
		testCases.add(testCase);
	}

	@Override
	public TestStatus getStatus() {
		TestStatus status = TestStatus.PASSED;
		for (TestCaseLog testCase : testCases) {
			if (testCase.getStatus().ordinal() < status.ordinal()) {
				status = testCase.getStatus();
			}
		}

		return status;
	}
}
