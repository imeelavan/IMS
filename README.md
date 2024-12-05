# LMS
Implementation of Concurrent Library Management System (LMS) with RESTful API

This is my implementation for Library Management System (LMS). I followed Domain Driven Design (DDD) for this implementation. I have introduced 4 custom exceptions and extensive test coverage for the following classes <br>
Library class - LibraryTest.java <br>
InMemoryBookRepository class - InMemoryBookRepositoryTest.java <br>
BookController class - BookControllerTest.java

# How to Run
Open LMS source in your IDE and locate Maven tab. Then navigate to Lms -> Plugins -> spring-boot and click on spring-boot:run
This will get application up and running on default port 8080. Now open Postman use below endpoints and requests for each of the actions listed below.

**1. Add Book** <br>
Endpoint - http://localhost:8080/book/add <br>
Request - 

{
    "isbn": "123",
    "title": "Test title",
    "author": "author1",
    "publicationYear": "2000",
    "availableCopies": "1"       
}

<br>
Response - <br>
Book(isbn=123, title=test, author=test, publicationYear=2000, availableCopies=5)

Submit few requests by changing the values above so that you will have few books in the library to test with.

Since the below 5 operations use @RequestParam, you can use Postman with appropriate option (put, delete, and get) selected from operation dropdown for the endpoints below without body

**2. Remove Book** <br>
Endpoint - http://localhost:8080/book/remove?isbn=123 <br>
Operation - **DELETE** <br>
Response - <br>
Book(isbn=123, title=test1, author=test1, publicationYear=2000, availableCopies=1)

**3. Find Book by ISBN** <br>
Endpoint - http://localhost:8080/book/find?isbn=123 <br>
Operation - **GET** <br>
Response - <br>
Book(isbn=123, title=test1, author=test1, publicationYear=2000, availableCopies=1)

**4. Find Book by Author** <br>
Endpoint - http://localhost:8080/book/find?author=author1 <br>
Operation - **GET** <br>
Response - <br>
[Book(isbn=123, title=test1, author=author1, publicationYear=2000, availableCopies=1), Book(isbn=123456, title=test1, author=author1, publicationYear=2000, availableCopies=1)]

**5. Borrow Book** <br>
Endpoint - http://localhost:8080/book/borrow?isbn=123 <br>
Operation - **PUT** <br>
Response - <br> 
Book(isbn=123, title=test1, author=author1, publicationYear=2000, availableCopies=0)

**6. Return Book** <br>
Endpoint - http://localhost:8080/book/return?isbn=123 <br>
Operation - **PUT** <br>
Response - <br>
Book(isbn=123, title=test1, author=author1, publicationYear=2000, availableCopies=1)

# Assumptions
I assumed that all attributes of Book class are mandatory and annotate then with @NonNull