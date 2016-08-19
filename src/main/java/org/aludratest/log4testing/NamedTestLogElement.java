package org.aludratest.log4testing;

/**
 * Interface for test log elements providing a name.
 * 
 * @author falbrech
 *
 */
public interface NamedTestLogElement extends TestLogElement {

	/**
	 * Returns the name of this test log element. This name may be used for displaying (logging) purposes.
	 * 
	 * @return The name of this test log element. <code>null</code> and empty strings are highly discouraged for use, but must be
	 *         handled by all consumers (at least by ignoring this element).
	 */
	String getName();

}
