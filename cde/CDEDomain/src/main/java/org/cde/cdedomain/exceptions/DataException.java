package org.cde.cdedomain.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;


public class DataException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3248018250169413692L;
	private String message;
	private Throwable throwable;

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
	
	public DataException(String message, Throwable throwable) {		
		super(message, throwable);
		generateStackTraceString(throwable);
		this.message = message;
		this.throwable = throwable;
		
	}

	public DataException(Throwable throwable) {
		
		super(throwable);
		this.throwable = throwable;
	}

	public DataException(String message) {
		super(message);
		this.message = message;
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

}
