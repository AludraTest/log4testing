package org.aludratest.log4testing;

import java.util.List;

/**
 * Interface for test log elements containing a list of test step logs. This is the common interface of {@link TestStepGroupLog},
 * the "natural" parent of test steps, and {@link TestStepLog} itself, for the recursive nature defined by the API.
 * 
 * @author falbrech
 *
 */
public interface TestStepLogContainer extends TestLogElement {

	/**
	 * Returns the list of test step logs contained in this test log element.
	 * 
	 * @return The list of test step logs contained in this test log element, maybe an empty list, but never <code>null</code>.
	 */
	List<? extends TestStepLog> getTestSteps();

}
