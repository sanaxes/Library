package com.alexander.library.handlers;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import com.alexander.library.logic.Book;
import com.alexander.library.logic.BookRepository;

@Service
public class CRUDRequestHandler {

  @Autowired
  private BookRepository bookRepository;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  // CREATE BOOK
  public ResponseEntity<Void> createBook(Book book, UriComponentsBuilder ucBuilder) {
    logger.info("POST REQUEST. CREATE BOOK. PATH: /api/v1/books");
    if (bookRepository.exists(book.getId())) {
      logger.info("A BOOK WITH THIS ID: " + book.getId() + " ALREADY EXISTS");
      return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }
    if (!bookRepository.findByIsbn(book.getIsbn()).isEmpty()) {
      logger.info("A BOOK WITH THIS ISBN: " + book.getIsbn() + " ALREADY EXISTS");
      return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }
    if (!book.isValid()) {
      logger.info("NAME OR ISBN CANNOT BE NULL AND ISBN LENGTH WOULD BE 13 CHARACTERS");
      return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }
    bookRepository.save(book);
    logger.info("BOOK " + bookRepository.findOne(book.getId()).toString() + " CREATED");
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(ucBuilder.path("/api/v1//books/{id}").buildAndExpand(book.getId()).toUri());
    headers.add("id", String.valueOf(book.getId()));
    return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
  }

  // GET ALL BOOKS WITH PARAMS
  public ResponseEntity<Page<Book>> getAllBooksWithParams(Optional<String> name,
      Optional<String> author, Pageable pageable) {
    logger.info("GET REQUEST." + " PARAMS: [name: {" + (name.isPresent() ? name.get() : "")
        + "}, author: {" + (author.isPresent() ? author.get() : "")
        + "}]. GET ALL BOOKS WITH PARAMS. PATH: /api/v1/books");
    Page<Book> books = bookRepository.findAll(pageable);
    if (name.isPresent() && author.isPresent()) {
      books = bookRepository.findByNameContainingAndAuthorContaining(name.get(), author.get(),
          pageable);
    } else if (name.isPresent()) {
      books = bookRepository.findByNameContaining(name.get(), pageable);
    } else if (author.isPresent()) {
      books = bookRepository.findByAuthorContaining(author.get(), pageable);
    }
    if (!books.hasContent()) {
      logger.info("NO CONTENT");
      return new ResponseEntity<Page<Book>>(HttpStatus.NO_CONTENT);
    }
    logger.info("List: " + books.getContent());
    return new ResponseEntity<Page<Book>>(books, HttpStatus.OK);
  }

  // GET SINGLE BOOK
  public ResponseEntity<List<Book>> getSingleBookById(long id) {
    logger.info("GET REQUEST." + " PARAMS: [id: " + id + "]. GET SINGLE BOOK. PATH: /api/v1/books/"
        + String.valueOf(id));
    List<Book> book = bookRepository.findById(id);
    if (book.isEmpty()) {
      logger.info("NO CONTENT");
      return new ResponseEntity<List<Book>>(HttpStatus.NOT_FOUND);
    }
    logger.info("Book: " + book.toString());
    return new ResponseEntity<List<Book>>(book, HttpStatus.OK);
  }

  // GET SINGLE BOOK BY ISBN
  public ResponseEntity<List<Book>> getSingleBookByIsbn(String isbn) {
    logger.info("GET REQUEST." + " PARAMS: [isbn: " + isbn
        + "]. GET SINGLE BOOK BY ISBN. PATH: /api/v1/books/");
    List<Book> book = bookRepository.findByIsbn(isbn);
    if (book.isEmpty()) {
      logger.info("NO CONTENT");
      return new ResponseEntity<List<Book>>(HttpStatus.NO_CONTENT);
    }
    logger.info("Book: " + book.toString());
    HttpHeaders headers = new HttpHeaders();
    headers.add("id", String.valueOf(book.get(0).getId()));
    return new ResponseEntity<List<Book>>(book, headers, HttpStatus.OK);
  }

  // DELETE BOOK
  public ResponseEntity<Void> deleteBook(long id) {
    logger.info("DELETE REQUEST. DELETE BOOK. PATH: /api/v1/books/" + String.valueOf(id));
    if (!bookRepository.exists(id)) {
      logger.info("A BOOK WITH ID: [" + id + "] DOES NOT EXISTS");
      return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }
    bookRepository.delete(id);
    logger.info("BOOK WITH ID: [" + id + "] IS DELETED");
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

  // UPDATE BOOK
  public ResponseEntity<Void> updateBook(long id, Book book, UriComponentsBuilder ucBuilder) {
    logger.info("PUT REQUEST. UPDATE BOOK. PATH: /api/v1/books/" + String.valueOf(id));
    if (!bookRepository.exists(id)) {
      logger.info("A BOOK WITH ID: [" + id + "] DOES NOT EXISTS");
      return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }
    if (!book.isValid()) {
      logger.info("NAME OR ISBN CANNOT BE NULL AND ISBN LENGTH WOULD BE 13 CHARACTERS");
      return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }
    book.setId(id);
    Book bookForCheckIsbn = bookRepository.findByIsbn(book.getIsbn()).get(0);
    if (bookForCheckIsbn.getId() != book.getId()) {
      logger.info("THIS ISBN: [" + book.getIsbn() + "] ALREADY EXISTS INTO LIBRARY");
      return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }
    bookRepository.save(book);
    logger.info("BOOK " + book.toString() + " UPDATED");
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(ucBuilder.path("/api/v1/books/{id}").buildAndExpand(book.getId()).toUri());
    return new ResponseEntity<Void>(headers, HttpStatus.OK);
  }

}
