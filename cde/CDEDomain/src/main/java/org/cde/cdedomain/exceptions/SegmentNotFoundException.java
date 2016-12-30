package org.cde.cdedomain.exceptions;


public class SegmentNotFoundException extends Exception {

	private static final long serialVersionUID = 3432523L;
	private String message_;
	private Throwable throwable_;
    private String stackTraceString_;
    
    
	public String getStackTraceString() {
		return stackTraceString_;
	}
	public void setStackTraceString(String stackTraceString) {
		this.stackTraceString_ = stackTraceString;
	}
	public String getMessage() {
		return message_;
	}
	public void setMessage(String message) {
		this.message_ = message;
	}
	public Throwable getThrowable() {
		return throwable_;
	}
	public void setThrowable(Throwable throwable) {
		this.throwable_ = throwable;
	}


	public SegmentNotFoundException() {
	}

	public SegmentNotFoundException(String message) {
		super(message);
		this.message_ = message;
	}

	public SegmentNotFoundException(Throwable throwable) {
		super(throwable);
		this.throwable_ = throwable;
	}

	public SegmentNotFoundException(String message, Throwable throwable) {
		super(message, throwable);
		this.message_ = message;
		this.throwable_ = throwable;
	}


}
