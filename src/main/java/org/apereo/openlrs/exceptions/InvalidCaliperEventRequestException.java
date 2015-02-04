package org.apereo.openlrs.exceptions;


/**
 * Exception indicating that a received caliper event request was invalid.
 * @author steve cody, scody@unicon.net
 */
public class InvalidCaliperEventRequestException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidCaliperEventRequestException() {
	}

	public InvalidCaliperEventRequestException(String message) {
		super(message);
	}

	public InvalidCaliperEventRequestException(Throwable cause) {
		super(cause);
	}

	public InvalidCaliperEventRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidCaliperEventRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
