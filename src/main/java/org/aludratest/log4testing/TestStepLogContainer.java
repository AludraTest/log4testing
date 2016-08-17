package org.aludratest.log4testing;

import java.util.List;

public interface TestStepLogContainer extends TestLogElement {

	List<? extends TestStepLog> getTestSteps();

}
