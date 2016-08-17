package org.aludratest.log4testing.output;

public class LogWriterException extends Exception {

	private static final long serialVersionUID = 5051990265107513764L;

	public LogWriterException(String message, Throwable cause) {
		super(message, cause);
	}

	public LogWriterException(String message) {
		super(message);
	}

	public LogWriterException(Throwable cause) {
		super(cause);
	}

}
