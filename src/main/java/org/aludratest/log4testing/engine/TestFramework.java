package org.aludratest.log4testing.engine;

/**
 * Interface for test frameworks. An implementation is responsible for hooking into the underlying test framework implementation
 * (e.g. JUnit or AludraTest), either on object construction or (recommended) when adding the first framework listener. The latter
 * allows the implementation to unhook when the last framework listener is removed, which avoids memory leaks. <br>
 * The implementation is also responsible for creating the test log objects and fill their properties appropriately.
 * 
 * @author falbrech
 * 
 */
public interface TestFramework {
	
	/**
	 * Adds the given listener to the list of listeners to be notified about Test Framework events. If an identical listener is
	 * already registered, this method must not add it again.
	 * 
	 * @param listener
	 *            Listener to add.
	 */
	void addTestFrameworkListener(TestFrameworkListener listener);

	/**
	 * Removes the given listener from the list of listeners. If no such listener is registered, this method does nothing.
	 * 
	 * @param listener
	 *            Listener to remove.
	 */
	void removeTestFrameworkListener(TestFrameworkListener listener);

}
