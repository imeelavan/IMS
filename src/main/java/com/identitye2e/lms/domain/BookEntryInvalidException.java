package com.identitye2e.lms.domain;

/**
 * This exception can be thrown when adding invalid book data
 */
public class BookEntryInvalidException extends RuntimeException {

	public BookEntryInvalidException() {
        super();
    }

    public BookEntryInvalidException(String message) {
        super(message);
    }

    public BookEntryInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

}
