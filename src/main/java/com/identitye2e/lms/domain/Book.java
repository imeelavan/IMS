package com.identitye2e.lms.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
public class Book {
    @NonNull
	private String isbn;
    @NonNull
	private String title;
    @NonNull
	private String author;
    @NonNull
	private Integer publicationYear;
    @NonNull
	private Integer availableCopies;
}
