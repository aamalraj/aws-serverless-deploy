package com.fantasyinc.gaming.votes.exception;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -4324665470564256178L;
	private final int errorCode;

	public ServiceException(int errorCode, final String message, final Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public ServiceException(int errorCode, final String message) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return errorCode;
	}

}