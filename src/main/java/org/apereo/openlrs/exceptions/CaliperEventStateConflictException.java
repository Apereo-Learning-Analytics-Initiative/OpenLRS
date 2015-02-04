package org.apereo.openlrs.exceptions;


/**
 * Exception indicating that a received Caliper Event request cannot be fulfilled due to a conflict with the state of the target statement.
 * @author steve cody, scody@unicon.net
 */
public class CaliperEventStateConflictException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public CaliperEventStateConflictException() {
	}

	public CaliperEventStateConflictException(String message) {
		super(message);
	}

	public CaliperEventStateConflictException(Throwable cause) {
		super(cause);
	}

	public CaliperEventStateConflictException(String message, Throwable cause) {
		super(message, cause);
	}

	public CaliperEventStateConflictException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
