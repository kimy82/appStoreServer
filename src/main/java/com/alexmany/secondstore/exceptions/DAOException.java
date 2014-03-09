package com.alexmany.secondstore.exceptions;

public class DAOException extends RuntimeException{

	private static final long	serialVersionUID	= 1L;

	public DAOException() {

	}

	public DAOException( String message ) {

		super(message);
	}

	public DAOException( Exception e, String message ) {

		super(message, e);
	}
}