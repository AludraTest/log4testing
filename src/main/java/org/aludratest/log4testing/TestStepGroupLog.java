package org.aludratest.log4testing;


/**
 * Interface for test step group log elements. A Test Step Group holds 0 to n test steps which represent the "real" work. Test
 * Step Groups shall group multiple low-level steps like "click", "enterData" etc. to business-level logic like "Perform Login".
 * 
 * @author falbrech
 *
 */
public interface TestStepGroupLog extends NamedTestLogElement, TestStepLogContainer {

	/**
	 * Returns the test case log this test step group log belongs to.
	 * 
	 * @return The test case log this test step group log belongs to.
	 */
	TestCaseLog getParent();

}
