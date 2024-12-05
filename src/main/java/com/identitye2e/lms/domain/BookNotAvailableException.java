package com.identitye2e.lms.domain;

/**
 * This exception can be thrown when there is no copies of a book available to borrow
 */
public class BookNotAvailableException extends RuntimeException {

	public BookNotAvailableException() {
        super();
    }

    public BookNotAvailableException(String message) {
        super(message);
    }

    public BookNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

}
