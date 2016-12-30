package org.cde.cdedomain.exceptions;

public class AuthFailException extends DataException {

	private static final long serialVersionUID = 1L; 

	public AuthFailException(String message) {
		super(message);

	}

	public AuthFailException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
