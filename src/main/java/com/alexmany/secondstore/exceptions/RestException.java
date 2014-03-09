package com.alexmany.secondstore.exceptions;

public class RestException extends RuntimeException{

	private static final long	serialVersionUID	= 1L;

	public RestException() {

	}

	public RestException( String message ) {

		super(message);
	}

	public RestException( Exception e, String message ) {

		super(message, e);
	}
}