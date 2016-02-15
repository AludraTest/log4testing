package org.aludratest.log4testing.config;

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
