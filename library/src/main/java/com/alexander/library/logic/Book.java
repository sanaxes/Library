package com.alexander.library.logic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.time.Year;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.google.gson.stream.JsonWriter;

@Entity(name = "Book")
public class Book implements Serializable {
  private static final long serialVersionUID = 5699053157416597624L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(name = "name")
  @NotNull
  private String name;

  @Column(name = "author")
  private String author;

  @Column(name = "isbn", unique = true)
  @NotNull
  @Size(min = 13, max = 13)
  private String isbn;

  @Column(name = "year_of_establishment")
  private Year yearOfEstablishment;

  public Book() {}

  public Book(String name, String author, String isbn, long yearOfEstablishment) {
    this.name = name;
    this.author = author;
    this.isbn = isbn;
    this.yearOfEstablishment = Year.parse(String.valueOf(yearOfEstablishment));
  }

  public boolean isValid() {
    if (this.isbn == null || this.name == null | this.isbn.length() != 13) {
      return false;
    } else {
      return true;
    }
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getIsbn() {
    return this.isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public Year getYearOfEstablishment() {
    return yearOfEstablishment;
  }

  public void setYearOfEstablishment(Year year) {
    this.yearOfEstablishment = year;
  }

  private String toJson() {
    OutputStream outputStream = new ByteArrayOutputStream();
    JsonWriter writer;
    try {
      writer = new JsonWriter(new OutputStreamWriter(outputStream, "UTF-8"));
      writer.beginObject();
      writer.name("name").value(this.name);
      writer.name("author").value(this.author);
      writer.name("isbn").value(this.isbn);
      writer.name("yearOfEstablishment")
          .value(this.yearOfEstablishment == null ? null : this.yearOfEstablishment.getValue());
      writer.endObject();
      writer.close();
      outputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return outputStream.toString();
  }

  @Override
  public String toString() {
    return this.toJson();
  }
}
