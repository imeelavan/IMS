package com.identitye2e.lms.application;

import com.identitye2e.lms.domain.*;
import com.identitye2e.lms.infrastructure.InMemoryBookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Library.class)
@Import(LibraryTest.TestConfig.class)
public class LibraryTest {

    @Autowired
    private Library library;

	@TestConfiguration
    static class TestConfig {
        @Bean
        public BookRepository bookRepository() {
			BookRepository bookRepository = new InMemoryBookRepository();

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

            return bookRepository;
        }
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
		Book actual = library.addBook(book1);
		// then
		assertNotNull(actual);
		assertEquals(actual.getTitle(), "Harry Potter");
	}

	@Test
	void addBook_should_throw_bookEntryInvalidException() {
		// given
		Book book1 = Book.builder()
				.isbn("1234")
				.title("Harry Potter")
				.author("J. K. Rowling")
				.publicationYear(1997)
				.availableCopies(-1)
				.build();

		// when
		Exception exception = assertThrows(BookEntryInvalidException.class, () -> {
			library.addBook(book1);
		});

		// then
		assertTrue(exception.getMessage().contains("Invalid book entry, please check the payload"));
	}

	@Test
	void addBook_should_throw_bookAlreadyExistException() {
		// given
		Book book1 = Book.builder()
				.isbn("12345")
				.title("Harry Potter")
				.author("J. K. Rowling")
				.publicationYear(1997)
				.availableCopies(1)
				.build();

		Book book2 = Book.builder()
				.isbn("12345")
				.title("Test")
				.author("J. K. Rowling")
				.publicationYear(1998)
				.availableCopies(1)
				.build();
		library.addBook(book1);
		// when
		Exception exception = assertThrows(BookAlreadyExistException.class, () -> {
			library.addBook(book2);
		});
		// then
		assertTrue(exception.getMessage().contains("Book already exists"));
	}

	@Test
	void findBookByISBN_should_return_book() {
		// given
		String isbn = "123";
		// when
		Book actual = library.findBookByISBN(isbn);
		// then
		assertNotNull(actual);
		assertEquals(actual.getTitle(), "Harry Potter");
	}

	@Test
	void findBookByISBN_should_not_return_book() {
		// given
		String isbn = "789";
		// when

		Exception exception = assertThrows(BookNotFoundException.class, () -> {
			library.findBookByISBN(isbn);
		});
		// then
		assertTrue(exception.getMessage().contains("Book not found"));
	}

	@Test
	void findBooksByAuthor_should_return_books() {
		// given
		String author = "J. K. Rowling";
		// when
		List<Book> actuals = library.findBooksByAuthor(author);
		// then
		assertNotNull(actuals);
		assertEquals(actuals.get(0).getTitle(), "Harry Potter");
	}

	@Test
	void findBooksByAuthor_should_not_return_books() {
		// given
		String author = "Stan Lee";
		// when
		List<Book> actuals = library.findBooksByAuthor(author);
		// then
		assertNotNull(actuals);
		assertEquals(actuals.size(), 0);
	}

	@Test
	void removeBook_should_remove_book() {
		// given
		String isbn = "123";
		// when
		Book actual =library.removeBook(isbn);
		// then
		assertNotNull(actual);
		assertEquals(actual.getTitle(), "Harry Potter");
	}

	@Test
	void removeBook_should_throw_bookNotFoundException() {
		// given
		Book book1 = Book.builder()
				.isbn("12342")
				.title("Harry Potter")
				.author("J. K. Rowling")
				.publicationYear(1997)
				.availableCopies(1)
				.build();
		library.addBook(book1);
		// when

		Exception exception = assertThrows(BookNotFoundException.class, () -> {
			library.removeBook("12");
		});
		// then
		assertTrue(exception.getMessage().contains("Book not found"));
	}

	@Test
	void borrowBook_should_reduce_book_count() {
		// given
		Book book1 = Book.builder()
				.isbn("12343")
				.title("Harry Potter")
				.author("J. K. Rowling")
				.publicationYear(1997)
				.availableCopies(1)
				.build();
		library.addBook(book1);
		// when
		Book actual =library.borrowBook(book1.getIsbn());
		// then
		assertEquals(actual.getAvailableCopies(), 0);
	}

	@Test
	void borrowBook_should_throw_bookNotFoundException() {
		// given
		Book book1 = Book.builder()
				.isbn("12344")
				.title("Harry Potter")
				.author("J. K. Rowling")
				.publicationYear(1997)
				.availableCopies(1)
				.build();
		library.addBook(book1);
		// when

		Exception exception = assertThrows(BookNotFoundException.class, () -> {
			library.borrowBook("12");
		});
		// then
		assertTrue(exception.getMessage().contains("Book not found"));
	}

	@Test
	void borrowBook_should_throw_bookNotAvailableException() {
		// given
		Book book1 = Book.builder()
				.isbn("12346")
				.title("Harry Potter")
				.author("J. K. Rowling")
				.publicationYear(1997)
				.availableCopies(1)
				.build();
		library.addBook(book1);
		//borrow this book here to reduce count, so that when borrowing again you get "No available copies"
		library.borrowBook(book1.getIsbn());
		// when
		Exception exception = assertThrows(BookNotAvailableException.class, () -> {
			library.borrowBook(book1.getIsbn());
		});
		// then
		assertTrue(exception.getMessage().contains("No available copies"));
	}

	@Test
	void returnBook_should_increase_book_count() {
		// given
		Book book1 = Book.builder()
				.isbn("12347")
				.title("Harry Potter")
				.author("J. K. Rowling")
				.publicationYear(1997)
				.availableCopies(1)
				.build();
		library.addBook(book1);
		// when
		Book actual =library.returnBook(book1.getIsbn());
		// then
		assertEquals(actual.getAvailableCopies(), 2);
	}

	@Test
	void returnBook_should_throw_bookNotFoundException() {
		// given
		Book book1 = Book.builder()
				.isbn("12348")
				.title("Harry Potter")
				.author("J. K. Rowling")
				.publicationYear(1997)
				.availableCopies(1)
				.build();
		library.addBook(book1);
		// when

		Exception exception = assertThrows(BookNotFoundException.class, () -> {
			library.returnBook("12");
		});
		// then
		assertTrue(exception.getMessage().contains("Book not found"));
	}
}
