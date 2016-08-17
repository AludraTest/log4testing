package org.aludratest.log4testing;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public interface TestLogElement {

	int getId();

	DateTime getStartTime();

	DateTime getEndTime();

	Duration getDuration();

	Duration getWork();

	TestStatus getStatus();

}
