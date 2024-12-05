package com.identitye2e.lms.application;

import com.identitye2e.lms.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class Library {
	
	private final BookRepository bookRepository;

	public Library(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	/**
	 * It adds book to the ConcurrentHashMap via bookRepository.
	 *
	 * @param book contains the attributes of a book.
	 * @return attributes of a book as book object
	 * @throws BookEntryInvalidException if invalid data entered.
	 * @throws BookAlreadyExistException if book being added exists already.
	 */
	public Book addBook(Book book) {
		//domain validation
		if(!validateBookEntry(book)){
			throw new BookEntryInvalidException("Invalid book entry, please check the payload");
		}
		//check if book already exists
		Optional<Book> existingBook = bookRepository.findBookByISBN(book.getIsbn());
		/*If book exists, throws BookAlreadyExistException, however the ideal scenario would be to increase the count
		  by the available copies that we tried to add but it is out of scope for this exercise.
		 */
		if (existingBook.isPresent()) {
			throw new BookAlreadyExistException("Book already exists");
		}
		return bookRepository.addBook(book);
	}

	/**
	 * It removes book from the ConcurrentHashMap via bookRepository.
	 *
	 * @param isbn is the isbn of a book.
	 * @return attributes of a book as book object
	 * @throws BookNotFoundException if no book found for the given isbn.
	 */
	public Book removeBook(String isbn) {
		return bookRepository.removeBook(isbn).orElseThrow(() ->new BookNotFoundException("Book not found"));
	}

	/**
	 * It returns book from the ConcurrentHashMap via bookRepository.
	 *
	 * @param isbn is the isbn of a book.
	 * @return attributes of a book as book object
	 * @throws BookNotFoundException if no book found for a given isbn.
	 */
	public Book findBookByISBN(String isbn) {
		return bookRepository.findBookByISBN(isbn).orElseThrow(() ->new BookNotFoundException("Book not found"));
	}

	/**
	 * It returns a list of book from the ConcurrentHashMap via bookRepository.
	 *
	 * @param author is the author of a book.
	 * @return list of books written by an author
	 */
	public List<Book> findBooksByAuthor(String author) {
		return bookRepository.findBooksByAuthor(author);
	}

	/**
	 * It update availability copies of a book from the ConcurrentHashMap via bookRepository when borrowing.
	 *
	 * @param isbn is the isbn of a book.
	 * @return attributes of a book as book object
	 * @throws BookNotFoundException if no book found for a given isbn.
	 * @throws BookNotAvailableException if no copies of a book available for a given isbn.
	 */
	public Book borrowBook(String isbn) {
		Book book = bookRepository.findBookByISBN(isbn).orElseThrow(()->new BookNotFoundException("Book not found"));

		if(book.getAvailableCopies() < 1){
			throw new BookNotAvailableException("No available copies");
		}
		return bookRepository.updateBookAvailabilityCopies(book, book.getAvailableCopies() - 1);
	}

	/**
	 * It update availability copies of a book from the ConcurrentHashMap via bookRepository when returning.
	 *
	 * @param isbn is the isbn of a book.
	 * @return attributes of a book as book object
	 * @throws BookNotFoundException if no book found for a given isbn.
	 */
	public Book returnBook(String isbn) {
		Book book = bookRepository.findBookByISBN(isbn).orElseThrow(()->new BookNotFoundException("Book not found"));
		return bookRepository.updateBookAvailabilityCopies(book, book.getAvailableCopies() + 1);
	}

	/**
	 * It validates the attributes of a book.
	 *
	 * @param book contains the attributes of a book..
	 * @return true if valid otherwise false
	 */
	private boolean validateBookEntry(Book book) {
		boolean valid = true;
		if(book == null){
			valid = false;
		}else{
			if(book.getAvailableCopies() < 1){
				valid = false;
			}
		}
		return valid;
	}
}
