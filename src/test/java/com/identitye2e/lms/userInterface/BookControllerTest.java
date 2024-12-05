package com.identitye2e.lms.userInterface;

import com.identitye2e.lms.application.Library;
import com.identitye2e.lms.domain.Book;
import com.identitye2e.lms.domain.BookNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@AutoConfigureJsonTesters
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Library library;

    @Test
    public void findBook_return_400_when_both_input_are_empty() throws Exception {
		mockMvc.perform(get("/book/find"))
			.andExpect(status().isBadRequest());
    }

    @Test
    public void findBook_return_400_when_both_input_are_present() throws Exception {
		mockMvc.perform(get("/book/find?isbn=123&author=J. K. Rowling"))
			.andExpect(status().isBadRequest());
    }

    @Test
    public void findBook_return_200_when_isbn_is_valid() throws Exception {

        when(library.findBookByISBN("123"))
			.thenReturn(Book.builder()
				.isbn("123")
				.title("Harry Potter")
				.author("J. K. Rowling")
				.publicationYear(1997)
				.availableCopies(1)
				.build());

		mockMvc.perform(get("/book/find?isbn=123"))
			.andExpect(status().isOk());
    }

    @Test
    public void findBook_return_400_when_isbn_is_invalid() throws Exception {
		when(library.findBookByISBN("789"))
			.thenThrow(BookNotFoundException.class);

		mockMvc.perform(get("/book/find?isbn=789"))
			.andExpect(status().isBadRequest());
    }

    @Test
    public void findBook_return_200_when_author_is_valid() throws Exception {
		Book book = Book.builder()
			.isbn("123")
			.title("Harry Potter")
			.author("J. K. Rowling")
			.publicationYear(1997)
			.availableCopies(1)
			.build();
		List<Book> books = new ArrayList<>();
		books.add(book);

		when(library.findBooksByAuthor("J. K. Rowling"))
			.thenReturn(books);

		mockMvc.perform(get("/book/find?author=J. K. Rowling"))
			.andExpect(status().isOk());
    }

    @Test
    public void findBook_return_200_when_author_is_invalid() throws Exception {
		mockMvc.perform(get("/book/find?author=Stan Lee"))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("[]")));
    }
}
