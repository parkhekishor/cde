package org.cde.cdedomain.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CDEGenricException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1815307455133640191L;
	private String message;
	private Throwable throwable;
	private boolean businessException;
	private boolean technicalException;

	private String stackTraceString;

	public String getStackTraceString() {
		return stackTraceString;
	}

	public void setStackTraceString(String stackTraceString) {
		this.stackTraceString = stackTraceString;
	}

	static public String generateStackTraceString(Throwable t) {
		StringWriter s = new StringWriter();
		t.printStackTrace(new PrintWriter(s));
		return s.toString();
	}

	public CDEGenricException(String message, Throwable throwable) {
		super(message, throwable);
		this.message = message;
		this.throwable = throwable;

	}

	public CDEGenricException(Throwable throwable) {
		super(throwable);
		this.throwable = throwable;
	}

	public CDEGenricException(String message) {
		super(message);
		this.message = message;
	}

	public CDEGenricException(String message, Throwable throwable,
			boolean businessException, boolean technicalException) {
		this(message, throwable);
		this.businessException = businessException;
		this.technicalException = technicalException;
	}

	public CDEGenricException(String message, boolean businessException,
			boolean technicalException) {
		this(message);
		this.businessException = businessException;
		this.technicalException = technicalException;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public boolean isBusinessException() {
		return businessException;
	}

	public void setBusinessException(boolean businessException) {
		this.businessException = businessException;
	}

	public boolean isTechnicalException() {
		return technicalException;
	}

	public void setTechnicalException(boolean technicalException) {
		this.technicalException = technicalException;
	}

}
