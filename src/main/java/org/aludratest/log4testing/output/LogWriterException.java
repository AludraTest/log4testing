package org.aludratest.log4testing.output;

/**
 * Exception which can be thrown by a {@link TestLogWriter} to signal any problem when writing test logs. Usually, the exception
 * should wrap a root exception like an <code>IOException</code>, <code>SQLException</code> or similar.
 * 
 * @author falbrech
 *
 */
public class LogWriterException extends Exception {

	private static final long serialVersionUID = 5051990265107513764L;

	/**
	 * Creates a new exception with given message and cause.
	 * 
	 * @param message
	 *            Exception message.
	 * @param cause
	 *            Exception which caused this exception.
	 */
	public LogWriterException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new exception with given message.
	 * 
	 * @param message
	 *            Exception message.
	 */
	public LogWriterException(String message) {
		super(message);
	}

	/**
	 * Creates a new exception with given cause.
	 * 
	 * @param cause
	 *            Exception which caused this exception.
	 */
	public LogWriterException(Throwable cause) {
		super(cause);
	}

}
