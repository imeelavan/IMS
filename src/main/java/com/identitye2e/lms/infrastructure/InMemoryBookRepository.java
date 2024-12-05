package com.identitye2e.lms.infrastructure;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


import com.identitye2e.lms.domain.Book;
import org.springframework.stereotype.Component;

import com.identitye2e.lms.domain.BookRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InMemoryBookRepository implements BookRepository {

	//This map has ISBN as key and Book as value , eg "123123", new Book()
	final ConcurrentHashMap<String, Book> books = new ConcurrentHashMap<>();
	//This map has Author as key and list of their Book's ISBN as value eg "J. K. Rowling", ["123123","456789" ]
	final ConcurrentHashMap<String, List<String>> booksByAuthorCache = new ConcurrentHashMap<>();

	/**
	 * It adds book to the ConcurrentHashMap and update booksByAuthorCache map
	 *
	 * @param book contains the attributes of a book.
	 * @return  the book
	 */
	@Override
	public Book addBook(Book book) {
		books.put(book.getIsbn(), book);

		if (booksByAuthorCache.containsKey(book.getAuthor())) {
			booksByAuthorCache.get(book.getAuthor()).add(book.getIsbn());
		} else {
			List<String> isbnList =new ArrayList<>();
			isbnList.add(book.getIsbn());
			booksByAuthorCache.put(book.getAuthor(),  isbnList);
		}

		return book;
	}

	/**
	 * It removes book from the ConcurrentHashMap and update booksByAuthorCache map
	 *
	 * @param isbn is the isbn of a book.
	 * @return  the book wrapped as an Optional<>, if a book with given isbn exists otherwise Optional.empty()
	 */
	@Override
	public Optional<Book> removeBook(String isbn) {
		Optional<Book> result;
		if (!books.containsKey(isbn)) {
			result = Optional.empty();
		} else {
			Optional<Book> book = findBookByISBN(isbn);
			if (booksByAuthorCache.containsKey(book.get().getAuthor())) {
				booksByAuthorCache.get(book.get().getAuthor()).remove(isbn);
			}
			books.remove(isbn);
			result = book;
		}
		return result;
	}

	/**
	 * It returns book from the ConcurrentHashMap
	 *
	 * @param isbn is the isbn of a book.
	 * @return  the book wrapped as an Optional<>, if a book with given isbn exists otherwise Optional.empty()
	 */
	@Override
	public Optional<Book> findBookByISBN(String isbn) {
		if (!books.containsKey(isbn)) {
			return Optional.empty();
		}
		return Optional.of(books.get(isbn));
	}

	/**
	 * It returns list of books from the ConcurrentHashMap
	 *
	 * @param author is the author of a book.
	 * @return  the list of books, if a book with given author exists otherwise empty list
	 */
	@Override
	public List<Book> findBooksByAuthor(String author) {
		if (!booksByAuthorCache.containsKey(author)) {
			return Collections.emptyList();
		}

		List<String> allISBNs = booksByAuthorCache.get(author);
		List<Book> results = new ArrayList<>();
		for (String isbn: allISBNs)
			results.add(books.get(isbn));

		return results;
	}

	/**
	 * It returns book after updating available copies of the book
	 *
	 * @param book contains the attributes of a book.
	 * @param newCount is the current available copies of a book.
	 * @return  the updated book
	 */
	@Override
	public Book updateBookAvailabilityCopies(Book book, int newCount) {
		book.setAvailableCopies(newCount);
		books.put(book.getIsbn(), book);
		return book;
	}
}
