package com.identitye2e.lms.infrastructure;

import com.identitye2e.lms.domain.Book;
import com.identitye2e.lms.domain.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryBookRepositoryTest  {

	private BookRepository bookRepository = new InMemoryBookRepository();

    @BeforeEach
    public void init() {
		Book book1 = Book.builder()
			.isbn("123")
			.title("Harry Potter")
			.author("J. K. Rowling")
			.publicationYear(1997)
			.availableCopies(1)
			.build();

		bookRepository.addBook(book1);
		
		Book book2 = Book.builder()
			.isbn("456")
			.title("The Lord of The Ring")
			.author("J. R. R. Tolkien")
			.publicationYear(1954)
			.availableCopies(1)
			.build();

		bookRepository.addBook(book2);
    }

    @AfterEach
    public void teardown() {
    }

	@Test
	void addBook_should_add_book() {
		// given
		Book book1 = Book.builder()
				.isbn("1234")
				.title("Harry Potter")
				.author("J. K. Rowling")
				.publicationYear(1997)
				.availableCopies(1)
				.build();
		// when
		Book actual = bookRepository.addBook(book1);
		// then
		assertNotNull(actual);
		assertEquals(actual.getTitle(), "Harry Potter");
	}

	@Test
	void removeBook_should_remove_book() {
		// given
		String isbn = "123";
		// when
		Book actual =bookRepository.removeBook(isbn).get();
		// then
		assertNotNull(actual);
		assertEquals(actual.getTitle(), "Harry Potter");
	}
	
	@Test
	void findBookByISBN_should_return_book() {
		// given
		String isbn = "123";
		// when
		Book actual = bookRepository.findBookByISBN(isbn).get();
		// then
		assertNotNull(actual);
		assertEquals(actual.getTitle(), "Harry Potter");
	}

	@Test
	void findBookByISBN_should_not_return_book() {
		// given
		String isbn = "789";

		// when
		Optional<Book> actual = bookRepository.findBookByISBN(isbn);

		// then
		assertTrue(!actual.isPresent());
	}

	@Test
	void findBooksByAuthor_should_return_books() {
		// given
		String author = "J. K. Rowling";
		// when
		List<Book> actuals = bookRepository.findBooksByAuthor(author);
		// then
		assertNotNull(actuals);
		assertEquals(actuals.size(), 1);
		assertEquals(actuals.get(0).getTitle(), "Harry Potter");
	}

	@Test
	void findBooksByAuthor_should_not_return_books() {
		// given
		String author = "Stan Lee";

		// when
		List<Book> actuals = bookRepository.findBooksByAuthor(author);

		// then
		assertEquals(actuals.size(), 0);
	}

	@Test
	void updateBookAvailabilityCopies_should_update_book_count() {
		// given
		Book book1 = Book.builder()
				.isbn("123456")
				.title("Harry Potter")
				.author("J. K. Rowling")
				.publicationYear(1997)
				.availableCopies(1)
				.build();
		// when
		Book actual =bookRepository.updateBookAvailabilityCopies(book1,5);
		// then
		assertNotNull(actual);
		assertEquals(actual.getAvailableCopies(), 5);
	}
}
