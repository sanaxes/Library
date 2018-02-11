package com.alexander.library.logic;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "/api/v1/books", path = "/api/v1/books")
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {

  Page<Book> findAll(Pageable p);

  List<Book> findById(long id);

  List<Book> findByIsbn(String isbn);

  Page<Book> findByNameContaining(String name, Pageable p);

  Page<Book> findByAuthorContaining(String author, Pageable p);

  Page<Book> findByNameContainingAndAuthorContaining(String name, String author, Pageable p);

}
