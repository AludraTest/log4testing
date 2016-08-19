package org.aludratest.log4testing.engine;

import java.util.AbstractList;
import java.util.List;

import org.aludratest.log4testing.TestCaseLog;
import org.aludratest.log4testing.TestSuiteLog;
import org.aludratest.log4testing.TestSuiteStatistics;

class FilteringTestSuiteLog extends FilteringNamedTestLogElement<TestSuiteLog> implements TestSuiteLog {

	public FilteringTestSuiteLog(TestSuiteLog delegate, LogContext logContext) {
		super(delegate, logContext);
	}

	@Override
	public TestSuiteLog getParent() {
		TestSuiteLog parent = getDelegate().getParent();
		return parent == null ? null : new FilteringTestSuiteLog(parent, getLogContext());
	}

	@Override
	public List<? extends TestSuiteLog> getChildSuites() {
		return new AbbreviatingTestSuitesList();
	}


	@Override
	public List<? extends TestCaseLog> getTestCases() {
		return new AbbreviatingTestCasesList();
	}

	@Override
	public TestSuiteStatistics gatherStatistics() {
		// by definition, statistics of delegate must be equivalent (same number of test cases, only steps could be filtered)
		return getDelegate().gatherStatistics();
	}

	private class AbbreviatingTestSuitesList extends AbstractList<TestSuiteLog> {

		@Override
		public TestSuiteLog get(int index) {
			return new FilteringTestSuiteLog(getDelegate().getChildSuites().get(index), getLogContext());
		}

		@Override
		public int size() {
			return getDelegate().getChildSuites().size();
		}

	}

	private class AbbreviatingTestCasesList extends AbstractList<TestCaseLog> {

		@Override
		public TestCaseLog get(int index) {
			return new FilteringTestCaseLog(getDelegate().getTestCases().get(index), getLogContext());
		}

		@Override
		public int size() {
			return getDelegate().getTestCases().size();
		}

	}
}
