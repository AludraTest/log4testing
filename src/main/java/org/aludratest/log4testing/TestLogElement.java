package org.aludratest.log4testing;

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * Base parent interface for elements which can be logged in a test log. Defines common methods to be provided by all log
 * elements.
 * 
 * @author falbrech
 *
 */
public interface TestLogElement {

	/**
	 * Returns the ID of this test log element. This ID <b>must</b> be unique throughout the current test execution and log object
	 * type (e.g. suite, test case, test step, test step group), and <b>should</b> be unique throughout current test execution
	 * over <b>all</b> object types. <br>
	 * The ID should not be presented to the average log reader - i.e. it does not have the same quality like the name of a test
	 * log element. But it is completely fine to use this ID e.g. in HTML links or for file generation logic.
	 * 
	 * @return ID of this test log element.
	 */
	int getId();

	/**
	 * Returns date and time when this test log element started. May be <code>null</code> if the element has not started yet.
	 * 
	 * @return Date and time when this test log element started, or <code>null</code>.
	 */
	DateTime getStartTime();

	/**
	 * Returns date and time when this test log element finished. May be <code>null</code> if the element has not finished yet.
	 * 
	 * @return Date and time when this test log element finished, or <code>null</code>.
	 */
	DateTime getEndTime();

	/**
	 * Returns the total duration of this test log element. This is defined as <code>endTime - startTime</code>. May be
	 * <code>null</code> if the element has not finished yet.
	 * 
	 * @return The total duration of this test log element, or <code>null</code>.
	 */
	Duration getDuration();

	/**
	 * Returns the total work of this test log element. This is defined as sum of durations of all steps belonging to this test
	 * log element (so is equal to duration for single Test Steps). This can be much higher than duration if tests of a Test Suite
	 * are executed in parallel. <br>
	 * If the element has not finished yet, it is OK to return <code>null</code>, but it is recommended to sum up all work of all
	 * subelements having finished yet.
	 * 
	 * @return The total work of this test log element.
	 */
	Duration getWork();

	/**
	 * Returns the status of this test log element. For single test steps, this is an atomic information. Parent elements must
	 * check the status values of their children and determine their status based on the most "severe" child status. This is not
	 * trivial and may partly depend on Test Framework logic. Some recommended logic elements:
	 * <ul>
	 * <li>If all child elements have the same status, this must also be the status of this element.</li>
	 * <li>If at least one child element has a status with <code>isFailure()</code> being <code>true</code>, one of these failing
	 * statuses must be the status of this element.</li>
	 * <li>If a test case log is a child of this log element and has the <code>ignored</code> flag set, its status should not be
	 * used for status calculation. This implies that, if all test case logs under this element are ignored, the status should be
	 * {@link TestStatus#PASSED}.</li>
	 * </ul>
	 * 
	 * @return The status of this test log element, never <code>null</code>.
	 */
	TestStatus getStatus();

}
