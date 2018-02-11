This is simple spring-boot application with CRUD system (Hibernate, MySQL).

1. Preparing to run application.
1.1. login to mysql with root grants
1.2. use command: "CREATE DATABASE LIBRARY;" for create database.
1.3. use command: "CREATE USER 'librarian'@'localhost' IDENTIFIED BY '123456';" for create new user.
1.4. use command: "GRANT ALL PRIVILEGES ON library . * TO 'librarian'@'localhost';" for give privileges to created user.
1.5. you actually may change the name, password, port and database name into src/main/resources/application.properties
2. Run the application.
2.1. unzip archive into some folder.
2.2. open cmd and change directory to this folder.
2.3. use command: "mvn install" for build and run jUnit test's.
2.4. change directory to target using cmd.
2.5. use command: "java -jar library-1.0.jar" for run application.
3. Work with application.
Now you can do http requests to application api.

BASE URL: http://localhost:8080/api/v1/books

GET /api/v1/books 
params: 
1. sort=[id, name, author, isbn],asc[desc]
2. size=value
3. limit=value
4. name [return books where name is contain]
5. author [return books where author is contain]
6. name, author [return books where name and author is contain]
OR
7. isbn=value
example: http://localhost:8080/api/v1/books/?name=it&author=king&sort=id,asc
return the all books with your params.

GET /api/v1/books/{id}
return a single book.

POST /api/v1/books
headers: Content-Type :application/json
body: JSON
params: name(String), author(String), isbn(int), yearOfEstablishment(int).
example:
{
	"name" : "it",
	"author" : "king",
	"isbn" : 1556631948173,
	"yearOfEstablishment" : 2007
}
create a book.

PUT /api/v1/books/{id} 
headers: Content-Type :application/json
body: JSON
params: name(String), author(String), isbn(int), yearOfEstablishment(int).
example:
{
	"name" : "it",
	"author" : "king",
	"isbn" : 1556031948173,
	"yearOfEstablishment" : 2007
}
update a single book.

DELETE /api/v1/books/{id}
delete a single book.
