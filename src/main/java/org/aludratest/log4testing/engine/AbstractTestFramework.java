package org.aludratest.log4testing.engine;

import java.util.ArrayList;
import java.util.List;

import org.aludratest.log4testing.TestCaseLog;
import org.aludratest.log4testing.TestSuiteLog;

/**
 * Convenience abstract base class, implementing the listener infrastructure. Calls {@link #hook()} when the first listener is
 * added, and {@link #unhook()} when the last listener is removed.
 * 
 * @author falbrech
 * 
 */
public abstract class AbstractTestFramework implements TestFramework {

	private List<TestFrameworkListener> listeners = new ArrayList<TestFrameworkListener>();

	@Override
	public final void addTestFrameworkListener(TestFrameworkListener listener) {
		if (!listeners.contains(listener)) {
			if (listeners.isEmpty()) {
				hook();
			}
			listeners.add(listener);
		}
	}

	@Override
	public final void removeTestFrameworkListener(TestFrameworkListener listener) {
		listeners.remove(listener);
		if (listeners.isEmpty()) {
			unhook();
		}
	}

	/**
	 * Hooks into the test framework implementation. Is called when the first test framework listener is added. Could be called
	 * multiple times for the same instance (after a call to {@link #unhook()} and adding a new test framework listener).
	 */
	protected abstract void hook();

	/**
	 * Unhooks from the test framework implementation. Is called when the last test framework listener is removed. Could be called
	 * multiple times for the same instance (after {@link #hook()} has been called again).
	 */
	protected abstract void unhook();

	protected void fireStartingTestProcess(TestSuiteLog rootSuite) {
		for (TestFrameworkListener listener : getListenersCopy()) {
			listener.startingTestProcess(rootSuite);
		}
	}

	protected void fireStartingTestSuite(TestSuiteLog suite) {
		for (TestFrameworkListener listener : getListenersCopy()) {
			listener.startingTestSuite(suite);
		}
	}

	protected void fireStartingTestCase(TestCaseLog testCase) {
		for (TestFrameworkListener listener : getListenersCopy()) {
			listener.startingTestCase(testCase);
		}
	}

	protected void fireFinishedTestProcess(TestSuiteLog rootSuite) {
		for (TestFrameworkListener listener : getListenersCopy()) {
			listener.finishedTestProcess(rootSuite);
		}
	}

	protected void fireFinishedTestSuite(TestSuiteLog suite) {
		for (TestFrameworkListener listener : getListenersCopy()) {
			listener.finishedTestSuite(suite);
		}
	}

	protected void fireFinishedTestCase(TestCaseLog testCase) {
		for (TestFrameworkListener listener : getListenersCopy()) {
			listener.finishedTestCase(testCase);
		}
	}

	private List<TestFrameworkListener> getListenersCopy() {
		return new ArrayList<TestFrameworkListener>(listeners);
	}

}
