package org.aludratest.log4testing.util;

import java.util.EnumMap;
import java.util.Map;

import org.aludratest.log4testing.TestCaseLog;
import org.aludratest.log4testing.TestSuiteLog;
import org.aludratest.log4testing.TestSuiteStatistics;

/**
 * Default implementation of Test Suite Statistics interface. Offers a static factory method which gathers the statistics for a
 * given Test Suite Log and caches the results.
 * 
 * @author falbrech
 * 
 */
public final class DefaultTestSuiteStatistics implements TestSuiteStatistics {

	private static enum MeasureKey {
		TEST_CASES, CHILD_SUITES, PASSED, FAILED_TOTAL, IGNORED, FUNCTIONAL_FAILURE, FAILED_ACCESS, FAILED_PERFORMANCE, FAILED_AUTOMATION, INCONCLUSIVE, IGNORED_PASSED, IGNORED_FAILED;
	}

	private Map<MeasureKey, Integer> measures = new EnumMap<MeasureKey, Integer>(MeasureKey.class);

	public static TestSuiteStatistics create(TestSuiteLog suite) {
		return new DefaultTestSuiteStatistics(suite);
	}

	private DefaultTestSuiteStatistics(TestSuiteLog suite) {
		gather(suite, false);
	}

	private void gather(TestSuiteLog suite, boolean isChild) {
		if (isChild) {
			inc(MeasureKey.CHILD_SUITES);
		}

		for (TestCaseLog log : suite.getTestCases()) {
			inc(MeasureKey.TEST_CASES);
			if (log.isIgnored()) {
				inc(MeasureKey.IGNORED);
				if (log.getLastFailedStep() != null) {
					inc(MeasureKey.IGNORED_FAILED);
				}
				else {
					inc(MeasureKey.IGNORED_PASSED);
				}
			}
			else {
				if (log.getStatus().isFailure()) {
					inc(MeasureKey.FAILED_TOTAL);
				}

				switch (log.getStatus()) {
					case FAILED:
						inc(MeasureKey.FUNCTIONAL_FAILURE);
						break;
					case FAILEDACCESS:
						inc(MeasureKey.FAILED_ACCESS);
						break;
					case FAILEDAUTOMATION:
						inc(MeasureKey.FAILED_AUTOMATION);
						break;
					case FAILEDPERFORMANCE:
						inc(MeasureKey.FAILED_PERFORMANCE);
						break;
					case IGNORED:
						break;
					case INCONCLUSIVE:
						inc(MeasureKey.INCONCLUSIVE);
						break;
					case PASSED:
						inc(MeasureKey.PASSED);
						break;
					case PENDING:
						break;
					case RUNNING:
						break;
					default:
						break;
				}
			}
		}

		for (TestSuiteLog child : suite.getChildSuites()) {
			gather(child, true);
		}
	}

	private void inc(MeasureKey measureKey) {
		Integer i = measures.get(measureKey);
		if (i == null) {
			i = Integer.valueOf(1);
		}
		else {
			i = Integer.valueOf(i.intValue() + 1);
		}
		measures.put(measureKey, i);
	}

	private int get(MeasureKey measureKey) {
		Integer i = measures.get(measureKey);
		return i == null ? 0 : i.intValue();
	}

	@Override
	public int getNumberOfTestCases() {
		return get(MeasureKey.TEST_CASES);
	}

	@Override
	public int getNumberOfChildSuites() {
		return get(MeasureKey.CHILD_SUITES);
	}

	@Override
	public int getNumberOfPassedTestCases() {
		return get(MeasureKey.PASSED);
	}

	@Override
	public int getNumberOfFailedTestCases() {
		return get(MeasureKey.FAILED_TOTAL);
	}

	@Override
	public int getNumberOfIgnoredTestCases() {
		return get(MeasureKey.IGNORED);
	}

	@Override
	public int getNumberOfFunctionallyFailedTestCases() {
		return get(MeasureKey.FUNCTIONAL_FAILURE);
	}

	@Override
	public int getNumberOfFailedAccessTestCases() {
		return get(MeasureKey.FAILED_ACCESS);
	}

	@Override
	public int getNumberOfFailedPerformanceTestCases() {
		return get(MeasureKey.FAILED_PERFORMANCE);
	}

	@Override
	public int getNumberOfAutomationFailedTestCases() {
		return get(MeasureKey.FAILED_AUTOMATION);
	}

	@Override
	public int getNumberOfInconclusiveTestCases() {
		return get(MeasureKey.INCONCLUSIVE);
	}

	@Override
	public int getNumberOfIgnoredAndPassedTestCases() {
		return get(MeasureKey.IGNORED_PASSED);
	}

	@Override
	public int getNumberOfIgnoredAndFailedTestCases() {
		return get(MeasureKey.IGNORED_FAILED);
	}

}
