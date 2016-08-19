package org.aludratest.log4testing.engine;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import org.aludratest.log4testing.TestCaseLog;
import org.aludratest.log4testing.TestStepFilter;
import org.aludratest.log4testing.TestStepGroupLog;
import org.aludratest.log4testing.TestStepLog;

class FilteringTestStepGroupLog extends FilteringNamedTestLogElement<TestStepGroupLog> implements TestStepGroupLog {

	public FilteringTestStepGroupLog(TestStepGroupLog delegate, LogContext logContext) {
		super(delegate, logContext);
	}

	@Override
	public List<TestStepLog> getTestSteps() {
		return new FilteringTestStepLogList();
	}

	@Override
	public TestCaseLog getParent() {
		return new FilteringTestCaseLog(getDelegate().getParent(), getLogContext());
	}

	private class FilteringTestStepLogList extends AbstractList<TestStepLog> {

		private List<TestStepLog> filteredList;

		@Override
		public TestStepLog get(int index) {
			assertFiltered();
			return filteredList.get(index);
		}

		@Override
		public int size() {
			assertFiltered();
			return filteredList.size();
		}

		private void assertFiltered() {
			if (filteredList == null) {
				filteredList = new ArrayList<TestStepLog>();

				for (TestStepLog step : getDelegate().getTestSteps()) {
					boolean add = true;
					for (TestStepFilter filter : getLogContext().getFilters()) {
						if (!filter.filter(step)) {
							add = false;
							break;
						}
					}

					if (add) {
						filteredList.add(new FilteringTestStepLog(step, getLogContext()));
					}
				}
			}
		}
	}

}
