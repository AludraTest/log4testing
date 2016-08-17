package org.aludratest.log4testing;

import org.aludratest.log4testing.NamedTestLogElement;
import org.aludratest.log4testing.TestStatus;
import org.joda.time.DateTime;
import org.joda.time.Duration;

public abstract class MockNamedTestLogElement implements NamedTestLogElement {

	private int id;

	private String name;

	private DateTime startTime;

	private DateTime endTime;

	private Duration duration;

	private TestStatus status = TestStatus.PENDING;

	private Duration work;

	public MockNamedTestLogElement(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public DateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	@Override
	public DateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}

	@Override
	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	@Override
	public TestStatus getStatus() {
		return status;
	}

	public void setStatus(TestStatus status) {
		this.status = status;
	}

	@Override
	public Duration getWork() {
		return work;
	}

	public void setWork(Duration work) {
		this.work = work;
	}

}
