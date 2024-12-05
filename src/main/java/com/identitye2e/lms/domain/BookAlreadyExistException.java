package com.identitye2e.lms.domain;

/**
 * This exception can be thrown when adding an existing book to library
 */
public class BookAlreadyExistException extends RuntimeException {

	public BookAlreadyExistException() {
        super();
    }

    public BookAlreadyExistException(String message) {
        super(message);
    }

    public BookAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

}
