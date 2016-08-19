package org.aludratest.log4testing.config;

/**
 * Signals that some parts of the Log4Testing configuration are invalid. This can only occur during Writer or Filter configuration
 * and is handled within the Log4Testing engine, which only logs appropriate warnings.
 * 
 * @author falbrech
 *
 */
public class InvalidConfigurationException extends Exception {

	private static final long serialVersionUID = -847067880622034762L;

	public InvalidConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidConfigurationException(String message) {
		super(message);
	}

	public InvalidConfigurationException(Throwable cause) {
		super(cause);
	}

}
