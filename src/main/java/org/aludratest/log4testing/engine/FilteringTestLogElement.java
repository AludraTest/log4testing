package org.aludratest.log4testing.engine;

import org.aludratest.log4testing.TestLogElement;
import org.aludratest.log4testing.TestStatus;
import org.joda.time.DateTime;
import org.joda.time.Duration;

public abstract class FilteringTestLogElement<T extends TestLogElement> implements TestLogElement {
	
	private T delegate;
	
	private LogContext logContext;

	protected FilteringTestLogElement(T delegate, LogContext logContext) {
		if (delegate == null) {
			throw new IllegalArgumentException("delegate must not be null");
		}
		if (logContext == null) {
			throw new IllegalArgumentException("logContext must not be null");
		}
		this.delegate = delegate;
		this.logContext = logContext;
	}

	protected final T getDelegate() {
		return delegate;
	}

	protected final LogContext getLogContext() {
		return logContext;
	}

	@Override
	public final int getId() {
		return delegate.getId();
	}

	@Override
	public final DateTime getStartTime() {
		return delegate.getStartTime();
	}

	@Override
	public final DateTime getEndTime() {
		return delegate.getEndTime();
	}

	@Override
	public final Duration getDuration() {
		return delegate.getDuration();
	}

	@Override
	public final Duration getWork() {
		return delegate.getWork();
	}

	@Override
	public final TestStatus getStatus() {
		return delegate.getStatus();
	}

	@Override
	public String toString() {
		return delegate.toString();
	}
}
