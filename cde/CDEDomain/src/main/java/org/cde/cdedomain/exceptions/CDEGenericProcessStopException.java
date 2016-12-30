/**
 * 
 */
package org.cde.cdedomain.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CDEGenericProcessStopException extends Exception {


	private static final long serialVersionUID = -1561850099330883576L;
	private String message;
	private Throwable throwable;

    private String stackTraceString;
    
	public String getStackTraceString() {
		return stackTraceString;
	}

	public void setStackTraceString(String stackTraceString) {
		this.stackTraceString = stackTraceString;
	}

	public CDEGenericProcessStopException() {

	}

	static public String generateStackTraceString(Throwable t) {
        StringWriter s = new StringWriter();
        t.printStackTrace(new PrintWriter(s));
        return s.toString();
    }
	
	public CDEGenericProcessStopException(String message, Throwable throwable) {
		super(message, throwable);
		generateStackTraceString(throwable);
		this.message = message;
		this.throwable = throwable;
		
	}

	public CDEGenericProcessStopException(Throwable throwable) {
		super(throwable);
		this.throwable = throwable;
	}

	public CDEGenericProcessStopException(String message) {
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
