package org.aludratest.log4testing;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public interface TestLogElement {

	public int getId();

	public DateTime getStartTime();

	public DateTime getEndTime();

	public Duration getDuration();

	public Duration getWork();

	public TestStatus getStatus();

}
