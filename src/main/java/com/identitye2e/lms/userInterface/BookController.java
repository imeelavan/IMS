package com.identitye2e.lms.userInterface;

import com.identitye2e.lms.application.Library;
import com.identitye2e.lms.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This is a rest controller responsible for functionalities of Concurrent Library Management System (LMS)
 * @author V.Pirabaharan
 */
@Slf4j
@RestController
public class BookController {

	private final Library library;

	public BookController(Library library) {
		this.library = library;
	}

	/**
	 * This is an endpoint that is responsible for adding book
	 * @param book contains the attributes of a book.
	 * @return ResponseEntity<String>
	 */
	@PostMapping("/book/add")
	public ResponseEntity<String> addBook(@RequestBody Book book) {
		try {
			Book savedBook = library.addBook(book);
			return ResponseEntity.ok(savedBook.toString());
		}catch (BookEntryInvalidException e) {
			log.error("", e);
			return ResponseEntity.badRequest().body("Invalid book entry, please check the payload");
		}catch (BookAlreadyExistException e) {
			log.error("", e);
			return ResponseEntity.badRequest().body("Book already exists");
		}catch (Exception e) {
			log.error("", e);
		}
		return ResponseEntity.internalServerError().body("Unexpected error occurred");
	}

	/**
	 * This is an endpoint that is responsible for removing book
	 * @param isbn is the isbn of a book.
	 * @return ResponseEntity<String>
	 */
	@DeleteMapping("/book/remove")
	public ResponseEntity<String> removeBook(@RequestParam String isbn) {
		try{
			Book returnedBook = library.removeBook(isbn);
			return ResponseEntity.ok(returnedBook.toString());
		}catch (BookNotFoundException e) {
			log.error("", e);
			return ResponseEntity.badRequest().body("Book not found");
		}catch (Exception e) {
			log.error("", e);
		}
		return ResponseEntity.internalServerError().body("Unexpected error occurred");
	}

	/**
	 * This is an endpoint that is responsible for finding book
	 * @param isbn is the isbn of a book.
	 * @param author is the author of a book.
	 * @return ResponseEntity<String>
	 */
	@GetMapping("/book/find")
	public ResponseEntity<String> findBook(@RequestParam(required = false) String isbn, @RequestParam(required = false) String author) {
		if (isbn == null && author == null) {
			return ResponseEntity.badRequest()
				.body("Either isbn or author should be provided.");
		}
		
		if (isbn != null && author != null) {
			return ResponseEntity.badRequest()
				.body("You could only provide one. Not both.");
		}
		
		if (isbn != null) {
			try {
				Book book = library.findBookByISBN(isbn);
				return ResponseEntity.ok(book.toString());
			}catch (BookNotFoundException e) {
				log.error("", e);
				return ResponseEntity.badRequest().body("Book not found");
			}catch (Exception e) {
				log.error("", e);
			}
			return ResponseEntity.internalServerError().body("Unexpected error occurred");
		} else {
			List<Book> books = library.findBooksByAuthor(author);
			return ResponseEntity.ok().body(books.toString());
		}
	}

	/**
	 * This is an endpoint that is responsible for borrowing book
	 * @param isbn is the isbn of a book.
	 * @return ResponseEntity<String>
	 */
	@PutMapping("/book/borrow")
	public ResponseEntity<String> borrowBook(@RequestParam String isbn) {
		
		try {
			Book book = library.borrowBook(isbn);
			return ResponseEntity.ok(book.toString());
		} 
		catch (BookNotFoundException e) {
			log.error("", e);
			return ResponseEntity.badRequest().body("Book does't exist");
		}
		catch (BookNotAvailableException e) {
			log.error("", e);
			return ResponseEntity.unprocessableEntity().body("Book isn't available");
		}
		catch (Exception e) {
			log.error("", e);
		}
		return ResponseEntity.internalServerError().body("Unexpected error occurred");
	}

	/**
	 * This is an endpoint that is responsible for returning book
	 * @param isbn is the isbn of a book.
	 * @return ResponseEntity<String>
	 */
	@PutMapping("/book/return")
	public ResponseEntity<String> returnBook(@RequestParam String isbn) {
		try {
			Book book = library.returnBook(isbn);
			return ResponseEntity.ok(book.toString());
		}
		catch (BookNotFoundException e) {
			log.error("", e);
			return ResponseEntity.badRequest().body("Book does't exist");
		}
		catch (BookNotAvailableException e) {
			log.error("", e);
			return ResponseEntity.unprocessableEntity().body("Book isn't available");
		}
		catch (Exception e) {
			log.error("", e);

		}
		return ResponseEntity.internalServerError().body("Unexpected error occurred");
	}

}
