package com.alexmany.secondstore.exceptions;

public class UtilsException extends RuntimeException{

	private static final long	serialVersionUID	= 1L;

	public UtilsException() {

	}

	public UtilsException( String message ) {

		super(message);
	}

	public UtilsException( Exception e, String message ) {

		super(message, e);
	}
}