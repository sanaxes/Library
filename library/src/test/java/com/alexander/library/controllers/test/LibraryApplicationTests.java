package com.alexander.library.controllers.test;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.alexander.library.logic.Book;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LibraryApplicationTests {
  @Autowired
  private MockMvc mockMvc;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private static int counterOfTests = 1;

  @Test
  // POST, GET, PUT and DELETE Request's OK
  public void shouldCreateReadUpdateAndDeleteBook() throws Exception {
    logger.info("TEST #" + counterOfTests + " CREATE, READ, UPDATE, DELETE");
    Book book = new Book("somebook", "someauthor", "0000000000000", 2000);
    MockHttpServletResponse responsePost = doPostAndGetResponse(book);
    assertEquals(HttpStatus.CREATED.value(), responsePost.getStatus());
    int id = Integer.valueOf(responsePost.getHeader("id"));
    int responseGet = doGet(id, new LinkedMultiValueMap<>());
    assertEquals(HttpStatus.OK.value(), responseGet);
    int responsePut = doPut(book, id);
    assertEquals(HttpStatus.OK.value(), responsePut);
    int responseDelete = doDelete(id);
    assertEquals(HttpStatus.OK.value(), responseDelete);
    logger.info("END OF TEST #" + counterOfTests++);
  }

  @Test
  // OK CONTAIN
  public void shouldCreateAndFindContainByNameAndAuthor() throws Exception {
    logger.info("TEST #" + counterOfTests + " FIND BY CONTAIN");
    Book book = new Book("somebook", "someauthor", "0000000000000", 2000);
    MockHttpServletResponse responsePost = doPostAndGetResponse(book);
    assertEquals(HttpStatus.CREATED.value(), responsePost.getStatus());
    int id = Integer.valueOf(responsePost.getHeader("id"));
    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
    map.add("name", "name");
    int responseNameContain = doGet(id, map);
    assertEquals(HttpStatus.OK.value(), responseNameContain);
    map.add("author", "author");
    int responseNameAndAuthorContain = doGet(id, map);
    assertEquals(HttpStatus.OK.value(), responseNameAndAuthorContain);
    map.remove("name");
    int responseAuthorContain = doGet(id, map);
    assertEquals(HttpStatus.OK.value(), responseAuthorContain);
    int responseDelete = doDelete(id);
    assertEquals(HttpStatus.OK.value(), responseDelete);
    logger.info("END OF TEST #" + counterOfTests++);
  }

  @Test
  // NO CONTENT
  public void shouldReturnNoContent() throws Exception {
    logger.info("TEST #" + counterOfTests + " NO CONTENT");
    int responseGetNoId = doGet(0, new LinkedMultiValueMap<>());
    assertEquals(HttpStatus.NOT_FOUND.value(), responseGetNoId);
    int responseDeleteNoId = doDelete(0);
    assertEquals(HttpStatus.NOT_FOUND.value(), responseDeleteNoId);
    logger.info("END OF TEST #" + counterOfTests++);
  }

  private MockHttpServletResponse doPostAndGetResponse(Book book) throws Exception {
    return this.mockMvc
        .perform(
            post("/api/v1/books/").contentType(MediaType.APPLICATION_JSON).content(book.toString()))
        .andReturn().getResponse();
  }

  private int doGet(int id, MultiValueMap<String, String> params) throws Exception {
    return this.mockMvc.perform(get("/api/v1/books/" + id).params(params)).andReturn().getResponse()
        .getStatus();
  }

  private int doPut(Book book, int id) throws Exception {
    return this.mockMvc.perform(
        put("/api/v1/books/" + id).contentType(MediaType.APPLICATION_JSON).content(book.toString()))
        .andReturn().getResponse().getStatus();
  }

  private int doDelete(int id) throws Exception {
    return this.mockMvc.perform(delete("/api/v1/books/" + id)).andReturn().getResponse()
        .getStatus();
  }

}
