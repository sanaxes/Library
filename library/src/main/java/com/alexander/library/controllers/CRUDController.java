package com.alexander.library.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.alexander.library.handlers.CRUDRequestHandler;
import com.alexander.library.logic.Book;

@RequestMapping(value = "/api/v1/")
@RestController
public class CRUDController {
  @Autowired
  private CRUDRequestHandler crudRequestHandler;

  // 'localhost:8080/api/v1/books' POST REQUEST [CREATE BOOK]
  @PostMapping(value = "/books")
  public ResponseEntity<Void> createBook(@RequestBody Book book, UriComponentsBuilder ucBuilder) {
    return crudRequestHandler.createBook(book, ucBuilder);
  }

  // 'localhost:8080/api/v1/books' GET REQUEST [ALL BOOKS WITH PARAMS]
  @GetMapping(value = "/books")
  public ResponseEntity<Page<Book>> listAllBooksByNameContain(
      @RequestParam("name") Optional<String> name, @RequestParam("author") Optional<String> author,
      Pageable pageable) {
    return crudRequestHandler.getAllBooksWithParams(name, author, pageable);
  }

  // 'localhost:8080/api/v1/books/{id}' GET REQUEST [SINGLE BOOK]
  @GetMapping(value = "/books/{id}")
  public ResponseEntity<List<Book>> findById(@PathVariable long id) {
    return crudRequestHandler.getSingleBookById(id);
  }

  // 'localhost:8080/api/v1/books' GET REQUEST [GET SINGLE BOOK BY ISBN]
  @GetMapping(value = "/books", params = {"isbn"})
  public ResponseEntity<List<Book>> findByIsbn(@RequestParam("isbn") String isbn,
      UriComponentsBuilder ucBuilder) {
    return crudRequestHandler.getSingleBookByIsbn(isbn, ucBuilder);
  }

  // 'localhost:8080/api/v1/books/{id}' DELETE REQUEST [DELETE BOOK]
  @DeleteMapping(value = "/books/{id}")
  public ResponseEntity<Void> deleteBook(@PathVariable long id) {
    return crudRequestHandler.deleteBook(id);
  }

  // 'localhost:8080/api/v1/books/{id}' PUT REQUEST [UPDATE BOOK]
  @PutMapping(value = "/books/{id}")
  public ResponseEntity<Void> updateBook(@PathVariable long id, @RequestBody Book book,
      UriComponentsBuilder ucBuilder) {
    return crudRequestHandler.updateBook(id, book, ucBuilder);
  }
}
