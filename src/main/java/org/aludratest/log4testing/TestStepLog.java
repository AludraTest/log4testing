package org.aludratest.log4testing;

import java.util.List;

/**
 * Interface for test step log elements. A test step is the most low-level element in the log element hierarchy. It represents a
 * real performed action, usually against a System Under Test, including many details. Some Test Frameworks may not offer
 * information on this low level at all, which is totally fine. For these frameworks, <code>getTestSteps()</code> for a test step
 * group, or even <code>getTestStepGroups()</code> for a test case, may always return empty lists. <br>
 * According to the API in this interface, test steps may contain sub-steps, making this a recursive structure. But currently,
 * there is no known implementor of this interface offering such sub-steps (not even AludraTest). Nevertheless, this would be
 * fully supported by Log4Testing.
 * 
 * @author falbrech
 *
 */
public interface TestStepLog extends TestStepLogContainer {

	/**
	 * Returns the starting offset of this test step, in seconds, relative to the start of the <b>parent test case</b> (not the
	 * test step group!).
	 * 
	 * @return The starting offset of this test step, in seconds.
	 */
	int getStartTimeOffsetSeconds();

	/**
	 * Returns the parent of this test step log element. For normal test steps, this would be a {@link TestStepGroupLog}. For
	 * sub-steps (see class Javadoc), this would be a <code>TestStepLog</code>.
	 * 
	 * @return The parent of this test step log element, never <code>null</code>.
	 */
	TestStepLogContainer getParent();

	/**
	 * Returns the <code>command</code> attribute of this test step. Usually, this is the name of a method called in an interface,
	 * or the name of a command sent via a communication channel.
	 * 
	 * @return The <code>command</code> attribute of this test step, maybe <code>null</code> (but not recommended).
	 */
	String getCommand();

	/**
	 * Returns the <code>service</code> attribute of this test step. This could be some identifier specifying the context of the
	 * <code>command</code> attribute, but may as well be left empty.
	 * 
	 * @return The <code>service</code> attribute of this test step, or <code>null</code>.
	 */
	String getService();

	/**
	 * Returns the <code>elementType</code> attribute of this test step. This describes the family of the element the test step is
	 * interacting with, e.g. "file" for file based tests, or "button" for UI tests. There is no convention on the contents of
	 * this attribute, so it can be freely defined by the Test Framework what to fill in here. Leaving this empty when it is not
	 * applicable to the used Test Framework is also totally fine.
	 * 
	 * @return The <code>elementType</code> attribute of this test step, or <code>null</code>.
	 */
	String getElementType();

	/**
	 * Returns the <code>elementName</code> attribute of this test step. This names the element the test step is interacting with,
	 * e.g. "Uploaded File" for file based tests, or "LoginButton" for UI tests. There is no convention on the contents of this
	 * attribute, so it can be freely defined by the Test Framework what to fill in here. Especially, this should not be a
	 * technical name like a component ID, as this fits better in the <code>technicalLocator</code> attribute. Leaving this empty
	 * when it is not applicable to the used Test Framework is also totally fine.
	 * 
	 * @return The <code>elementName</code> attribute of this test step, or <code>null</code>.
	 */
	String getElementName();

	/**
	 * Returns the <code>technicalLocator</code> attribute of this test step. This should be a unique locator to the element the
	 * test step is interacting with, e.g. the name of a file for file based tests, or a component ID or XPath for UI tests.
	 * Leaving this empty when it is not applicable to the used Test Framework is also totally fine.
	 * 
	 * @return The <code>technicalLocator</code> attribute of this test step, or <code>null</code>.
	 */
	String getTechnicalLocator();

	/**
	 * Returns the <code>technicalArguments</code> attribute of this test step. Technical arguments are arguments controlling the
	 * test flow, e.g. timeout values, contrary to the <code>usedArguments</code> attribute, which usually contains test data
	 * parameters like the text to enter. It is up to the Test Framework how to list multiple arguments in this field - usually,
	 * one would comma-separate them.
	 * 
	 * @return The <code>technicalArguments</code> attribute of this test step, or <code>null</code>.
	 */
	String getTechnicalArguments();

	/**
	 * Returns the <code>usedArguments</code> attribute of this test step. These are all arguments important for the test logic of
	 * the test step, usually some test data elements like the text to enter. It is up to the Test Framework how to list multiple
	 * arguments in this field - usually, one would comma-separate them.
	 * 
	 * @return The <code>usedArguments</code> attribute of this test step, or <code>null</code>.
	 */
	String getUsedArguments();

	/**
	 * Returns the <code>result</code> attribute of this test step - usually a String representation of the return value of the
	 * called method. Content depends on Test Framework logic.
	 * 
	 * @return The <code>result</code> attribute of this test step, or <code>null</code>.
	 */
	String getResult();

	/**
	 * Returns the <code>errorMessage</code> attribute of this test step. This is usually only filled for test steps with a failed
	 * test status. The error message should describe why the test step failed - <b>not</b> be an error message of the System
	 * under Test itself (example: "Unexpected error popup appeared" would be fine - but the contents of the error popup should
	 * better go into an attachment of the step log).
	 * 
	 * @return The <code>errorMessage</code> attribute of this test step, or <code>null</code>.
	 */
	String getErrorMessage();

	/**
	 * Returns the exception which caused this test step to fail. This must be an exception from within the Test Framework (e.g.
	 * an AssertionFailedException, if such is defined by the Test Framework), not from within the System Unter Test. It usually
	 * should correspond to the <code>errorMessage</code> attribute.
	 * 
	 * @return The exception which caused this test step to fail, or <code>null</code>.
	 */
	Throwable getError();

	/**
	 * Returns the <code>comment</code> attribute of this test step. This can be used for any additional attributions for this
	 * test step supported by the Test Framework.
	 * 
	 * @return The <code>comment</code> attribute of this step, or <code>null</code>.
	 */
	String getComment();

	/**
	 * Returns all attachments attached to this test step.
	 * 
	 * @return All attachments attached to this step, maybe an empty list, but never <code>null</code>.
	 */
	List<? extends AttachmentLog> getAttachments();

}
