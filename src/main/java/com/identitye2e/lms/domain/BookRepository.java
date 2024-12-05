package com.identitye2e.lms.domain;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
	
	public Book addBook(Book book);
	public Optional<Book> removeBook(String isbn) ;
	public Optional<Book> findBookByISBN(String isbn);
	public List<Book> findBooksByAuthor(String author) ;
    public Book updateBookAvailabilityCopies(Book book, int count);
}
