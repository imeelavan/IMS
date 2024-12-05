package com.identitye2e.lms.domain;

/**
 * This exception can be thrown when there is no book to remove or find, eg trying to find a book with wrong/invalid ISBN
 */
public class BookNotFoundException extends RuntimeException {

	public BookNotFoundException() {
        super();
    }

    public BookNotFoundException(String message) {
        super(message);
    }

    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
